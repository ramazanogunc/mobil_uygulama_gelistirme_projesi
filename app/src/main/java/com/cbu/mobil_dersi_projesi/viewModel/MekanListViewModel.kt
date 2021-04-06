package com.cbu.mobil_dersi_projesi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cbu.mobil_dersi_projesi.data.repository.MekanRepository
import kotlinx.coroutines.launch

class MekanListViewModel(private val mekanRepository: MekanRepository) : ViewModel() {

    fun getAllByUserId(userId: Int) = mekanRepository.getAllByUserId(userId)

    fun delete(mekanId: Int) = viewModelScope.launch {
        mekanRepository.delete(mekanId)
    }
}

class MekanListViewModelFactory(private val mekanRepository: MekanRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MekanListViewModel::class.java)) {
            return MekanListViewModel(mekanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}