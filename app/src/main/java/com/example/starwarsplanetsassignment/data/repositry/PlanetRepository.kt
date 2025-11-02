package com.example.starwarsplanetsassignment.data.repositry

import com.example.starwarsplanetsassignment.data.model.PlanetsResponse
import com.example.starwarsplanetsassignment.data.network.ApiService
import jakarta.inject.Inject

class PlanetRepository @Inject constructor(private val api: ApiService) {
    suspend fun getPlanetsFromNetworkAPI(): PlanetsResponse? {
        val response = api.getPlanets()
        return if (response.isSuccessful) response.body() else null
    }
}