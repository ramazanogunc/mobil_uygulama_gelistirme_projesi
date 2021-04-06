package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import com.cbu.mobil_dersi_projesi.model.User
import kotlinx.coroutines.launch


class ProfileEditViewModel(private val userRepository: UserRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun getByUserId(userId: Int, callback: (user: User) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val user = userRepository.getByUserId(userId)
            isLoading.value = false
            callback(user)
        }
    }

    fun update(user: User, onComplete: () -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            userRepository.update(user)
            isLoading.value = false
            onComplete()
        }
}

class ProfileEditViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileEditViewModel::class.java)) {
            return ProfileEditViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}