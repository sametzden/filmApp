package com.example.filmapp.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.TVShow
import kotlin.reflect.typeOf

@Composable
fun HomeScreen(movieViewModel: MovieViewModel = viewModel(),navController: NavController) {

    val popularTVShows by movieViewModel.popularTVShows.observeAsState(emptyList())
    val nowPlayingMovies by movieViewModel.nowPlayingMovies.observeAsState(emptyList())
    val popularMovies by movieViewModel.popularMovies.observeAsState(emptyList())

    // Veriyi çek
    LaunchedEffect(Unit) {
        movieViewModel.fetchPopularMovies()
        movieViewModel.fetchPopularTVShows()
    }
    HomeScreen(navController,movieViewModel)
/*
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
    }*/

}
@Composable
fun HomeScreen(navController: NavController, viewModel: MovieViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Üst Kısım (Başlık + Arama Çubuğu)

        Text("What do you want to watch?", color = Color.White, fontSize = 22.sp, modifier =
        Modifier.padding(top = 30.dp, bottom = 15.dp , start = 60.dp))

        // Sekmeler (Movies - TV Shows)
        TabRow(selectedTabIndex = selectedTab,Modifier.background(color = Color.Black)) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                    Text(title, color =  Color.White )
                }
            }
        }

        // Seçili Sekmeye Göre İçerik
        if (selectedTab == 0) {
            MovieList(navController, viewModel)
        } else {
            TVShowList(navController, viewModel)
        }
    }
}


@Composable
fun MovieList(navController: NavController, viewModel: MovieViewModel) {
    val nowPlaying by viewModel.nowPlayingMovies.observeAsState(emptyList())
    val populerMovies by viewModel.popularMovies1.observeAsState(emptyList())
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(emptyList())
    val upcomingMovies by viewModel.upcomingMovies.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text("Populer Movies", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(populerMovies.take(15)) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                }
            }
            item {
                Text("Upcoming Movies", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(upcomingMovies.take(15)) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                }
            }
            item {
                Text("Top Rated Movies", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(topRatedMovies.take(15)) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                }
            }
            item {
                Text("Now Playing", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(nowPlaying.take(15)) { movie ->
                        MovieItem(movie = movie, navController)
                    }
                }
            }
        }
    }




}
@Composable
fun TVShowList(navController: NavController, viewModel: MovieViewModel) {
    val popularShows by viewModel.popularTVShows1.observeAsState(emptyList())
    val topRatedTVShows by viewModel.topRatedTVShows.observeAsState(emptyList())
    val nowPlayingTVShows by viewModel.nowPlayingTVShows.observeAsState(emptyList())
    val upcomingTVShows by viewModel.upcomingTVShows.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        LazyColumn {
            item {
                Text("Popular TV Shows", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(popularShows.take(15)) { tvShow ->
                        TVShowItem(tvShow = tvShow,navController)
                    }
                }
            }


            item {
                Text("Upcoming Tv Shows", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(upcomingTVShows.take(15)) { tvShow ->
                        TVShowItem(tvShow = tvShow,navController)
                    }
                }
            }

            item {
                Text("Top Rated TV Shows", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(topRatedTVShows.take(15)) { tvShow ->
                        TVShowItem(tvShow = tvShow,navController)
                    }
                }
            }

            item {
                Text("Now Playing TV Shows", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(nowPlayingTVShows.take(15)) { tvShow ->
                        TVShowItem(tvShow = tvShow,navController)
                    }
                }
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
