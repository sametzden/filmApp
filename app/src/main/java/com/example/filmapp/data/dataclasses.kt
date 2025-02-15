package com.example.filmapp.data

import com.google.gson.annotations.SerializedName

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


data class movieForSave(
    val id: Int?,
    val title: String?,
    val poster_path: String?
){
    // Parametresiz constructor Firebase için gereklidir
    constructor() : this(null, null, null)
}

data class showForSave(
    val id: Int?,
    val name: String?,
    val poster_path: String?
){
    // Parametresiz constructor Firebase için gereklidir
    constructor() : this(null, null, null)
}


data class TVShowResponse(
    var results: List<TVShow>
)
data class ActorMoviesResponse(
    val id: Int,
    val cast: List<Movie>
)

data class ActorTVShowsResponse(
    val id: Int,
    val cast: List<TVShow>
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
data class Actor(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    val placeOfBirth: String?,
    @SerializedName("profile_path")val profilePath: String?
)

data class DiscoverMovieResponse(
    val page: Int,
    val results: List<MovieDetailForDiscover>,
    val total_pages: Int,
    val total_results: Int
)

data class DiscoverTvResponse(
    val page: Int,
    val results: List<TvShowDetailForDiscover>,
    val total_pages: Int,
    val total_results: Int
)
data class CreditsResponse(
    val cast: List<CastMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    @SerializedName("profile_path") val profilePath: String,
    val popularity: Double
)
data class Person(
    override val id: Int,
    override val name: String,
    @SerializedName("profile_path") val profilePath: String?,
    override val popularity: Double,
    @SerializedName("known_for") val knownFor: List<KnownFor>,
    override val title: String?,
    override val poster_path: String,
    override val vote_average: Double?,
    override val genres: List<Genre>?
) : MediaItem

data class KnownFor(
    val title: String?,
    @SerializedName("media_type") val mediaType: String
)
data class PeopleResponse(
    val results: List<Person>
)
data class MovieDetailForDiscover(
    val id: Int,
    val title: String,
    val poster_path: String?, // API'den gelen poster_path
    val genre_ids: List<Int>, // API'den gelen genre_ids
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val vote_count: Int
)
data class TvShowDetailForDiscover(
    val id: Int,
    val name: String,
    val poster_path: String?,
    val genre_ids: List<Int>,
    val overview: String,
    val first_air_date: String,
    val vote_average: Double,
    val vote_count: Int
)


