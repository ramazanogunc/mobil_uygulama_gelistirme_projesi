package com.cbu.mobil_dersi_projesi.ui.fragment.profileTabFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.cbu.mobil_dersi_projesi.data.AppSharedPreference
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentRegisterAndEditBinding
import com.cbu.mobil_dersi_projesi.helper.LoadingDialog
import com.cbu.mobil_dersi_projesi.helper.hide
import com.cbu.mobil_dersi_projesi.helper.toast
import com.cbu.mobil_dersi_projesi.model.User
import com.cbu.mobil_dersi_projesi.viewModel.ProfileEditViewModel
import com.cbu.mobil_dersi_projesi.viewModel.ProfileEditViewModelFactory

class ProfileEditFragment:Fragment() {
    private var _binding: FragmentRegisterAndEditBinding? = null
    private val binding get() = _binding!!

    private val profileEditViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ProfileEditViewModelFactory(
                UserRepository(
                    AppDatabase.getInstance(requireActivity()).userDao()
                )
            )
        ).get(ProfileEditViewModel::class.java)
    }
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterAndEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        getData()
    }

    private fun getData() {
        val currentUserId = AppSharedPreference(requireContext()).getCurrentUserId()
        profileEditViewModel.getByUserId(currentUserId){ user ->
            binding.nameSurname.setText(user.nameSurname)
            binding.email.setText(user.email)
            binding.password.setText(user.password)
        }
    }

    private fun initUi() {
        binding.btnSave.setOnClickListener { onClickBtnSave() }
        binding.appbar.hide()
        binding.btnSave.text = "Güncelle"
        profileEditViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                loadingDialog.show()
            else
                loadingDialog.hide()
        }
    }

    private fun onClickBtnSave() {
        if (validateEditTexts()) {
            val nameSurname = binding.nameSurname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val user = User(nameSurname, email, password)
            user.userId = AppSharedPreference(requireContext()).getCurrentUserId()
            profileEditViewModel.update(user){
                toast("Profil güncellendi.")
            }
        }
    }

    private fun validateEditTexts(): Boolean {
        binding.nameSurname.error = null
        binding.email.error = null
        binding.password.error = null

        if (binding.nameSurname.text.isNullOrBlank()) binding.nameSurname.error = "required"
        if (binding.email.text.isNullOrBlank()) binding.email.error = "required"
        if (binding.password.text.isNullOrBlank()) binding.password.error = "required"

        return binding.nameSurname.error == null && binding.email.error == null && binding.password.error == null
    }
}