package com.example.filmapp.data

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val release_date: String
)

data class TVShowResponse(
    val results: List<TVShow>
)

data class TVShow(
    val id: Int,
    val name: String,
    val poster_path: String,
    val overview: String,
    val first_air_date: String
)
data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)
data class TVShowDetail(
    val id: Int,
    val name: String,
    val overview: String,
    val first_air_date: String,
    val poster_path: String,
    val genres: List<Genre>
)
