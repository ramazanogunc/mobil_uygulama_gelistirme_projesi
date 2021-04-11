package com.cbu.mobil_dersi_projesi.ui.mekanAddEdit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.cbu.mobil_dersi_projesi.data.AppSharedPreference
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentMekanAddEditBinding
import com.cbu.mobil_dersi_projesi.util.helper.LoadingDialog
import com.cbu.mobil_dersi_projesi.util.helper.toast
import com.cbu.mobil_dersi_projesi.viewModel.MekanAddEditViewModel
import com.cbu.mobil_dersi_projesi.viewModel.MekanAddEditViewModelFactory
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.location.places.ui.PlacePicker.getPlace
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog


class MekanAddEditFragment : Fragment() {
    private var _binding: FragmentMekanAddEditBinding? = null
    private val binding get() = _binding!!

    private val mekanAddEditViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MekanAddEditViewModelFactory(
                MekanRepository(
                    AppDatabase.getInstance(requireActivity()).mekanDao()
                )
            )
        ).get(MekanAddEditViewModel::class.java)
    }

    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    companion object {
        val MODE_ADD = 0
        val MODE_EDIT = 1
    }

    private var MODE = MODE_ADD

    private var mekanId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMekanAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) MODE = requireArguments().getInt("mode")
        initUi()
        doEditMode { getDataAndSetUi() }
    }

    private fun getDataAndSetUi() {
        mekanId = requireArguments().getInt("mekanId")
        mekanAddEditViewModel.get(mekanId).observe(viewLifecycleOwner) { mekan ->
            toast(mekan.toString())
            binding.name.setText(mekan.name)
            binding.description.setText(mekan.description)
            if (mekan._img1 != null) {
                binding.img1.setImageBitmap(mekan._img1)
                isSelectImg1 = true
            }
            if (mekan._img2 != null) {
                binding.img2.setImageBitmap(mekan._img2)
                isSelectImg2 = true
            }
            if (mekan._img3 != null) {
                binding.img3.setImageBitmap(mekan._img3)
                isSelectImg3 = true
            }
            latitude = mekan.latitude
            longitude = mekan.longitude
            isSelectLocation = true
            binding.location.text = "Konum değerleri : $latitude , $longitude"
        }
    }

    private fun initUi() {
        binding.btnSave.setOnClickListener { onClickBtnSave() }
        binding.img1.setOnClickListener {
            isSelectImg1 = false
            pickImages(binding.img1, 1)
        }
        binding.img2.setOnClickListener {
            isSelectImg2 = false
            pickImages(binding.img2, 2)
        }
        binding.img3.setOnClickListener {
            isSelectImg3 = false
            pickImages(binding.img3, 3)
        }
        binding.btnPickLocation.setOnClickListener { onClickBtnPickLocation() }
        doEditMode { binding.collapsingToolbarLayout.title = "Mekan Düzenle" }
        mekanAddEditViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                loadingDialog.show()
            else
                loadingDialog.hide()
        }
    }

    private val PLACE_PICKER_REQUEST = 1900

    private fun onClickBtnPickLocation() {
        isSelectLocation = false
        binding.location.text = "konum seçilmedi"
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(requireActivity()), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    private fun onClickBtnSave() {
        if (validateInPuts()) {
            val mekan = getMekanFromUi()
            toast(mekan.toString())
            doAddMode { saveMekan(mekan) }
            doEditMode { updateMekan(mekan) }
        } else
            toast("Lütfen boş alanları doldurun.")
    }

    private fun updateMekan(mekan: Mekan) {
        mekan.userId = AppSharedPreference(requireContext()).getCurrentUserId()
        mekanAddEditViewModel.update(mekan) {
            toast("Mekan Güncellendi")
        }
    }

    private fun saveMekan(mekan: Mekan) {
        mekanAddEditViewModel.insert(mekan) {
            toast("Yeni mekan eklendi")
            requireActivity().onBackPressed()
        }
    }

    private fun getMekanFromUi(): Mekan {
        val name = binding.name.text.toString()
        val description = binding.description.text.toString()
        val img1: Bitmap? = if (isSelectImg1) imageViewToBitmap(binding.img1) else null
        val img2: Bitmap? = if (isSelectImg2) imageViewToBitmap(binding.img2) else null
        val img3: Bitmap? = if (isSelectImg3) imageViewToBitmap(binding.img3) else null
        return Mekan(
            name,
            description,
            latitude,
            longitude,
            img1,
            img2,
            img3,
            AppSharedPreference(requireContext()).getCurrentUserId()
        )
    }

    var isSelectImg1 = false
    var isSelectImg2 = false
    var isSelectImg3 = false

    var isSelectLocation = false

    private fun validateInPuts(): Boolean {
        var isValidate = true
        binding.name.error = null
        binding.description.error = null

        if (binding.name.text.isNullOrBlank()) {
            binding.name.error = "required"
            isValidate = false
        }
        if (binding.description.text.isNullOrBlank()) {
            binding.description.error = "required"
            isValidate = false
        }
        if (!isSelectLocation) isValidate = false
        return isValidate
    }

    lateinit var selectedImageView: ImageView

    fun pickImages(selectedImageView: ImageView, whichImage: Int) {
        this.selectedImageView = selectedImageView
        PickImageDialog.build(
            PickSetup().setTitle("Kaynak")
                .setProgressText("Yükleniyor")
                .setCancelText("İptal")
                .setCameraButtonText("Kamera")
                .setGalleryButtonText("Galeri")
        )
            .setOnPickResult {
                selectedImageView.setImageURI(it.uri)
                if (whichImage == 1) isSelectImg1 = true
                if (whichImage == 2) isSelectImg2 = true
                if (whichImage == 3) isSelectImg3 = true
            }
            .setOnPickCancel {
            }.show(childFragmentManager)

    }


    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                val place = getPlace(requireContext(), data)

                latitude = place.latLng.latitude
                longitude = place.latLng.longitude
                binding.location.text = "Konum değerleri : $latitude , $longitude"
                isSelectLocation = true
            }
        }
    }

    private fun imageViewToBitmap(imageView: ImageView): Bitmap {
        val bitmapDrawable = imageView.drawable as BitmapDrawable
        val bitmap: Bitmap
        if (bitmapDrawable == null) {
            imageView.buildDrawingCache()
            bitmap = imageView.drawingCache
            imageView.buildDrawingCache(false)
        } else {
            bitmap = bitmapDrawable.bitmap
        }
        return bitmap
    }

    private fun doAddMode(function: () -> Unit) {
        if (MODE == MODE_ADD)
            function()
    }

    private fun doEditMode(function: () -> Unit) {
        if (MODE == MODE_EDIT)
            function()
    }
}