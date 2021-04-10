package com.cbu.mobil_dersi_projesi.ui.mekanDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.databinding.FragmentMekanDetailBinding


class MekanDetailFragment : Fragment() {

    private var mekan: Mekan? = null
    private var _binding: FragmentMekanDetailBinding? = null
    private val binding get() = _binding!!


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


    }

}