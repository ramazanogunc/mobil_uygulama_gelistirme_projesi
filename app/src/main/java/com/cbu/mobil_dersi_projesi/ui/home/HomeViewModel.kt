package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository


class HomeViewModel(private val mekanRepository: MekanRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getAll() = mekanRepository.getAll()

    fun get(mekanId: Int) = mekanRepository.get(mekanId)
}

class HomeViewModelFactory (private val mekanRepository: MekanRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mekanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}