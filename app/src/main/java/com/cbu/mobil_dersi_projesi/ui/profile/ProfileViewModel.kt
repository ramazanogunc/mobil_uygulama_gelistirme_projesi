package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import kotlinx.coroutines.launch


class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun delete(userId: Int, onComplete: () -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.delete(userId)
            _isLoading.value = false
            onComplete()
        }
}

class ProfileViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}