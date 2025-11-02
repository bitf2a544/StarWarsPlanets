package com.example.starwarsplanetsassignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsplanetsassignment.data.model.Planet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.starwarsplanetsassignment.data.repositry.PlanetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PlanetViewModel @Inject constructor(
    private val repository: PlanetRepository
) : ViewModel() {

    private val _planets = MutableStateFlow<List<Planet>>(emptyList())
    val planets: StateFlow<List<Planet>> = _planets

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun fetchPlanets() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getPlanetsFromNetworkAPI()
            if (result != null) {
                _planets.value = result.results
            }
            _loading.value = false
        }
    }
}
