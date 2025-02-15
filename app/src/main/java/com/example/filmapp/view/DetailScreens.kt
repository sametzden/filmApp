package com.example.filmapp.view


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmapp.R
import com.example.filmapp.data.CastMember
import com.example.filmapp.data.Movie
import com.example.filmapp.data.TVShow
import com.example.filmapp.models.MovieViewModel


@Composable
fun MovieDetailScreen(movieId: Int, movieViewModel: MovieViewModel = viewModel(),navController: NavController) {
    val movieDetail by movieViewModel.movieDetail.observeAsState()
    val movieForSave by movieViewModel.movie.observeAsState()
    val cast by movieViewModel.cast
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieDetail(movieId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchMovieCredits(movieId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchMovie(movieId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.checkIfItemIsSaved(movieId, "movie") { saved ->
            isSaved = saved
        }
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
                            .alpha(0.5f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = Color.White
                    )
                    // Kaydetme/Çıkarma butonu
                    Button(onClick = {
                        movieForSave?.let { movieViewModel.toggleSaveItem(it, "movie") }
                        isSaved = !isSaved
                    }) {
                        Text(if (isSaved) "Kaydedildi" else "Kaydet")
                    }
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
                        movie.genres.forEach { genre ->
                            GenreChip(genre.name)
                        }
                    }
                    Column {
                        Text("Oyuncular", fontSize = 22.sp, color = Color.White)
                        LazyRow {
                            items(cast) { actor ->
                                println(actor.profilePath)
                                ActorItem(actor, navController)
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun ActorItem(actor: CastMember, navController: NavController) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { navController.navigate("actorDetail/${actor.id}") }
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${actor.profilePath}"),
            contentDescription = actor.name,
            modifier = Modifier.size(80.dp)
        )
        Text(actor.name, color = Color.White, fontSize = 14.sp)
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
fun TVShowDetailScreen(tvShowId: Int, movieViewModel: MovieViewModel = viewModel(),navController: NavController) {
    val tvShowDetail by movieViewModel.tvShowDetail.observeAsState()
    val cast by movieViewModel.showCast
    val showForSave by movieViewModel.show.observeAsState()
    var isSaved by remember { mutableStateOf(false) }
    LaunchedEffect(tvShowId) {
        movieViewModel.fetchTVShowDetails(tvShowId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchShowCredits(tvShowId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchTvShow(tvShowId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.checkIfItemIsSaved(tvShowId, "tvShow") { saved ->
            isSaved = saved
        }
    }

    tvShowDetail?.let { tvShow ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original" + tvShow.backdrop_path,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.3f) // Hafif karartma efekti

                )

                Column(
                    modifier = Modifier
                        .background(color = colorResource(R.color.black))
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = tvShow.name,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    // Kaydetme/Çıkarma butonu
                    Button(onClick = {
                        showForSave?.let { movieViewModel.toggleSaveItem(it, "tvShow") }
                        isSaved = !isSaved
                    }) {
                        Text(if (isSaved) "Kaydedildi" else "Kaydet")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Sezon Sayısı: ${tvShow.number_of_seasons}", color = Color.White)
                    Text(
                        text = "Toplam Bölüm Sayısı: ${tvShow.number_of_episodes}",
                        color = Color.White
                    )
                    Text(
                        text = ": ${tvShow.vote_average} (${tvShow.vote_count} oy)",
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
                    Row {
                        tvShow.genres?.forEach { genre ->
                            GenreChip(genre.name)
                        }
                    }

                    Column {
                        Text("Oyuncular", fontSize = 22.sp, color = Color.White)
                        LazyRow {
                            items(cast) { actor ->
                                ActorItem(actor, navController)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ActorDetailScreen(actorId: Int, navController: NavController) {
    val viewModel: MovieViewModel = viewModel()
    val actorDetails by viewModel.actorDetails
    val actorMovies by viewModel.actorMovies
    val actorTVShows by viewModel.actorTVShows
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")
    LaunchedEffect(actorId) {
        viewModel.fetchActorDetails(actorId)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)) {
        actorDetails?.let { actorData ->
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${actorData.profilePath}"),
                contentDescription = actorData.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Text(actorData.name, fontSize = 22.sp, color = Color.White)
            Text("Doğum Tarihi: ${actorData.birthday ?: "Bilinmiyor"}", color = Color.White)


            Spacer(Modifier.height(16.dp))
            // Sekmeler (Movies - TV Shows)
            TabRow(
                selectedTabIndex = selectedTab,
                Modifier.background(color = Color.Black),
                containerColor = Color.Black
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                        Text(title, color = Color.White)
                    }
                }
            }

            // Seçili Sekmeye Göre İçerik
            if (selectedTab == 0) {
                ActorMovieList(actorMovies,navController)
            } else {
                ActorShowList(actorTVShows, navController)
            }

        }
    }
}
@Composable
fun ActorMovieList(actorMovies : List<Movie>,navController: NavController){
    Text("Oynadığı Filmler", fontSize = 20.sp, color = Color.White)
    actorMovies.forEach(){
            movie->
        println(movie.name)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(actorMovies) { movie ->
                println(movie.name)
                MovieItem(
                    movie = movie,
                    navController = navController,
                )
            }
        }
    }
}


@Composable
fun ActorShowList(actorTvShows : List<TVShow>,navController: NavController){
    Text("Oynadığı Diziler", fontSize = 20.sp, color = Color.White)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(actorTvShows) { show ->

                TVShowItem(
                    tvShow = show,
                    navController = navController,
                )
            }
        }
    }
}
