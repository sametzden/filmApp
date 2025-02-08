package com.example.filmapp.data


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("tv/popular")
    suspend fun getPopularTVShows(
        @Query("api_key") apiKey: String
    ): TVShowResponse

        @GET("movie/{movie_id}")
        suspend fun getMovieDetails(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String
        ): Response<MovieDetail>

        @GET("tv/{tvshow_id}")
        suspend fun getTVShowDetails(
            @Path("tvshow_id") tvShowId: Int,
            @Query("api_key") apiKey: String
        ): Response<TVShowDetail>
    }


