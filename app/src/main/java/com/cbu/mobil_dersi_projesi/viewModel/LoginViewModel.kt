package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.model.User
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun login(email: String, password: String, callBack: (user: User?) -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            val user = userRepository.login(email, password)
            isLoading.value = false
            callBack(user)
        }
}

class LoginViewModelFactory (private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}