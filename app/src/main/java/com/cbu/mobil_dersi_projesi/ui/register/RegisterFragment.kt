package com.cbu.mobil_dersi_projesi.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.cbu.mobil_dersi_projesi.R
import com.cbu.mobil_dersi_projesi.data.local.AppDatabase
import com.cbu.mobil_dersi_projesi.data.model.User
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.databinding.FragmentRegisterAndEditBinding
import com.cbu.mobil_dersi_projesi.helper.LoadingDialog
import com.cbu.mobil_dersi_projesi.helper.toast
import com.cbu.mobil_dersi_projesi.viewModel.RegisterViewModel
import com.cbu.mobil_dersi_projesi.viewModel.RegisterViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterAndEditBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            RegisterViewModelFactory(
                UserRepository(
                    AppDatabase.getInstance(requireActivity()).userDao()
                )
            )
        ).get(RegisterViewModel::class.java)
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
    }

    private fun initUi() {
        binding.btnSave.setOnClickListener { onClickBtnRegister() }
        registerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                loadingDialog.show()
            else
                loadingDialog.hide()
        }
    }

    private fun onClickBtnRegister() {
        if (validateEditTexts()) {
            val nameSurname = binding.nameSurname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            registerViewModel.register(User(nameSurname, email, password)){
                toast("Kayıt oldunuz şimdi giriş yapabilirsiniz.")
                findNavController().navigate(R.id.action_register_to_login)
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