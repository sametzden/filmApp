package com.example.filmapp.movies

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
enum class MovieCategory {
    POPULAR_MOVIES, UPCOMING_MOVIES, TOP_RATED_MOVIES,
    POPULAR_TVSHOWS, TOP_RATED_TVSHOWS, UPCOMING_TVSHOWS
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
        fetchUpcomingMovies()

        fetchPopularTVShows()
        fetchTopRatedTVShows()
        fetchNowPlayingTVShows()
        fetchUpcomingTVShows()
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
            val response = MovieAPI.RetrofitClient.api.getUpcomingMovies(apiKey, page = currentPage)
            upcomingMovies.addAll(response.results)


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

     fun fetchUpcomingTVShows() {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getUpcomingTVShows(apiKey, page = currentPage)
            upcomingTVShows.addAll(response.results)

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
    var _movieList = mutableStateListOf<Movie>()
    // 📌 Sonsuz kaydırma ile veri yükleme
    fun loadMovies() {
        viewModelScope.launch {
            try {
                val response = when (_selectedCategory.value) {
                    MovieCategory.POPULAR_MOVIES -> MovieAPI.RetrofitClient.api.getPopularMovies(apiKey, page =  currentPage)
                    MovieCategory.UPCOMING_MOVIES -> MovieAPI.RetrofitClient.api.getUpcomingMovies(apiKey, page =  currentPage)
                    MovieCategory.TOP_RATED_MOVIES -> MovieAPI.RetrofitClient.api.getTopRatedMovies(apiKey, page =  currentPage)
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
    private val _tvShowList = mutableStateListOf<TVShow>()

    fun loadMoreTvShows() {

        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getPopularTVShows(apiKey, page = currentPage)
                _tvShowList.addAll(response.results)  // Yeni filmleri ekle
                currentPage++  // Sayfa numarasını artır
            } catch (e: Exception) {
                println("Hata: ${e.message}")
            } finally {

            }
        }
    }



}









class MovieViewModel2 : ViewModel() {


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