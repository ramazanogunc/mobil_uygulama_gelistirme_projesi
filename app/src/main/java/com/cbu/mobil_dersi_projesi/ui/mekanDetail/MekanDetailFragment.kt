package com.cbu.mobil_dersi_projesi.ui.mekanDetail

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.data.model.Weather
import com.cbu.mobil_dersi_projesi.data.repository.MekanDetailRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentMekanDetailBinding
import com.cbu.mobil_dersi_projesi.network.WeatherClient
import com.cbu.mobil_dersi_projesi.util.Status
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
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

        val cityName = getCityName(mekan!!.latitude, mekan!!.longitude)
        Log.e("city", cityName)

        setMekanDetails()
        mekanDetailViewModel.getWeather("en", cityName)
        observeWeather()
    }

    private fun observeWeather() {
        mekanDetailViewModel.weather.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    setWeathers(it.data!!)
                }
                Status.ERROR -> {
                }
            }
        })
    }

    private fun setWeathers(weathers: List<Weather>) {
        val today = weathers.first()
        val tomorrow = weathers[1]

        // today
        binding.txtMekanWeatherToday.text = "${today.day} - ${today.description}\n" +
                "${today.degree}"
        Glide.with(requireContext()).load(today.icon).centerCrop().into(binding.imageWeatherToday)

        // tomorrow
        binding.txtMekanWeatherTomorrow.text = "${tomorrow.day} - ${tomorrow.description}\n" +
                "${tomorrow.degree}"
        val imgUri1 = today.icon!!.toUri().buildUpon().scheme("https").build()
        GlideToVectorYou.justLoadImage(activity, imgUri1, binding.imageWeatherToday)

        val imgUri2 = tomorrow.icon!!.toUri().buildUpon().scheme("https").build()
        GlideToVectorYou.justLoadImage(activity, imgUri2, binding.imageWeatherTomorrow)

    }
    private fun setMekanDetails(){
        binding.toolbar.title = mekan!!.name
        binding.txtMekanDesc.text = "Description\n\n${mekan!!.description}"
        binding.txtMekanRotation.text = "Locale:\n\n" +
                                        "Latitude: ${mekan!!.latitude}\n" +
                                        "Longitude: ${mekan!!.longitude}"
    }

    private fun getCityName(lat: Double, lng: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
        return addresses.first().adminArea
    }

}