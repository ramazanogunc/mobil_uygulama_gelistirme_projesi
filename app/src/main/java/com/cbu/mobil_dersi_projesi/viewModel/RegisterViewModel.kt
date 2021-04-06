package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.model.User
import kotlinx.coroutines.launch


class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun register(user: User, onComplete: () -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            userRepository.register(user)
            isLoading.value = false
            onComplete()
        }
}

class RegisterViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}