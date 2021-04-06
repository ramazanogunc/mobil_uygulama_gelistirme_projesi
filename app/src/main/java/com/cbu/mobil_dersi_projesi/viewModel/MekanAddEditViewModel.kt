package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import com.cbu.mobil_dersi_projesi.model.Mekan
import kotlinx.coroutines.launch

class MekanAddEditViewModel(private val mekanRepository: MekanRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()

    fun insert(mekan: Mekan, onComplete: () -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            mekanRepository.insert(mekan)
            isLoading.value = false
            onComplete()
        }

    fun update(mekan: Mekan, onComplete: () -> Unit) =
        viewModelScope.launch {
            isLoading.value = true
            mekanRepository.update(mekan)
            isLoading.value = false
            onComplete()
        }

    fun get(mekanId:Int) = mekanRepository.get(mekanId)
}

class MekanAddEditViewModelFactory(private val mekanRepository: MekanRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MekanAddEditViewModel::class.java)) {
            return MekanAddEditViewModel(mekanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}