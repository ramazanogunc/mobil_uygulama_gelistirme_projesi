package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.model.Mekan
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import kotlinx.coroutines.launch

class MekanAddEditViewModel(private val mekanRepository: MekanRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun insert(mekan: Mekan, onComplete: () -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            mekanRepository.insert(mekan)
            _isLoading.value = false
            onComplete()
        }

    fun update(mekan: Mekan, onComplete: () -> Unit) =
        viewModelScope.launch {
            _isLoading.value = true
            mekanRepository.update(mekan)
            _isLoading.value = false
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