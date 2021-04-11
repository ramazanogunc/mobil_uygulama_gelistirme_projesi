package com.cbu.mobil_dersi_projesi.ui.mekanDetail

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.data.repository.MekanDetailRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentMekanDetailBinding
import com.cbu.mobil_dersi_projesi.network.WeatherClient
import com.cbu.mobil_dersi_projesi.util.Status
import java.util.*


class MekanDetailFragment : Fragment() {

    private var mekan: Mekan? = null
    private var _binding: FragmentMekanDetailBinding? = null
    private val binding get() = _binding!!

    private val mekanDetailViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MekanDetailViewModelFactory(
                MekanDetailRepository(
                    WeatherClient.getService()
                )
            )
        ).get(MekanDetailViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMekanDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mekan = requireArguments().getParcelable<Mekan>("mekan")

        Log.e("city", getCity(mekan!!.latitude, mekan!!.longitude))

        mekanDetailViewModel.weather.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    binding.txtMekanDesc.text = getCity(mekan!!.latitude, mekan!!.longitude)
                    binding.txtMekanWeatherToday.text = it.data?.first()?.day

                }

                Status.ERROR -> {

                }
            }
        })

    }

    private fun getCity(lat: Double, lng: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
        return addresses.first().adminArea
    }

}