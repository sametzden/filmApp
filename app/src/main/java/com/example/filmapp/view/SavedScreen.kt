package com.example.filmapp.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.filmapp.R
import com.example.filmapp.data.Movie
import com.example.filmapp.data.movieForSave
import com.example.filmapp.data.showForSave
import com.example.filmapp.models.MovieViewModel

@Composable
fun SavedScreen(viewModel: MovieViewModel,navController: NavController) {
    viewModel.savedItems()
    val savedMovies by viewModel.savedMovies.observeAsState(emptyList())
    val savedTvShows by viewModel.savedTvShows.observeAsState(emptyList())
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.5f // Şeffaflık
        )
        Column {

            TabRow(
                selectedTabIndex = selectedTab,
                Modifier.background(color = Color.Transparent),
                containerColor = Color.Transparent
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                        Text(title, color = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Seçili Sekmeye Göre İçerik
                    if (selectedTab == 0) {
                        items(savedMovies) { movie ->

                            MovieItemForSave(movie, navController)
                        }
                    } else {
                        items(savedTvShows) { show ->
                            TvShowItemForSave(show, navController)
                        }
                    }

                }
            }
        }

    }
}

@Composable
fun MovieItemForSave(movie: movieForSave, navController: NavController) {
    // Yatay listede her öğe için bir Row oluşturuyoruz
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Genişlik arttırıldı
            .clickable {
                navController.navigate("movieDetail/${movie.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp) // Köşe yuvarlama arttırıldı
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // Yükseklik arttırıldı
        ) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Başlık ve Puan (Resim üzerine entegre)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient( // Gradyan arka plan
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = movie.title ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy( // Başlık boyutu büyütüldü
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TvShowItemForSave(show: showForSave, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Genişlik arttırıldı
            .clickable {
                navController.navigate("tvShowDetail/${show.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp) // Köşe yuvarlama arttırıldı
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // Yükseklik arttırıldı
        ) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${show.poster_path}"),
                contentDescription = show.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Başlık ve Puan (Resim üzerine entegre)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient( // Gradyan arka plan
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = show.name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy( // Başlık boyutu büyütüldü
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }

            }
        }
    }
}