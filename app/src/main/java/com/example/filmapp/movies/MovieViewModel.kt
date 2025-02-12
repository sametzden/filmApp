package com.example.filmapp.movies

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.data.MediaItem
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieAPI
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.TVShow
import com.example.filmapp.data.TVShowDetail
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class MovieCategory {
    POPULAR_MOVIES, UPCOMING_MOVIES, TOP_RATED_MOVIES,NOW_PLAYING_MOVIES,
    POPULAR_TVSHOWS, TOP_RATED_TVSHOWS, UPCOMING_TVSHOWS,NOW_PLAYING_TVSHOWS
}

class MovieViewModel : ViewModel() {
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _popularTVShows = MutableLiveData<List<TVShow>>()
    val popularTVShows: LiveData<List<TVShow>> get() = _popularTVShows

    private val apiKey = "6d8b9e531b047e3bdd803b9979082c51"

    private var currentPage = 1
    var nowPlayingMovies = mutableStateListOf<Movie>()
    var popularMovies1 =mutableStateListOf<Movie>()
    var topRatedMovies =mutableStateListOf<Movie>()
    var upcomingMovies =mutableStateListOf<Movie>()

    var popularTVShows1= mutableStateListOf<TVShow>()
    var topRatedTVShows = mutableStateListOf<TVShow>()
    var nowPlayingTVShows = mutableStateListOf<TVShow>()
    var upcomingTVShows= mutableStateListOf<TVShow>()
    init {
        fetchNowPlayingMovies()
        fetchPopularMovies()
        fetchTopRatedMovies()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fetchUpcomingMovies()

        }
        fetchPopularTVShows()
        fetchTopRatedTVShows()
        fetchNowPlayingTVShows()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun filterMoviesByReleaseDate(movies: List<Movie>): List<Movie> {
        val today = LocalDate.now()  // Bugünün tarihi
        return movies.filter { movie ->
            try {
                val releaseDate = LocalDate.parse(movie.release_date)  // Yayın tarihini al
                releaseDate.isAfter(today.minusDays(1))  // Bugünden önceyse gösterme
            } catch (e: Exception) {
                false  // Hatalı tarih formatı varsa direkt ele
            }
        }
    }
    fun fetchNowPlayingMovies() {
        viewModelScope.launch {

            val response = MovieAPI.RetrofitClient.api.getNowPlayingMovies(apiKey, page = currentPage)
            nowPlayingMovies.addAll(response.results)
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getPopularMovies(apiKey, page = currentPage)
            popularMovies1.addAll(response.results)

        }
    }

     fun fetchTopRatedMovies() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getTopRatedMovies(apiKey, page = currentPage)
                topRatedMovies.addAll(response.results)

        }
    }


     fun fetchUpcomingMovies() {
        viewModelScope.launch {
            for (page in 1..5){
                val response = MovieAPI.RetrofitClient.api.getUpcomingMovies(apiKey, page = page)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val filteredMovies = filterMoviesByReleaseDate(response.results)
                    upcomingMovies.addAll(filteredMovies)
                }else{
                    upcomingMovies.addAll(response.results)
                }
            }
        }
    }

    internal fun fetchPopularTVShows() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getPopularTVShows(apiKey, page = currentPage)
            popularTVShows1.addAll(response.results)

        }
    }

     fun fetchTopRatedTVShows() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getTopRatedTVShows(apiKey, page = currentPage)
            topRatedTVShows.addAll(response.results)

        }
    }

     fun fetchNowPlayingTVShows() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getNowPlayingTVShows(apiKey, page = currentPage)
            nowPlayingTVShows.addAll(response.results)
        }
    }



    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

    fun fetchMovieDetail(movieId: Int, apiKey: String) {
        // API çağrısı burada yapılacak
        viewModelScope.launch {

            val response = MovieAPI.RetrofitClient.api.getMovieDetails(movieId, apiKey)
            if (response.isSuccessful) {
                _movieDetail.postValue(response.body())
            } else {
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


    private var _selectedCategory = MutableLiveData<MovieCategory?>(null)
    val selectedCategory: LiveData<MovieCategory?> = _selectedCategory


    // 📌 Kullanıcı "See All" ekranında kategori seçince burası çalışacak
    fun selectCategory(category: MovieCategory) {
        _selectedCategory.value = category
        currentPage = 1

        loadMovies()
    }
    fun selectTvCategory(category: MovieCategory) {
        _selectedCategory.value = category
        currentPage = 1

        loadMoreTvShows()
    }

    var _movieList = mutableStateListOf<Movie>()

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val response = when (_selectedCategory.value) {
                    MovieCategory.POPULAR_MOVIES -> MovieAPI.RetrofitClient.api.getPopularMovies(apiKey, page =  currentPage)
                    MovieCategory.TOP_RATED_MOVIES -> MovieAPI.RetrofitClient.api.getTopRatedMovies(apiKey, page =  currentPage)
                    MovieCategory.NOW_PLAYING_MOVIES -> MovieAPI.RetrofitClient.api.getNowPlayingMovies(apiKey, page = currentPage)
                    else -> null
                }
                response?.let {
                    val currentMovies = _movieList
                    _movieList.addAll( it.results)
                    currentPage++
                }


            } catch (e: Exception) {
                println("Hata: ${e.message}")
            } finally {

            }
        }
    }
    var _tvShowList = mutableStateListOf<TVShow>()

    fun loadMoreTvShows() {

        viewModelScope.launch {
            try {
                val response = when (_selectedCategory.value) {
                    MovieCategory.POPULAR_TVSHOWS -> MovieAPI.RetrofitClient.api.getPopularTVShows(apiKey, page =  currentPage)
                    MovieCategory.TOP_RATED_TVSHOWS-> MovieAPI.RetrofitClient.api.getTopRatedTVShows(apiKey, page =  currentPage)
                    MovieCategory.NOW_PLAYING_TVSHOWS -> MovieAPI.RetrofitClient.api.getNowPlayingTVShows(apiKey, page = currentPage)
                    else -> null
                }

                response?.let {
                    val currentTVShow = _tvShowList
                    _tvShowList.addAll( it.results)
                    currentPage++
                }
            } catch (e: Exception) {
                println("Hata: ${e.message}")
            } finally {

            }
        }
    }


    private val _searchResults = mutableStateOf<List<MediaItem>>(emptyList())
    val searchResults: State<List<MediaItem>> = _searchResults

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun searchMovies(query: String) {
        _searchQuery.value = query

        viewModelScope.launch {
            try {
                if (query.isNotEmpty()) {
                    val movieResponse = MovieAPI.RetrofitClient.api.searchMovies(apiKey = apiKey, query = query)
                    val tvResponse = MovieAPI.RetrofitClient.api.searchTVShows(apiKey = apiKey, query = query)
                    val allResults = (movieResponse.results+tvResponse.results).sortedByDescending { it.popularity }
                   _searchResults.value = allResults.take(5) // İlk 5 sonucu al
                } else {
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                println("Hata: ${e.message}")
            }
        }
    }




}

















/*fun fetchPopularMovies() {
/*
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
*/