package com.example.starwarsplanetsassignment.di

import com.example.starwarsplanetsassignment.data.network.ApiService
import com.example.starwarsplanetsassignment.data.repositry.PlanetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://swapi.dev/api/") // or BuildConfig.API_BASE_URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePlanetRepository(apiService: ApiService): PlanetRepository =
        PlanetRepository(apiService)
}
