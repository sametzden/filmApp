package com.example.filmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.filmapp.movies.HomeScreen
import com.example.filmapp.movies.MovieDetailScreen

import com.example.filmapp.movies.MovieViewModel
import com.example.filmapp.movies.TVShowDetailScreen

import com.example.filmapp.ui.theme.FilmAppTheme

class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmAppTheme {
                // NavController'ı oluşturuyoruz
                val navController = rememberNavController()

                // App'ı çağırıyoruz ve NavController'ı kullanıyoruz
                App()
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
fun App() {
    // NavController oluşturuluyor
    val navController = rememberNavController()

    // NavHost ile yönlendirme yapılacak
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(navController = navController)
        }
        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 1
            MovieDetailScreen(movieId = movieId)
        }
        composable("tvShowDetail/{tvShowId}") { backStackEntry ->
            val tvShowId = backStackEntry.arguments?.getString("tvShowId")?.toInt() ?: 1
            TVShowDetailScreen(tvShowId = tvShowId)
        }
    }
}

