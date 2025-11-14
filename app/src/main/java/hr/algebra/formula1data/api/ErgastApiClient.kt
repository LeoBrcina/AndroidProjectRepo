package hr.algebra.formula1data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ErgastApiClient {

    private const val BASE_URL = "https://ergast.com/api/f1/"

    val api: ErgastApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ErgastApiService::class.java)
    }
}