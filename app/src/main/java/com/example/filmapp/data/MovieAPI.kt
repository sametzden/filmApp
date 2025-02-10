package com.example.filmapp.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieAPI {
    object RetrofitClient {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "6d8b9e531b047e3bdd803b9979082c51"

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $API_KEY")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()

        val api: TMDBApi = retrofit.create(TMDBApi::class.java)
    }

}