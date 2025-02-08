package com.example.filmapp.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieAPI
import com.example.filmapp.data.TVShow


@Composable
fun MovieDetailScreen(movieId: Int, movieViewModel: MovieViewModel = viewModel()) {
    val movieDetail by movieViewModel.movieDetail.observeAsState()

    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieDetail(movieId,"6d8b9e531b047e3bdd803b9979082c51")
    }

    movieDetail?.let { movie ->
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
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
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Release Date: ${movie.release_date}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}


@Composable
fun TVShowDetailScreen(tvShowId: Int, movieViewModel: MovieViewModel = viewModel()) {
    val tvShowDetail by movieViewModel.tvShowDetail.observeAsState()

    LaunchedEffect(tvShowId) {
        movieViewModel.fetchTVShowDetails(tvShowId,"6d8b9e531b047e3bdd803b9979082c51")
    }

    tvShowDetail?.let { tvShow ->
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
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
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tvShow.overview,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "First Air Date: ${tvShow.first_air_date}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
