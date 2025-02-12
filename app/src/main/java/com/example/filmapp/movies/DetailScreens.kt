package com.example.filmapp.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.filmapp.R


import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieAPI
import com.example.filmapp.data.TVShow


@Composable
fun MovieDetailScreen(movieId: Int, movieViewModel: MovieViewModel = viewModel()) {
    val movieDetail by movieViewModel.movieDetail.observeAsState()

    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieDetail(movieId, "6d8b9e531b047e3bdd803b9979082c51")
    }

    movieDetail?.let { movie ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(R.color.black))
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.backdrop_path}"),
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Süre: ${movie.runtime} dakika", color = Color.White)
                    Text(
                        text = "Puan: ${movie.vote_average} (${movie.vote_count} oy)",
                        color = Color.White
                    )
                    Text(text = "Yayın Tarihi: ${movie.release_date}", color = Color.White)
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Release Date: ${movie.release_date}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.White
                    )
                    Row {
                        movie.genres?.forEach { genre ->
                            GenreChip(genre.name)
                        }
                    }

                }
            }
        }
    }
}
@Composable
fun GenreChip(genre: String) {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = genre, color = Color.White, fontSize = 14.sp)
    }
}



@Composable
fun TVShowDetailScreen(tvShowId: Int, movieViewModel: MovieViewModel = viewModel()) {
    val tvShowDetail by movieViewModel.tvShowDetail.observeAsState()

    LaunchedEffect(tvShowId) {
        movieViewModel.fetchTVShowDetails(tvShowId, "6d8b9e531b047e3bdd803b9979082c51")
    }

    tvShowDetail?.let { tvShow ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(R.color.black))
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.backdrop_path}"),
                        contentDescription = tvShow.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(8.dp, shape = RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = tvShow.name,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Sezon Sayısı: ${tvShow.number_of_seasons}", color = Color.White)
                    Text(
                        text = "Toplam Bölüm Sayısı: ${tvShow.number_of_episodes}",
                        color = Color.White
                    )
                    Text(
                        text = "Puan: ${tvShow.vote_average} (${tvShow.vote_count} oy)",
                        color = Color.White
                    )
                    Text(text = "Durum: ${tvShow.status}", color = Color.White)
                    Text(
                        text = tvShow.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp), color = Color.White
                    )

                    Text(
                        text = "First Air Date: ${tvShow.first_air_date}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp), color = Color.White
                    )

                    tvShow.genres?.forEach { genre ->
                        GenreChip(genre.name)
                    }

                }
            }
        }
    }
}