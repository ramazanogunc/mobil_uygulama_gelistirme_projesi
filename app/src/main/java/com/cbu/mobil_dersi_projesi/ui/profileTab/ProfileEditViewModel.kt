package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.model.User
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import kotlinx.coroutines.launch


class ProfileEditViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getByUserId(userId: Int, callback: (user: User) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val user = userRepository.getByUserId(userId)
            _isLoading.value = false
            callback(user)
        }
    }

    fun update(user: User, onComplete: () -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.update(user)
            _isLoading.value = false
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