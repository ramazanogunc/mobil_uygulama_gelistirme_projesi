package com.cbu.mobil_dersi_projesi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cbu.mobil_dersi_projesi.R
import com.cbu.mobil_dersi_projesi.data.AppSharedPreference
import com.cbu.mobil_dersi_projesi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.menu.clear();
        binding.bottomNavigation.inflateMenu(getBottomMenu())
        // init nav host fragment
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun getBottomMenu() =
        if (AppSharedPreference(this).isLogin()) R.menu.bottom_menu_for_user
        else R.menu.bottom_menu_for_guest
}