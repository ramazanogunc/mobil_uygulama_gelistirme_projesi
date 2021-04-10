package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.model.User
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import kotlinx.coroutines.launch


class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun register(user: User, onComplete: () -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.register(user)
            _isLoading.value = false
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