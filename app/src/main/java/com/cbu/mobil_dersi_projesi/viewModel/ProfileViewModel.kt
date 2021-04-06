package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import kotlinx.coroutines.launch


class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun delete(userId: Int, onComplete: () -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            userRepository.delete(userId)
            isLoading.value = false
            onComplete()
        }
}

class ProfileViewModelFactory (private val userRepository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}