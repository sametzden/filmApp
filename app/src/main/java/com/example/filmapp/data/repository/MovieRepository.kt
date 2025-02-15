package com.example.filmapp.data.repository

import com.example.filmapp.data.MovieAPI

import com.example.filmapp.data.MovieDetailForDiscover

import com.example.filmapp.data.TvShowDetailForDiscover
import com.google.firebase.auth.FirebaseAuth

class MovieRepository {
    private val API_KEY = "6d8b9e531b047e3bdd803b9979082c51"

    suspend fun discoverMovies(genres: Int?, minRating: Float?, year: Int?): List<MovieDetailForDiscover> {
        return try {
            val response = MovieAPI.RetrofitClient.api.discoverMovies(
                apiKey = API_KEY,
                genres = genres,
                minRating = minRating,
                year = year

            )
            println("API Yanıtı: $response")
            response.results // API'den dönen yanıtı doğrudan döndür
        } catch (e: Exception) {
            println("Hata: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun discoverTvShows(genres: Int?, minRating: Float?, year: Int?): List<TvShowDetailForDiscover> {
        return try {
            val response = MovieAPI.RetrofitClient.api.discoverTvShows(
                apiKey = API_KEY,
                genres = genres,
                minRating = minRating,
                year = year
            )
            response.results // API'den dönen yanıtı doğrudan döndür
        } catch (e: Exception) {
            println("Hata: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}