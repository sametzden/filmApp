package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmapp.R
import com.example.filmapp.models.MovieViewModel

@Composable
fun WatchedScreen(viewModel: MovieViewModel, navController: NavController) {
    viewModel.watchedItems()
    val watchedMovies by viewModel.watchedMovies.observeAsState(emptyList())
    val watchedTVShows by viewModel.watchedTVShows.observeAsState(emptyList())
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Filmler", "Diziler")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF242A32), Color(0xFF1B0505))))
    ) {
        /*
        Image(
            painter = painterResource(id = R.drawable.logo),
            "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.5f // Şeffaflık
        )*/
        Column(
            modifier = Modifier.fillMaxSize().background(Color.Transparent)
        ) { // Arka plan rengini Column'a taşıdık
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent, // Container rengini buraya taşıdık
                indicator = { tabPositions -> // Indicator rengini özelleştirebilirsiniz
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color.White // Örnek: Beyaz bir indicator
                    )
                }

            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, color = Color.White) } // Text rengini burada ayarladık
                    )
                }
            }

            // LazyVerticalGrid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp), // Uyarlanabilir grid
                modifier = Modifier.fillMaxSize().padding(16.dp), // Padding eklendi
                verticalArrangement = Arrangement.spacedBy(16.dp), // Öğeler arası dikey boşluk
                horizontalArrangement = Arrangement.spacedBy(16.dp)  // Öğeler arası yatay boşluk
            ) {
                if (selectedTab == 0) {
                    items(watchedMovies) { movie ->
                        MovieItemForSave(movie, navController)
                    }
                } else {
                    items(watchedTVShows) { show ->
                        TvShowItemForSave(show, navController)
                    }
                }
            }
        }
    }
}
