package com.cbu.mobil_dersi_projesi.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentHomeBinding
import com.cbu.mobil_dersi_projesi.helper.mekanInfoDialog
import com.cbu.mobil_dersi_projesi.viewModel.HomeViewModel
import com.cbu.mobil_dersi_projesi.viewModel.HomeViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*


class HomeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            HomeViewModelFactory(
                MekanRepository(
                    AppDatabase.getInstance(requireActivity()).mekanDao()
                )
            )
        ).get(HomeViewModel::class.java)
    }

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleMap(savedInstanceState)
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        binding.mapView.onCreate(mapViewBundle)
        binding.mapView.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        if(_binding != null && binding.mapView != null) binding.mapView.onSaveInstanceState(mapViewBundle)
    }



    var isFirstLoad: Boolean = true

    override fun onMapReady(map: GoogleMap?) {
        if (checkLocationPermission()) {
            map?.isMyLocationEnabled = true
            map?.setOnMyLocationChangeListener { location ->
                if (isFirstLoad) {
                    moveCurrentLocaiton(map, location)
                    circleDraw(map, location.latitude, location.longitude)
                    isFirstLoad = false
                }
            }
        } else {
            requestLocationPermission()
        }

        homeViewModel.getAll().observe(viewLifecycleOwner) { mekanList ->
            //mark all mekans
            mekanList.forEach { mekan ->
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(mekan.latitude, mekan.longitude))
                markerOptions.title(mekan.name)
                val marker = map?.addMarker(markerOptions)
                marker?.tag = mekan.mekanId
            }
        }

        map?.setOnMarkerClickListener { marker ->
            homeViewModel.get(marker.tag as Int).observe(viewLifecycleOwner){ mekan->
                mekanInfoDialog(requireContext(), mekan)
            }
            return@setOnMarkerClickListener true
        }
    }

    private fun circleDraw(map: GoogleMap?, latitude: Double, longitude: Double) {
        map?.addCircle(
            CircleOptions()
                .center(LatLng(latitude, longitude))
                .radius(1000.0)
                .strokeColor(Color.BLACK)
                .strokeWidth(2f)
                .fillColor(Color.argb(50, 238, 116, 116))
        )
    }

    private fun moveCurrentLocaiton(map: GoogleMap?, location: Location) {
        val myPosition = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude)).zoom(17f)
            .tilt(30f).build()
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(myPosition)
        )
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            return true
        return false
    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private fun requestLocationPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {

            AlertDialog.Builder(requireActivity())
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton(
                    "OK"
                ) { _, _ -> //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    // mapview lifecycle
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}