package com.cbu.mobil_dersi_projesi.ui.profileTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbu.mobil_dersi_projesi.R
import com.cbu.mobil_dersi_projesi.data.AppSharedPreference
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentMekanListBinding
import com.cbu.mobil_dersi_projesi.ui.adapter.MekanRecyclerAdapter
import com.cbu.mobil_dersi_projesi.ui.mekanAddEdit.MekanAddEditFragment
import com.cbu.mobil_dersi_projesi.util.helper.alertConfirm
import com.cbu.mobil_dersi_projesi.util.helper.hide
import com.cbu.mobil_dersi_projesi.util.helper.show
import com.cbu.mobil_dersi_projesi.viewModel.MekanListViewModel
import com.cbu.mobil_dersi_projesi.viewModel.MekanListViewModelFactory

class MekanListFragment : Fragment() {
    private var _binding: FragmentMekanListBinding? = null
    private val binding get() = _binding!!

    private val mekanListViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            MekanListViewModelFactory(
                MekanRepository(
                    AppDatabase.getInstance(requireActivity()).mekanDao()
                )
            )
        ).get(MekanListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMekanListBinding.inflate(inflater, container, false)
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

    var adapter: MekanRecyclerAdapter? = null


    private fun getData() {
        val currentUserId = AppSharedPreference(requireContext()).getCurrentUserId()
        mekanListViewModel.getAllByUserId(currentUserId).observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.error.show()
                binding.recyclerViewMekan.hide()
            } else {
                binding.error.hide()
                binding.recyclerViewMekan.show()
                adapter = MekanRecyclerAdapter(list, ::onItemClick, ::onDeleteClick)
                binding.recyclerViewMekan.adapter = adapter

            }
        }
    }

    private fun onItemClick(mekan: Mekan){
        val bundle = bundleOf("mode" to MekanAddEditFragment.MODE_EDIT)
        bundle.putInt("mekanId", mekan.mekanId)
        findNavController().navigate(R.id.action_profile_to_mekanAddEditFragment, bundle)
    }

    private fun onDeleteClick(mekan: Mekan){
        requireContext().alertConfirm("Bu mekanı silmek istiyor musunuz"){
            mekanListViewModel.delete(mekan.mekanId)
        }
    }

    private fun initUi() {
        binding.fabMekanAdd.setOnClickListener { onFabMekanAddClick() }
        binding.recyclerViewMekan.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onFabMekanAddClick() {
        val bundle = bundleOf("mode" to MekanAddEditFragment.MODE_ADD)
        findNavController().navigate(R.id.action_profile_to_mekanAddEditFragment, bundle)
    }
}