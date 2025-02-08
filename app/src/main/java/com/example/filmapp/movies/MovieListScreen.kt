package com.example.filmapp.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.filmapp.data.Movie
import com.example.filmapp.data.TVShow

@Composable
fun HomeScreen(movieViewModel: MovieViewModel = viewModel(),navController: NavController) {
    val popularMovies by movieViewModel.popularMovies.observeAsState(emptyList())
    val popularTVShows by movieViewModel.popularTVShows.observeAsState(emptyList())

    // Veriyi çek
    LaunchedEffect(Unit) {
        movieViewModel.fetchPopularMovies()
        movieViewModel.fetchPopularTVShows()
    }

    // Arayüz
    Column(modifier = Modifier.padding(16.dp)) {
        // Popüler Filmler
        Text(text = "Popüler Filmler", style = MaterialTheme.typography.displaySmall)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(popularMovies.take(10)) { movie ->
                MovieItem(movie = movie,navController)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Popüler Diziler
        Text(text = "Popüler Diziler", style = MaterialTheme.typography.displaySmall)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(popularTVShows.take(10)) { tvShow ->
                TVShowItem(tvShow = tvShow, navController = navController)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie,navController: NavController) {
    // Yatay listede her öğe için bir Row oluşturuyoruz
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                // Tıklanan film detaylarına yönlendiriyoruz
                navController.navigate("movieDetail/${movie.id}")
            }// Poster genişliğini sabit tutuyoruz
    ) {
        // Filmin posterini gösteriyoruz
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
            contentDescription = movie.title,
            modifier = Modifier
                .height(250.dp)  // Poster yüksekliği
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))  // Yuvarlatılmış köşeler
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))  // Hafif gölge efekti
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filmin adı
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis, // Uzun başlıklar kesilecek
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun TVShowItem(tvShow: TVShow,navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                navController.navigate("tvShowDetail/${tvShow.id}")
            }  // Poster genişliğini sabit tutuyoruz
    ) {
        // Dizinin posterini gösteriyoruz
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
            contentDescription = tvShow.name,
            modifier = Modifier
                .height(250.dp)  // Poster yüksekliği
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))  // Yuvarlatılmış köşeler
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))  // Hafif gölge efekti
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dizinin adı
        Text(
            text = tvShow.name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis, // Uzun başlıklar kesilecek
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
