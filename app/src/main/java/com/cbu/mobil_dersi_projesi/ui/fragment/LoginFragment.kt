package com.cbu.mobil_dersi_projesi.ui.fragment

import android.content.Intent
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
import com.cbu.mobil_dersi_projesi.databinding.FragmentLoginBinding
import com.cbu.mobil_dersi_projesi.helper.LoadingDialog
import com.cbu.mobil_dersi_projesi.helper.toast
import com.cbu.mobil_dersi_projesi.model.User
import com.cbu.mobil_dersi_projesi.viewModel.LoginViewModel
import com.cbu.mobil_dersi_projesi.viewModel.LoginViewModelFactory


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            LoginViewModelFactory(
                UserRepository(
                    AppDatabase.getInstance(requireActivity()).userDao()
                )
            )
        ).get(LoginViewModel::class.java)
    }

    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        binding.btnLogin.setOnClickListener { onClickBtnSave() }
        loginViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if (isLoading)
                loadingDialog.show()
            else
                loadingDialog.hide()
        }
    }

    private fun onClickBtnSave() {
        if (validateEditTexts()) {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            loginViewModel.login(email, password) { user ->
                loginCheckAndRedirect(user)
            }
        }
    }

    private fun loginCheckAndRedirect(user: User?) {
        if (user != null) {
            val sharedPreference = AppSharedPreference(requireContext())
            sharedPreference.setIsLogin(true)
            sharedPreference.setCurrentUserId(user.userId)
            // recreate main activity
            val intent = requireActivity().intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            toast("Check email or password")
        }
    }

    private fun validateEditTexts(): Boolean {
        binding.email.error = null
        binding.password.error = null

        if (binding.email.text.isNullOrBlank()) binding.email.error = "required"
        if (binding.password.text.isNullOrBlank()) binding.password.error = "required"

        return binding.email.error == null && binding.password.error == null
    }
}