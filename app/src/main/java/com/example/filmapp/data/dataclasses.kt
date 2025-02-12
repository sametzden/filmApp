package com.example.filmapp.data

data class MovieResponse(
    var results: List<Movie>
)
interface MediaItem {
    val id: Int
    val title: String?
    val name : String?
    val poster_path: String
    val popularity: Double
    val vote_average: Double?
    val genres: List<Genre>?
}
data class Movie(
    override val id: Int,
    override val title: String?,
    override val poster_path: String,
    val overview: String,
    val release_date: String, override val popularity: Double, override val name: String?,
    override val genres: List<Genre>?, override val vote_average: Double?

) : MediaItem

data class TVShowResponse(
    var results: List<TVShow>
)

data class TVShow(
    override val id: Int,
    override val name: String?,
    override val poster_path: String,
    val overview: String,
    val first_air_date: String,
    override val popularity: Double, override val title: String?, override val genres: List<Genre>?,
    override val vote_average: Double?,
):MediaItem
data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val backdrop_path: String?,
    val popularity: Double,
    val runtime: Int?,  // Film süresi (dakika cinsinden)
    val vote_average: Double?, // Puan ortalaması
    val vote_count: Int?, // Puan veren kişi sayısı
    val genres: List<Genre>?
)

data class Genre(
    val id: Int,
    val name: String
)
data class TVShowDetail(

    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val first_air_date: String?,
    val last_air_date: String?,
    val vote_average: Double?,
    val popularity: Double,
    val vote_count: Int?,
    val number_of_seasons: Int?,
    val number_of_episodes: Int?,
    val episode_run_time: List<Int>?,  // Dakika cinsinden bölüm süresi
    val genres: List<Genre>?,
    val status: String?
)
