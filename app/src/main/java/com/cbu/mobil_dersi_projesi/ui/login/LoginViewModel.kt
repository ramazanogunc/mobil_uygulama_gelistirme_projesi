package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.model.User
import com.cbu.mobil_dersi_projesi.data.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun login(email: String, password: String, callBack: (user: User?) -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            val user = userRepository.login(email, password)
            _isLoading.value = false
            callBack(user)
        }
}

class LoginViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}