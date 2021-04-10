package com.cbu.mobil_dersi_projesi.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cbu.mobil_dersi_projesi.ui.profileTab.MekanListFragment
import com.cbu.mobil_dersi_projesi.ui.profileTab.ProfileEditFragment

class ProfileTabFragmentAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Mekanlarım", "Profil Düzenle")

    override fun getCount(): Int = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence? =
        tabTitles[position]

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> MekanListFragment()
        1 -> ProfileEditFragment()
        else -> MekanListFragment()
    }

}