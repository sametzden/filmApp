package com.example.filmapp.movies

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieAPI
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.TVShow
import com.example.filmapp.data.TVShowDetail
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _popularTVShows = MutableLiveData<List<TVShow>>()
    val popularTVShows: LiveData<List<TVShow>> get() = _popularTVShows

    private val apiKey = "6d8b9e531b047e3bdd803b9979082c51"

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getPopularMovies(apiKey)
                _popularMovies.value = response.results
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies", e)
            }
        }
    }

    fun fetchPopularTVShows() {
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getPopularTVShows(apiKey)
                _popularTVShows.value = response.results
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching TV shows", e)
            }
        }
    }
    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

    fun fetchMovieDetail(movieId: Int, apiKey: String) {
        // API çağrısı burada yapılacak
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getMovieDetails(movieId, apiKey)
                if (response.isSuccessful) {
                    _movieDetail.postValue(response.body())
                } else {
                    // Hata yönetimi
                }
            } catch (e: Exception) {
                // Hata yönetimi
            }
        }
    }
    private val _tvShowDetail = MutableLiveData<TVShowDetail>()
    val tvShowDetail: LiveData<TVShowDetail> = _tvShowDetail

    fun fetchTVShowDetails(tvShowId: Int,apiKey: String) {
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getTVShowDetails(tvShowId,apiKey)
                if (response.isSuccessful) {
                    _tvShowDetail.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
