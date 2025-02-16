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

    fun fetchFilteredMovies(genres: Int?, minRating: Float?) {
        viewModelScope.launch {
            println("fetchFilteredMovies çalıştı")
            val movieList = repository.discoverMovies(genres, minRating)
            _movies.value = movieList
            println("Filmler: ${movieList.size}")
        }
    }
    private val _moodMovie = MutableLiveData<List<MovieDetailForDiscover>>()
    val moodMovie: LiveData<List<MovieDetailForDiscover>> = _moodMovie

    private val _moodTvShow = MutableLiveData<List<TvShowDetailForDiscover>>()
    val moodTvShows: LiveData<List<TvShowDetailForDiscover>> = _moodTvShow

    fun fetchByMood(mood: String,selectedTab :Int) {
        val genreId = when (mood) {
            "Mutlu" -> 35   // Komedi
            "Üzgün" -> 18   // Drama
            "Heyecanlı" -> 28  // Aksiyon
            "Romantik" -> 10749  // Romantik
            "Korkmuş" -> 27  // Korku
            "Motivasyona İhtiyacım Var" -> 99  // Belgesel
            else -> 18
        }
        if (selectedTab==0)
        {
            viewModelScope.launch {
                val response = repository.discoverMovies(genreId,null)
                _moodMovie.postValue(listOf(response.shuffled().get(1)))
            }
        }else
        {   viewModelScope.launch {
                val response = repository.discoverTvShows(genreId,null)
                _moodTvShow.postValue(listOf(response.shuffled().get(1)))
            }

        }

    }

    fun fetchFilteredTvShows(genres: Int?, minRating: Float?) {
        viewModelScope.launch {
            val tvShowList = repository.discoverTvShows(genres, minRating)
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
