package com.example.filmapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.filmapp.data.movieForSave
import com.example.filmapp.data.repository.FirestoreRepository
import com.example.filmapp.data.showForSave

import com.example.filmapp.models.BottomNavBar
import com.example.filmapp.view.HomeScreen
import com.example.filmapp.models.MovieCategory
import com.example.filmapp.view.MovieDetailScreen


import com.example.filmapp.models.MovieViewModel
import com.example.filmapp.models.SignInViewModel
import com.example.filmapp.view.ActorDetailScreen

import com.example.filmapp.view.SeeAllMoviesScreen
import com.example.filmapp.view.SeeAllTVShowsScreen


import com.example.filmapp.view.TVShowDetailScreen
import com.example.filmapp.presentation.sign_in.GoogleAuthUiClient
import com.example.filmapp.presentation.sign_in.ProfileScreen
import com.example.filmapp.presentation.sign_in.SignInScreen

import com.example.filmapp.ui.theme.FilmAppTheme
import com.example.filmapp.view.SavedScreen
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel1: MovieViewModel by viewModels()
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println(googleAuthUiClient.getSignedInUser()?.username)
        println("oncreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            FilmAppTheme {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) {innerPadding->
                    // NavHost ile yönlendirme yapılacak
                    NavHost(navController = navController, startDestination = "sign_in", modifier = Modifier.padding(innerPadding)) {
                        composable("sign_in") {
                            val signViewModel = viewModel<SignInViewModel>()
                            val state by signViewModel.state.collectAsStateWithLifecycle()
                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {

                                    navController.navigate("homeScreen")
                                }
                            }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            signViewModel.onSignResult(signInResult)
                                        }
                                    }
                                }
                            )
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Giriş başarılı",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("homeScreen")
                                    signViewModel.resetState()
                                }
                            }
                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()

                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )


                                    }
                                }
                            )
                        }
                        composable(route = "profile") {
                            ProfileScreen(userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()

                                        delay(500)
                                        val currentUser = FirebaseAuth.getInstance().currentUser
                                        if (currentUser == null) {
                                            viewModel1.updateUserId() // Kullanıcı ID'yi sıfırla
                                            Toast.makeText(applicationContext, "Çıkış yapıldı", Toast.LENGTH_LONG).show()
                                            navController.navigate("sign_in") {
                                                popUpTo("profile") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(applicationContext, "Çıkış başarısız!", Toast.LENGTH_LONG).show()
                                        }

                                    }
                                })

                        }
                        composable("homeScreen") {
                            HomeScreen(navController = navController, viewModel = viewModel1)
                        }
                        composable("saved"){

                            SavedScreen(navController = navController , viewModel = viewModel1)
                        }
                        composable("movieDetail/{movieId}") { backStackEntry ->
                            val movieId =
                                backStackEntry.arguments?.getString("movieId")?.toInt() ?: 1
                            MovieDetailScreen(movieId = movieId, navController = navController)
                        }
                        composable("tvShowDetail/{tvShowId}") { backStackEntry ->
                            val tvShowId =
                                backStackEntry.arguments?.getString("tvShowId")?.toInt() ?: 1
                            TVShowDetailScreen(tvShowId = tvShowId, navController = navController)
                        }
                        composable("actorDetail/{actorId}") { backStackEntry ->
                            val actorId =
                                backStackEntry.arguments?.getString("actorId")?.toIntOrNull()
                                    ?: return@composable
                            ActorDetailScreen(actorId, navController)
                        }
                        composable("seeAllScreen/{category}") { backStackEntry ->
                            val categoryName =
                                backStackEntry.arguments?.getString("category") ?: "popularMovies"
                            val category = when (categoryName) {
                                "popularMovies" -> MovieCategory.POPULAR_MOVIES
                                "topRatedMovies" -> MovieCategory.TOP_RATED_MOVIES
                                "upcomingMovies" -> MovieCategory.UPCOMING_MOVIES
                                "nowPlayingMovies" -> MovieCategory.NOW_PLAYING_MOVIES
                                else -> MovieCategory.POPULAR_MOVIES
                            }
                            SeeAllMoviesScreen(viewModel1, category, navController)
                        }
                        composable("seeAllTVShowsScreen/{category}") { backStackEntry ->
                            val categoryName =
                                backStackEntry.arguments?.getString("category") ?: "popularMovies"
                            val category = when (categoryName) {
                                "popularShows" -> MovieCategory.POPULAR_TVSHOWS
                                "topRatedShows" -> MovieCategory.TOP_RATED_TVSHOWS
                                "nowPlayingShows" -> MovieCategory.NOW_PLAYING_TVSHOWS
                                else -> MovieCategory.POPULAR_MOVIES
                            }
                            SeeAllTVShowsScreen(viewModel1, category, navController)
                        }
                    }


                }

            }
        }
    }
}
fun shouldShowBottomBar(navController: NavHostController): Boolean {
    val currentRoute = navController.currentDestination?.route
    return currentRoute in listOf("homeScreen",  "saved", "profile")
}


/*
@Composable
fun App(viewModel1: MovieViewModel) {
    // NavController oluşturuluyor
    val navController = rememberNavController()

    // NavHost ile yönlendirme yapılacak
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("sign_in") {
            val signViewModel = viewModel<SignInViewModel>()
            val state by signViewModel.state.collectAsStateWithLifecycle()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK){
                        life
                    }
                }
            ) { }
        }
        composable("homeScreen") {
            HomeScreen(navController = navController, viewModel = viewModel1)
        }
        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 1
            MovieDetailScreen(movieId = movieId, navController = navController)
        }
        composable("tvShowDetail/{tvShowId}") { backStackEntry ->
            val tvShowId = backStackEntry.arguments?.getString("tvShowId")?.toInt() ?: 1
            TVShowDetailScreen(tvShowId = tvShowId,navController = navController)
        }
        composable("actorDetail/{actorId}") { backStackEntry ->
            val actorId = backStackEntry.arguments?.getString("actorId")?.toIntOrNull() ?: return@composable
            ActorDetailScreen(actorId, navController)
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
            SeeAllMoviesScreen(viewModel1, category, navController)
        }
        composable("seeAllTVShowsScreen/{category}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("category") ?: "popularMovies"
            val category = when (categoryName) {
                "popularShows" -> MovieCategory.POPULAR_TVSHOWS
                "topRatedShows" -> MovieCategory.TOP_RATED_TVSHOWS
                "nowPlayingShows" -> MovieCategory.NOW_PLAYING_TVSHOWS
                else -> MovieCategory.POPULAR_MOVIES
            }
            SeeAllTVShowsScreen(viewModel1, category, navController)
        }
    }}


*/