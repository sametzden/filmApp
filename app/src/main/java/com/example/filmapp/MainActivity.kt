package com.example.filmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.filmapp.movies.HomeScreen
import com.example.filmapp.movies.MovieCategory
import com.example.filmapp.movies.MovieDetailScreen


import com.example.filmapp.movies.MovieViewModel

import com.example.filmapp.movies.SeeAllMoviesScreen
import com.example.filmapp.movies.SeeAllTVShowsScreen


import com.example.filmapp.movies.TVShowDetailScreen

import com.example.filmapp.ui.theme.FilmAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmAppTheme {
                App(viewModel = movieViewModel)
            }
                }
            }
        }




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FilmAppTheme {

    }
}

@Composable
fun App(viewModel: MovieViewModel) {
    // NavController oluşturuluyor
    val navController = rememberNavController()

    // NavHost ile yönlendirme yapılacak
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 1
            MovieDetailScreen(movieId = movieId)
        }
        composable("tvShowDetail/{tvShowId}") { backStackEntry ->
            val tvShowId = backStackEntry.arguments?.getString("tvShowId")?.toInt() ?: 1
            TVShowDetailScreen(tvShowId = tvShowId)
        }
        composable("seeAllScreen/{category}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category") ?: "popularMovies"
            val category = when (categoryName) {
                "popularMovies" -> MovieCategory.POPULAR_MOVIES
                "topRatedMovies" -> MovieCategory.TOP_RATED_MOVIES
                "upcomingMovies" -> MovieCategory.UPCOMING_MOVIES
                "nowPlayingMovies" -> MovieCategory.NOW_PLAYING_MOVIES
                else -> MovieCategory.POPULAR_MOVIES
            }
            SeeAllMoviesScreen(viewModel, category, navController)
        }
        composable("seeAllTVShowsScreen/{category}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category") ?: "popularMovies"
            val category = when (categoryName) {
                "popularShows" -> MovieCategory.POPULAR_TVSHOWS
                "topRatedShows" -> MovieCategory.TOP_RATED_TVSHOWS
                "nowPlayingShows" -> MovieCategory.NOW_PLAYING_TVSHOWS
                else -> MovieCategory.POPULAR_MOVIES
            }
            SeeAllTVShowsScreen(viewModel, category, navController)
        }
    }}


