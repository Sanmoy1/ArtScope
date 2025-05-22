package com.example.artinstituteapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object that provides a configured Retrofit instance for the Art Institute API.
 */
object RetrofitClient {
    private const val BASE_URL = "https://api.artic.edu/api/v1/"
    
    /**
     * Creates an OkHttpClient with logging and timeout configurations.
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Lazily initialized Retrofit instance for the Art Institute API.
     */
    val instance: ArtInstituteApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtInstituteApi::class.java)
    }
}
