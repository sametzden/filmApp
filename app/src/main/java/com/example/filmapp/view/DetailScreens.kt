package com.example.filmapp.view


import android.annotation.SuppressLint
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmapp.R
import com.example.filmapp.data.CastMember
import com.example.filmapp.data.Movie
import com.example.filmapp.data.TVShow
import com.example.filmapp.models.MovieViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(movieId: Int, movieViewModel: MovieViewModel = viewModel(), navController: NavController) {
    val movieDetail by movieViewModel.movieDetail.observeAsState()
    val movieForSave by movieViewModel.movie.observeAsState()
    val cast by movieViewModel.cast
    var isSaved by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }
    LaunchedEffect(movieId) {
        movieViewModel.fetchMovieDetail(movieId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchMovieCredits(movieId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchMovie(movieId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.checkIfItemIsSaved(movieId, "movie") { saved ->
            isSaved = saved
        }
        movieViewModel.checkIfItemIsWatched(movieId,"movie") { watched->
            isWatched = watched

        }
    }

    movieDetail?.let { movie ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Arka plan resmi (bulanıklaştırılmış ve gradyanlı)
            Image(
                painter = rememberImagePainter(
                    data = "https://image.tmdb.org/t/p/original${movie.backdrop_path}",
                ),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.5f // Şeffaflık
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent),
                            startY = 0f,
                            endY = 0.3f
                        )
                    )
            )

            // İçerik (kaydırılabilir)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Poster resmi (gölgeli)
                        Image(
                            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp, shape = RoundedCornerShape(16.dp)), // Gölge
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Başlık ve detaylar (puan rozeti ile)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif // Daha şık bir font
                                ),
                                color = Color.White
                            )
                        }
                        // Puan rozeti
                        Box(
                            modifier = Modifier
                                .background(
                                    if (movie.vote_average!! > 7) Color.Green else if (movie.vote_average!! > 5) Color.Yellow else Color.Red,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp)
                        ) {
                            Text(
                                text = String.format("%.1f", movie.vote_average),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Kaydetme ve İzleme Butonları (ikonlu ve animasyonlu)
                        Row {
                            IconButton(onClick = {   movieForSave?.let { movieViewModel.toggleSaveItem(it, "movie") }
                                isSaved = !isSaved }) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                    contentDescription = if (isSaved) "Kaydedildi" else "Kaydet",
                                    tint = Color.White
                                )
                            }
                            Button(onClick = {  movieForSave?.let { movieViewModel.toggleWatchedItem(it, "movie") }
                                isWatched = !isWatched }) {
                                Text(if (isWatched) "İzledim" else "İzlemedim", color = Color.White)
                            }
                        }

                        // Diğer bilgiler
                        InfoRow("Süre", "${movie.runtime} dakika")
                        InfoRow("Puan", String.format("%.1f", movie.vote_average))
                        InfoRow("Yayın Tarihi", movie.release_date)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Özet (paragraf stili)
                        Text(
                            text = movie.overview,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.LightGray,
                                textAlign = TextAlign.Justify // Paragraf stili
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Türler (çip şeklinde)
                        FlowRow(
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            movie.genres.forEach { genre ->
                                GenreChip(genre.name)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Oyuncular (yatay kaydırma ve kartlar)
                        Text("Oyuncular", fontSize = 20.sp, color = Color.White)
                        LazyRow {
                            items(cast) { actor ->
                                ActorItem(actor, navController) // Oyuncu kartı
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun InfoRow(label: String, value: String) {
    Row {
        Text(label, color = Color.LightGray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(value, color = Color.LightGray)
    }
}

@Composable
fun GenreChip(genre: String) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Text(genre, color = Color.White)
    }
}

@Composable
fun ActorItem(actor: CastMember, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(100.dp) // Sabit genişlik
            .clickable { navController.navigate("actorDetail/${actor.id}") },
        horizontalAlignment = Alignment.CenterHorizontally // Ortala hizala
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${actor.profilePath}"),
            contentDescription = actor.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape), // Yuvarlak resim
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(actor.name, color = Color.LightGray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TVShowDetailScreen(tvShowId: Int, movieViewModel: MovieViewModel = viewModel(), navController: NavController) {
    val tvShowDetail by movieViewModel.tvShowDetail.observeAsState()
    val cast by movieViewModel.showCast
    val showForSave by movieViewModel.show.observeAsState()
    var isSaved by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }

    LaunchedEffect(tvShowId) {
        movieViewModel.fetchTVShowDetails(tvShowId, "6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchShowCredits(tvShowId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.fetchTvShow(tvShowId,"6d8b9e531b047e3bdd803b9979082c51")
        movieViewModel.checkIfItemIsSaved(tvShowId, "tvShow") { saved ->
            isSaved = saved
        }
        movieViewModel.checkIfItemIsWatched(tvShowId,"tvShow") { watched->
            isWatched = watched
        }
    }

    tvShowDetail?.let { tvShow ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Arka plan resmi (bulanıklaştırılmış ve gradyanlı)
            Image(
                painter = rememberImagePainter(
                    data = "https://image.tmdb.org/t/p/original${tvShow.backdrop_path}"
                ),
                contentDescription = tvShow.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.5f // Şeffaflık
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent),
                            startY = 0f,
                            endY = 0.3f
                        )
                    )
            ) {

            }

            // İçerik (kaydırılabilir)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Poster resmi (gölgeli)
                        Image(
                            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
                            contentDescription = tvShow.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp, shape = RoundedCornerShape(16.dp)), // Gölge
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Başlık ve detaylar (puan rozeti ile)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tvShow.name,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif // Daha şık bir font
                                ),
                                color = Color.White,
                                modifier = Modifier.weight(1f) // Başlık ağırlığı
                            )

                            Spacer(modifier = Modifier.width(16.dp))


                        }
                        // Puan rozeti
                        Box(
                            modifier = Modifier
                                .background(
                                    if (tvShow.vote_average!! > 7) Color.Green else if (tvShow.vote_average!! > 5) Color.Yellow else Color.Red,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp)
                        ) {
                            Text(
                                text = String.format("%.1f", tvShow.vote_average),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Kaydetme ve İzleme Butonları (ikonlu ve animasyonlu)
                        Row {
                            IconButton(onClick = {
                                showForSave?.let { movieViewModel.toggleSaveItem(it, "tvShow") }
                                isSaved = !isSaved
                            }) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                    contentDescription = if (isSaved) "Kaydedildi" else "Kaydet",
                                    tint = Color.White
                                )
                            }
                            Button(onClick = {
                                showForSave?.let { movieViewModel.toggleWatchedItem(it, "tvShow") }
                                isWatched = !isWatched
                            }) {
                                Text(if (isWatched) "İzledim" else "İzlemedim", color = Color.White)
                            }
                        }

                        // Diğer bilgiler
                        InfoRow("Sezon Sayısı:", "${tvShow.number_of_seasons}")
                        InfoRow("Puan", String.format("%.1f", tvShow.vote_average))
                        InfoRow("Toplam Bölüm Sayısı:", "${tvShow.number_of_episodes}")
                        InfoRow("Durum", tvShow.status ?: "Bilinmiyor") // Durum bilgisi

                        Spacer(modifier = Modifier.height(16.dp))

                        // Özet (paragraf stili)
                        Text(
                            text = tvShow.overview,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.LightGray,
                                textAlign = TextAlign.Justify // Paragraf stili
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Türler (çip şeklinde)
                        FlowRow(
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            tvShow.genres?.forEach { genre ->
                                GenreChip(genre.name)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Oyuncular (yatay kaydırma ve kartlar)
                        Text("Oyuncular", fontSize = 20.sp, color = Color.White)
                        LazyRow {
                            items(cast) { actor ->
                                ActorItem(actor, navController) // Oyuncu kartı
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp) // Padding eklendi
    ) {
        actorDetails?.let { actorData ->
            Row( // Oyuncunun resmi ve bilgileri yan yana
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${actorData.profilePath}"),
                    contentDescription = actorData.name,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp)) // Boşluk eklendi
                Column {
                    Text(actorData.name, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Doğum Tarihi: ${actorData.birthday ?: "Bilinmiyor"}", color = Color.White)
                    //Yer ve ölüm tarihi eklendi
                    if (actorData.placeOfBirth != null) {
                        Text("Doğum Yeri: ${actorData.placeOfBirth}", color = Color.White)
                    }

                }
            }


            Spacer(Modifier.height(16.dp))

            // Sekmeler (Movies - TV Shows)
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black),
                containerColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color.Cyan // Indicator rengi değiştirildi
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                color = if (selectedTab == index) Color.Cyan else Color.White
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Seçili Sekmeye Göre İçerik
            if (selectedTab == 0) {
                ActorMovieList(actorMovies, navController)
            } else {
                ActorShowList(actorTVShows, navController)
            }
        }
    }
}

@Composable
fun ActorMovieList(actorMovies: List<Movie>, navController: NavController) {
    if (actorMovies.isEmpty()) {
        Text("Bu oyuncunun oynadığı film bulunmamaktadır.", color = Color.White)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(actorMovies) { movie ->
                MovieItem(movie = movie, navController = navController) // navController geçirildi
            }
        }
    }
}

@Composable
fun ActorShowList(actorTvShows: List<TVShow>, navController: NavController) {
    if (actorTvShows.isEmpty()) {
        Text("Bu oyuncunun oynadığı dizi bulunmamaktadır.", color = Color.White)

    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(actorTvShows) { show ->
                TVShowItem(tvShow = show, navController = navController) // navController geçirildi
            }
        }
    }
}
