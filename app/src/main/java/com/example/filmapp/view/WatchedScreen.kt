package com.example.filmapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmapp.models.MovieViewModel

@Composable
fun WatchedScreen(viewModel: MovieViewModel, navController: NavController) {
    viewModel.watchedItems()
    val watchedMovies by viewModel.watchedMovies.observeAsState(emptyList())
    val watchedTVShows by viewModel.watchedTVShows.observeAsState(emptyList())
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
                    items(watchedMovies) { movie ->
                        MovieItemForSave(movie, navController)
                    }
                }else{
                    items(watchedTVShows) { show ->
                        TvShowItemForSave(show, navController)
                    }
                }

            }

        }


    }
}

