package com.example.filmapp.models

import androidx.compose.runtime.mutableStateListOf
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

    val movies = mutableStateListOf<MovieDetailForDiscover>()


    val tvShows = mutableStateListOf<TvShowDetailForDiscover>()


    fun fetchFilteredMovies(genres: Int?, minRating: Float?) {
        viewModelScope.launch {
            println("fetchFilteredMovies çalıştı")
            for (page in 1..5){
                val movieList = repository.discoverMovies(genres, minRating,page)
                movies.addAll(movieList)
            }
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
                for (page in 1..5){
                    val response = repository.discoverMovies(genreId,null,page)
                   // _movies.value =(listOf(response.shuffled().get(1)))
                    movies.addAll(response)
                }
                val movieformood =movies.shuffled().get(1)
                movies.clear()
                movies.addAll(listOf(movieformood))

            }
        }else
        {
            viewModelScope.launch {
            for (page in 1..5){
                val response = repository.discoverTvShows(genreId,null,page)
                //_tvShows.value = (listOf(response.shuffled().get(1)))
                tvShows.addAll(response)
            }
            val showForMood = tvShows.shuffled().get(1)
            tvShows.clear()
            tvShows.addAll(listOf(showForMood))
            }

        }

    }

    fun fetchFilteredTvShows(genres: Int?, minRating: Float?) {
        viewModelScope.launch {
            for (page in 1..5){
                val tvShowList = repository.discoverTvShows(genres, minRating,page)
                tvShows.addAll(tvShowList)
            }

        }
    }
    fun clearData() {
        movies.clear()
        tvShows.clear()

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
