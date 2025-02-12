package com.example.filmapp.data


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
    /*
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String
    ): TVShowResponse
*/
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Response<MovieDetail>

    @GET("tv/{tvshow_id}")
    suspend fun getTVShowDetails(
        @Path("tvshow_id") tvShowId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Response<TVShowDetail>


    // Filmler
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): MovieResponse

    // Diziler
    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): TVShowResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): TVShowResponse

    @GET("tv/airing_today")
    suspend fun getNowPlayingTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR",
        @Query("page") page: Int
    ): TVShowResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "tr-TR",

    ): MovieResponse

    @GET("search/tv")
    suspend fun searchTVShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "tr-TR",

    ): TVShowResponse
}




