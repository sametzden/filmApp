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
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Response<movieForSave>
    @GET("tv/{tvshow_id}")
    suspend fun getTvShow(
        @Path("tvshow_id") tvShowId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Response<showForSave>
    @GET("tv/{tvshow_id}")
    suspend fun getTVShowDetails(
        @Path("tvshow_id") tvShowId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Response<TVShowDetail>

    // oyuncuları çek
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): CreditsResponse
    @GET("tv/{tvshow_id}/credits")
    suspend fun getTVShowCredits(
        @Path("tvshow_id") tvShowId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): CreditsResponse
    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "tr-TR"
    ): Actor
    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): ActorMoviesResponse
    @GET("person/{person_id}/tv_credits")
    suspend fun getActorShows(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): ActorTVShowsResponse

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

    // search
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
    @GET("search/person")
    suspend fun searchPeople(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "tr-TR"
    ): PeopleResponse


    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genres: Int?,
       @Query("vote_average.gte") minRating: Float?,
        @Query("sort_by") sortBy: String = "popularity.desc",
       // @Query("primary_release_year") year: Int?,
        @Query("language") language: String = "tr-TR",
    ): DiscoverMovieResponse

    @GET("discover/tv")
    suspend fun discoverTvShows(
        @Query("api_key") apiKey: String,
       @Query("with_genres") genres: Int?,
        @Query("vote_average.gte") minRating: Float?,
        @Query("sort_by") sortBy: String = "popularity.desc",
        //@Query("first_air_date_year") year: Int?,
        @Query("language") language: String = "tr-TR",
    ): DiscoverTvResponse


}




