package com.example.filmapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.filmapp.data.MovieDetailForDiscover

import com.example.filmapp.data.TvShowDetailForDiscover
import com.example.filmapp.data.repository.MovieRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDetailForDiscover>>()
    val movies: LiveData<List<MovieDetailForDiscover>> = _movies

    private val _tvShows = MutableLiveData<List<TvShowDetailForDiscover>>()
    val tvShows: LiveData<List<TvShowDetailForDiscover>> = _tvShows

    fun fetchFilteredMovies(genres: Int?, minRating: Float?, year: Int?) {
        viewModelScope.launch {
            println("fetchFilteredMovies çalıştı")
            val movieList = repository.discoverMovies(genres, minRating, year)
            _movies.value = movieList
            println("Filmler: ${movieList.size}")
        }
    }

    fun fetchFilteredTvShows(genres: Int?, minRating: Float?, year: Int?) {
        viewModelScope.launch {
            val tvShowList = repository.discoverTvShows(genres, minRating, year)
            _tvShows.value = tvShowList
        }
    }
}
class DiscoverViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiscoverViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
