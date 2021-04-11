package com.cbu.mobil_dersi_projesi.ui.mekanDetail

import androidx.lifecycle.*
import com.cbu.mobil_dersi_projesi.data.model.Weather
import com.cbu.mobil_dersi_projesi.data.repository.MekanDetailRepository
import com.cbu.mobil_dersi_projesi.util.Resource
import kotlinx.coroutines.launch

class MekanDetailViewModel(private val mekanDetailRepository: MekanDetailRepository) : ViewModel() {

    private val _weather = MutableLiveData<Resource<List<Weather>>>()
    val weather: LiveData<Resource<List<Weather>>> get() = _weather

    fun getWeather(lang: String, city: String) {
        _weather.postValue(Resource.loading())
        viewModelScope.launch {
            mekanDetailRepository.getWeather(lang, city).let {
                _weather.postValue(Resource.success(it.data?.result.orEmpty()))
            }
        }
    }
}

class MekanDetailViewModelFactory(private val mekanDetailRepository: MekanDetailRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MekanDetailViewModel::class.java)) {
            return MekanDetailViewModel(mekanDetailRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}