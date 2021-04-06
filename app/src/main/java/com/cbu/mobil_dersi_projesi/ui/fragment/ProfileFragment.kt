package com.cbu.mobil_dersi_projesi.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cbu.mobil_dersi_projesi.R
import com.cbu.mobil_dersi_projesi.data.AppSharedPreference
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentProfileBinding
import com.cbu.mobil_dersi_projesi.helper.alertConfirm
import com.cbu.mobil_dersi_projesi.helper.toast
import com.cbu.mobil_dersi_projesi.ui.MainActivity
import com.cbu.mobil_dersi_projesi.ui.adapter.ProfileTabFragmentAdapter
import com.cbu.mobil_dersi_projesi.viewModel.ProfileViewModel
import com.cbu.mobil_dersi_projesi.viewModel.ProfileViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ProfileViewModelFactory(
                UserRepository(
                    AppDatabase.getInstance(requireActivity()).userDao()
                )
            )
        ).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.viewPager.adapter = ProfileTabFragmentAdapter(childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.deleteProfile -> {
            requireContext()
                .alertConfirm("Hesabınız ve tüm mekanlarınız silinecek. Emin misiniz ?") {
                    val shared = AppSharedPreference(requireContext())
                    profileViewModel.delete(shared.getCurrentUserId()){
                        toast("Hesabınız ve tüm mekanlarınız silindi")
                        AppSharedPreference(requireContext()).setIsLogin(false)
                        clearTaskAndRecreateActivity()
                    }
            }
            true
        }
        R.id.logOut -> {
            AppSharedPreference(requireContext()).setIsLogin(false)
            clearTaskAndRecreateActivity()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun clearTaskAndRecreateActivity(){
        val intent = requireActivity().intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}