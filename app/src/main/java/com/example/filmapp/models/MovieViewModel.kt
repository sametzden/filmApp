package com.example.filmapp.models

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.data.Actor
import com.example.filmapp.data.CastMember
import com.example.filmapp.data.MediaItem
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieAPI
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.TVShow

import com.example.filmapp.data.TVShowDetail
import com.example.filmapp.data.movieForSave
import com.example.filmapp.data.repository.FirestoreRepository
import com.example.filmapp.data.showForSave
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class MovieCategory {
    POPULAR_MOVIES, UPCOMING_MOVIES, TOP_RATED_MOVIES,NOW_PLAYING_MOVIES,
    POPULAR_TVSHOWS, TOP_RATED_TVSHOWS,NOW_PLAYING_TVSHOWS
}

class MovieViewModel: ViewModel() {
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _popularTVShows = MutableLiveData<List<TVShow>>()
    val popularTVShows: LiveData<List<TVShow>> get() = _popularTVShows

    private val apiKey = "6d8b9e531b047e3bdd803b9979082c51"

    private var _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> = _userId
    private val auth = Firebase.auth

    private val authStateListener = FirebaseAuth.AuthStateListener {
        _userId.value = it.currentUser?.uid
    }
    private var currentPage = 1
    var nowPlayingMovies = mutableStateListOf<Movie>()
    var popularMovies1 =mutableStateListOf<Movie>()
    var topRatedMovies =mutableStateListOf<Movie>()
    var upcomingMovies =mutableStateListOf<Movie>()
    private val repository = FirestoreRepository()
    var popularTVShows1= mutableStateListOf<TVShow>()
    var topRatedTVShows = mutableStateListOf<TVShow>()
    var nowPlayingTVShows = mutableStateListOf<TVShow>()

    private val _savedMovies = MutableLiveData<List<movieForSave>>()
    private val _savedTvShows = MutableLiveData<List<showForSave>>()
    private val _watchedMovies = MutableLiveData<List<movieForSave>>()
    init {
        // Firebase'den güncel kullanıcıyı al
        _userId.value = FirebaseAuth.getInstance().currentUser?.uid
        auth.addAuthStateListener(authStateListener)
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
    fun savedItems(){

        repository.getSavedMovies{ movie ->
            println("saved movies calıstı")
            _savedMovies.postValue(movie)
        }
        // Kaydedilen TV şovlarını dinle
        repository.getSavedTvShows { tvShows ->
            _savedTvShows.postValue(tvShows)
        }
    }
    fun watchedItems(){
        repository.getWatchedMovies { movie->
            _watchedMovies.postValue(movie)
        }
    }
    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }
    fun updateUserId() {
        _userId.value = FirebaseAuth.getInstance().currentUser?.uid
        println("updateUserId çağrıldı, yeni userId: ${_userId.value}")
    }
    // Kaydedilen filmler
    val savedMovies: LiveData<List<movieForSave>> get() = _savedMovies
    val watchedMovies: LiveData<List<movieForSave>> get() = _watchedMovies
    // Kaydedilen TV şovları
    val savedTvShows: LiveData<List<showForSave>> get() = _savedTvShows
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

    private val _movieVideoKey = MutableLiveData<String?>()
    val movieVideoKey: LiveData<String?> = _movieVideoKey
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
    fun fetchMovieVideos(movieId: Int,apiKey: String) {
        viewModelScope.launch {
            val response = MovieAPI.RetrofitClient.api.getMovieVideos(movieId, apiKey)
            if (response.isSuccessful) {
                val videos = response.body()?.results ?: emptyList()
                val trailer = videos.find { it.site == "YouTube" && it.type == "Trailer" }
                _movieVideoKey.postValue(trailer?.key) // Eğer fragman varsa key’i al
            }
        }
    }
    private val _movieForSave = MutableLiveData<movieForSave>()
    val movie: LiveData<movieForSave> = _movieForSave

    fun fetchMovie(movieId: Int, apiKey: String) {
        // API çağrısı burada yapılacak
        viewModelScope.launch {

            val response = MovieAPI.RetrofitClient.api.getMovie(movieId, apiKey)
            if (response.isSuccessful) {
                _movieForSave.postValue(response.body())

            } else {
                // Hata yönetimi
            }

        }
    }
    private val _showForSave = MutableLiveData<showForSave>()
    val show: LiveData<showForSave> = _showForSave

    fun fetchTvShow(showId: Int, apiKey: String) {
        // API çağrısı burada yapılacak
        viewModelScope.launch {

            val response = MovieAPI.RetrofitClient.api.getTvShow(showId, apiKey)
            if (response.isSuccessful) {
                _showForSave.postValue(response.body())

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


    private val _cast = mutableStateOf<List<CastMember>>(emptyList())
    val cast: State<List<CastMember>> = _cast

    fun fetchMovieCredits(movieId: Int,apiKey: String) {
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getMovieCredits(movieId, apiKey)
                _cast.value = response.cast.sortedByDescending { it.popularity }

            } catch (e: Exception) {
                Log.e("MovieDetailVM", "Error fetching cast", e)
            }
        }
    }

    private val _showCast = mutableStateOf<List<CastMember>>(emptyList())
    val showCast: State<List<CastMember>> = _showCast

    fun fetchShowCredits(showId: Int,apiKey: String) {
        viewModelScope.launch {
            try {
                val response = MovieAPI.RetrofitClient.api.getTVShowCredits(showId, apiKey)
                _showCast.value = response.cast.sortedByDescending { it.popularity }

            } catch (e: Exception) {
                Log.e("MovieDetailVM", "Error fetching cast", e)
            }
        }
    }




    private val _actorDetails = mutableStateOf<Actor?>(null)
    val actorDetails: State<Actor?> = _actorDetails

    private val _actorMovies = mutableStateOf<List<Movie>>(emptyList())
    val actorMovies: State<List<Movie>> = _actorMovies

    private val _actorTVShows = mutableStateOf<List<TVShow>>(emptyList())
    val actorTVShows: State<List<TVShow>> = _actorTVShows
    fun fetchActorDetails(actorId: Int) {
        viewModelScope.launch {
            try {
                _actorDetails.value = MovieAPI.RetrofitClient.api.getActorDetails(actorId, apiKey)
                _actorMovies.value  = MovieAPI.RetrofitClient.api.getActorMovies(actorId, apiKey).cast.sortedByDescending {
                    it.popularity
                }
                _actorTVShows.value = MovieAPI.RetrofitClient.api.getActorShows(actorId, apiKey).cast.sortedByDescending {
                    it.popularity
                }
            } catch (e: Exception) {
                Log.e("ActorDetailVM", "Error fetching actor details", e)
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
                    MovieCategory.TOP_RATED_TVSHOWS -> MovieAPI.RetrofitClient.api.getTopRatedTVShows(apiKey, page =  currentPage)
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

    fun toggleWatchedItem(item: Any, type: String) {
        println("fonksiyona girdi")
        when (type) {
            "movie" -> {
                if (item is movieForSave) {
                    println("item is movie")
                    item.id?.let {
                        checkIfItemIsWatched(it, type) { isWatched ->
                            println("kayıt kontrol")
                            if (isWatched) {
                                println("kayıtlı")
                                repository.removeWatchedMovie(item.id)
                            } else {
                                println("kayıt olmalı")
                                repository.saveWatchedMovie(item)
                            }
                        }
                    }
                }
            }
        }
    }
    fun checkIfItemIsWatched(itemId: Int, type: String, callback: (Boolean) -> Unit) {
        val collection = when (type) {
            "movie" -> "watchedMovies"
            "tvShow" -> "tvShows"
            else -> throw IllegalArgumentException("Invalid type")
        }
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        FirebaseFirestore.getInstance()
            .collection("users").document(currentUserId)
            .collection(collection)
            .document(itemId.toString())
            .get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener { e ->
                println("item yok")
                Log.w("Firestore", "Error checking item", e)
                callback(false)
            }
    }

    // Film veya TV şovunun kayıtlı olup olmadığını kontrol etme
    fun checkIfItemIsSaved(itemId: Int, type: String, callback: (Boolean) -> Unit) {
        val collection = when (type) {
            "movie" -> "movies"
            "tvShow" -> "tvShows"
            else -> throw IllegalArgumentException("Invalid type")
        }
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        FirebaseFirestore.getInstance()
            .collection("users").document(currentUserId)
            .collection(collection)
            .document(itemId.toString())
            .get()
            .addOnSuccessListener { document ->
                callback(document.exists())
            }
            .addOnFailureListener { e ->
                println("item yok")
                Log.w("Firestore", "Error checking item", e)
                callback(false)
            }
    }

    // Film veya TV şovunu kaydetme veya çıkarma
    fun toggleSaveItem(item: Any, type: String) {
        println("fonksiyona girdi")
        when (type) {
            "movie" -> {
                if (item is movieForSave) {
                    println("item is movie")
                    item.id?.let {
                        checkIfItemIsSaved(it, type) { isSaved ->
                            println("kayıt kontrol")
                            if (isSaved) {
                                println("kayıtlı")
                                repository.removeMovie( item.id)
                            } else {
                                println("kayıt olmalı")
                                repository.saveMovie(item)
                            }
                        }
                    }
                }
            }
            "tvShow" -> {
                if (item is showForSave) {
                    item.id?.let {
                        checkIfItemIsSaved(it, type) { isSaved ->
                            if (isSaved) {
                                repository.removeTvShow( item.id)
                            } else {
                                repository.saveTvShow( item)
                            }
                        }
                    }
                }
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