package com.example.filmapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.DisposableEffect
import com.example.filmapp.models.MovieCategory
import com.example.filmapp.models.MovieViewModel

@Composable
fun SeeAllMoviesScreen(viewModel: MovieViewModel, category: MovieCategory, navController: NavController) {

    val movies = viewModel._movieList
    DisposableEffect(Unit) {
        onDispose {
            viewModel._movieList.clear() // Kullanıcı geri döndüğünde listeyi temizle
        }
    }
    LaunchedEffect(category) {
        viewModel.selectCategory(category) // Seçili kategoriye göre veri çek
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(movies) { movie ->
                MovieItem(
                    movie = movie,
                    navController = navController,

                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) { // Buton tüm genişliği kaplasın
                Button(
                    onClick = { viewModel.loadMovies() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Daha Fazla Yükle", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun SeeAllTVShowsScreen(viewModel: MovieViewModel, category: MovieCategory, navController: NavController) {
    val shows = viewModel._tvShowList
    DisposableEffect(Unit) {
        onDispose {
            viewModel._tvShowList.clear() // Kullanıcı geri döndüğünde listeyi temizle
        }
    }
    LaunchedEffect(category) {
        viewModel.selectTvCategory(category) // Seçili kategoriye göre veri çek
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(shows) { show ->
                TVShowItem(
                    tvShow = show,
                    navController = navController,
                    )
            }

            item(span = { GridItemSpan(maxLineSpan) }) { // Buton tüm genişliği kaplasın
                Button(
                    onClick = { viewModel.loadMoreTvShows() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Daha Fazla Yükle", color = Color.White)
                }
            }
        }
    }


}