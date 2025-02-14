package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
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

    Column {

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

        Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
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


@Composable
fun MovieItemForSave(movie: movieForSave, navController: NavController) {
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
        movie.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // Uzun başlıklar kesilecek
                modifier = Modifier.padding(horizontal = 4.dp),
                color = Color.White

            )
        }


    }
}

@Composable
fun TvShowItemForSave(show: showForSave, navController: NavController) {
    // Yatay listede her öğe için bir Row oluşturuyoruz
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                // Tıklanan film detaylarına yönlendiriyoruz
                navController.navigate("tvShowDetail/${show.id}")
            }// Poster genişliğini sabit tutuyoruz
    ) {

        // Filmin posterini gösteriyoruz
        Image(

            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${show.poster_path}"),
            contentDescription = show.name,
            modifier = Modifier
                .height(250.dp)  // Poster yüksekliği
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))  // Yuvarlatılmış köşeler
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))  // Hafif gölge efekti
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filmin adı
        show.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // Uzun başlıklar kesilecek
                modifier = Modifier.padding(horizontal = 4.dp),
                color = Color.White

            )
        }


    }
}