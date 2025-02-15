package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Slider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.filmapp.data.Movie
import com.example.filmapp.data.MovieDetail
import com.example.filmapp.data.MovieDetailForDiscover
import com.example.filmapp.data.TVShow
import com.example.filmapp.data.TVShowDetail
import com.example.filmapp.data.TvShowDetailForDiscover
import com.example.filmapp.data.repository.MovieRepository
import com.example.filmapp.models.DiscoverViewModel

val movieGenres = mapOf(
    "Action" to 28,
    "Adventure" to 12,
    "Animation" to 16,
    "Comedy" to 35,
    "Crime" to 80,
    "Documentary" to 99,
    "Drama" to 18,
    "Family" to 10751,
    "Fantasy" to 14,
    "History" to 36,
    "Horror" to 27,
    "Music" to 10402,
    "Mystery" to 9648,
    "Romance" to 10749,
    "Science Fiction" to 878,
    "TV Movie" to 10770,
    "Thriller" to 53,
    "War" to 10752,
    "Western" to 37
)

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = viewModel(), navController: NavController) {
    val movies by viewModel.movies.observeAsState()
    val tvShows by viewModel.tvShows.observeAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var selectedGenre by remember { mutableStateOf(1) }
    var minRating by remember { mutableStateOf(0f) }
    var selectedYear by remember { mutableStateOf(0) }

    Column(
        Modifier.background(color = Color.Black).fillMaxSize()
    ) {

        // 📌 TabRow: Movies - TV Shows geçişi
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            ) { Text("Movies") }

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            ) { Text("TV Shows") }
        }

        // 📌 Filtreleme UI
        Column(Modifier.padding(16.dp)) {
            GenreDropdownMenu(
                selectedGenre = selectedGenre,
                onGenreSelected = { genre ->
                    selectedGenre = genre
                    if (selectedTab == 0) {
                        viewModel.fetchFilteredMovies(genre,minRating,selectedYear)
                    } else {
                        viewModel.fetchFilteredTvShows(genre, minRating, selectedYear)
                    }
                }

            )


            // Puan filtresi
            Text("Min Rating: ${minRating.toInt()}", color = Color.White)
            Slider(
                value = minRating,
                onValueChange = { rating ->
                    minRating = rating
                    if (selectedTab == 0) viewModel.fetchFilteredMovies(selectedGenre, rating, selectedYear)
                    else viewModel.fetchFilteredTvShows(selectedGenre, rating, selectedYear)
                },
                valueRange = 0f..10f
            )

            TextField(
                value = selectedYear.toString(),
                onValueChange = { year ->
                    if (year.all { it.isDigit() }) {
                        selectedYear = year.toIntOrNull() ?: 0
                        // Filtreleme işlemi
                        if (selectedTab == 0) {
                            viewModel.fetchFilteredMovies(selectedGenre, minRating, selectedYear)
                        } else {
                            viewModel.fetchFilteredTvShows(selectedGenre, minRating, selectedYear)
                        }
                    }
                },
                label = { Text("Year", color = Color.Black) },  // Etiket rengini beyaz yapıyoruz
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true, // Tek satırda tutma
                isError = false // Hata durumu yok
            )

        }

        if (selectedTab == 0) {
            movies?.let { MovieList(it, navController) }
        } else {
            tvShows?.let { TVShowList(it, navController) }
        }

    }
}
@Composable
fun GenreDropdownMenu(
    selectedGenre: Int,
    onGenreSelected: (Int) -> Unit,

) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = true }) {
            // Seçilen türün int değerine göre metni göstermek
            Text(text = "Select Genre: ", color = Color.White, fontSize = 24.sp)
        }

        // Dropdown menüsünü açma/kapama işlemi
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            movieGenres.entries.forEach { entry  ->
                DropdownMenuItem(
                    text = { Text( text = entry.key)}, // Film türünü yazdırıyoruz
                    onClick = {
                        onGenreSelected(entry.value)
                       // Seçilen türü geri döndür
                        expanded = false // Menüyü kapat
                    }
                )
            }
        }
    }
}
@Composable
fun MovieList(movies : List<MovieDetailForDiscover>, navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(movies) { movie ->
                println(movie.title)
                MovieDetailItem(
                    movie = movie,
                    navController = navController,
                )
            }
        }
    }
}
@Composable
fun MovieDetailItem(movie: MovieDetailForDiscover, navController: NavController) {
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
fun TVShowList(tvShows : List<TvShowDetailForDiscover>, navController: NavController){

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp), // Minimum item genişliği
            modifier = Modifier.fillMaxSize()
        ) {
            items(tvShows) { show ->

                TvShowDetailItem(
                    tvShow = show,
                    navController = navController,
                )
            }
        }
    }
}
@Composable
fun TvShowDetailItem(tvShow: TvShowDetailForDiscover, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                // Tıklanan film detaylarına yönlendiriyoruz
                navController.navigate("movieDetail/${tvShow.id}")
            }
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
            contentDescription = tvShow.name,
            modifier = Modifier
                .height(250.dp)  // Poster yüksekliği
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))  // Yuvarlatılmış köşeler
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))  // Hafif gölge efekti
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filmin adı
        Text(
            text = tvShow.name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis, // Uzun başlıklar kesilecek
            modifier = Modifier.padding(horizontal = 4.dp),
            color = Color.White

        )


    }
}



