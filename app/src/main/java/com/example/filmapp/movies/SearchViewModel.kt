package com.example.filmapp.movies

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.data.MediaItem
import com.example.filmapp.data.MovieAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val _searchResults = mutableStateOf<List<MediaItem>>(emptyList())
    val searchResults: State<List<MediaItem>> = _searchResults
    private val apiKey = "6d8b9e531b047e3bdd803b9979082c51"
    fun searchMoviesAndTvShows(query: String) {
        viewModelScope.launch {
            try {
                val movieResponse = MovieAPI.RetrofitClient.api.searchMovies(apiKey,query =  query)
                val tvResponse = MovieAPI.RetrofitClient.api.searchTVShows(apiKey, query = query)
                val allResults: List<MediaItem> = (movieResponse.results + tvResponse.results)
                    .sortedByDescending { it.popularity }  // Popülerliğe göre sırala

                _searchResults.value = allResults.take(5) // İlk 5 sonucu göster
            } catch (e: Exception) {
                println("Hata: $e")
            }
        }
    }
}