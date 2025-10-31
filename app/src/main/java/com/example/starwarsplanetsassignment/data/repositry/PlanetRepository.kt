package com.example.starwarsplanetsassignment.data.repositry

import com.example.starwarsplanetsassignment.data.model.PlanetsResponse
import com.example.starwarsplanetsassignment.data.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlanetRepository {

    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    suspend fun getPlanetsFromNetworkAPI(): PlanetsResponse? {
        val response = api.getPlanets()
        return if (response.isSuccessful)
            response.body()
        else
            null
    }

}