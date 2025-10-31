package com.example.starwarsplanetsassignment.data.network

import com.example.starwarsplanetsassignment.data.model.PlanetsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("planets")
    suspend fun getPlanets(): Response<PlanetsResponse>
}