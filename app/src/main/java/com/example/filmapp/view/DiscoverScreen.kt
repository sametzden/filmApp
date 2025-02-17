package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

import com.example.filmapp.data.MovieDetailForDiscover

import com.example.filmapp.data.TvShowDetailForDiscover

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
val showGenres = mapOf(
    "Animation" to 16,
    "Comedy" to 35,
    "Crime" to 80,
    "Documentary" to 99,
    "Drama" to 18,
    "Family" to 10751,
    "Kids" to 10762,
    "Mystery" to 9648,
    "News" to 10763,
    "Reality" to 10764,
    "Science Fiction" to 878,
    "Soap" to 10766,
    "Talk" to 10767,
    "War & Politics" to 10768,
    "Western" to 37
)
@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = viewModel(), navController: NavController) {
    val movies by viewModel.movies.observeAsState()
    val tvShows by viewModel.tvShows.observeAsState()
    val tabs = listOf("Movies", "TV Shows")
    val tabs2 = listOf("Tür Seç", "Ruh Halini Seç")
    var selectedTab2 by remember { mutableStateOf(0) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedGenre: Int? by remember { mutableStateOf(null) }
    var minRating by remember { mutableStateOf(0f) }
    var selectedYear by remember { mutableStateOf(0) }
    var showMovieList: Boolean by remember { mutableStateOf(false) }
    Column(
        Modifier.background(color = Color.Black).fillMaxSize()
    ) {

        // 📌 TabRow: Movies - TV Shows geçişi
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
        Spacer(Modifier.padding(16.dp))
        TabRow(
            selectedTabIndex = selectedTab2,
            Modifier.background(color = Color.Black),
            containerColor = Color.Black
        ) {
            tabs2.forEachIndexed { index, title ->
                Tab(selected = selectedTab2 == index, onClick = { selectedTab2 = index }) {
                    Text(title, color = Color.White)
                }
            }
        }

        if (selectedTab2 == 0) {
            // 📌 Filtreleme UI
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val genreEntries = if (selectedTab == 0) movieGenres.entries.toList() else showGenres.entries.toList() // Listeye dönüştür
                items(genreEntries) { entry ->
                    GenreCard(
                        genre = entry.key, // Tür adı (String)
                        genreId = entry.value, // Tür ID'si (Int)
                        isSelected = selectedGenre == entry.value,
                        onGenreClick = { clickedGenreId ->
                            selectedGenre = if (selectedGenre == clickedGenreId) null else clickedGenreId // Türü seç veya iptal et
                            showMovieList = selectedGenre != null
                            if (selectedGenre != null) {
                                filterItems(viewModel, selectedTab, selectedGenre!!, minRating) // Verileri çek
                            }
                        }
                    )
                }
            }


            // Puan filtresi
            Text("Min Rating: ${minRating.toInt()}", color = Color.White)
            Slider(
                value = minRating,
                onValueChange = { rating ->
                    minRating = rating
                    if (selectedTab == 0) viewModel.fetchFilteredMovies(selectedGenre, rating)
                    else viewModel.fetchFilteredTvShows(selectedGenre, rating)
                },
                valueRange = 0f..10f
            )


        }else {
            MoodSelectionScreen(viewModel, selectedTab, navController)
        }

        if (showMovieList) { // Film listesi gösteriliyorsa
            if (selectedTab == 0) {
                movies?.let { MovieList(it, navController) } // Tüm ekranı kaplayacak
            } else {
                tvShows?.let { TVShowList(it, navController) } // Tüm ekranı kaplayacak
            }
        }

    }



}



@Composable
fun GenreCard(genre: String, genreId: Int, isSelected: Boolean, onGenreClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onGenreClick(genreId) }
            .padding(4.dp)
            .width(150.dp)
            .height(80.dp),
        colors = CardColors(
            containerColor = if (isSelected) Color.Blue else Color.Gray,
            contentColor = Color.White,
            disabledContainerColor =  Color.Transparent,
            disabledContentColor = Color.Transparent
        ) ,
        // Seçili/Seçilmemiş renk
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = genre, // Tür adını göster
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}

private fun filterItems(viewModel: DiscoverViewModel, selectedTab: Int, selectedGenre: Int, minRating: Float) {
    if (selectedTab == 0) {
        viewModel.fetchFilteredMovies((selectedGenre), minRating) // Tek bir tür ID ile filtreleme
    } else {
        viewModel.fetchFilteredTvShows((selectedGenre), minRating) // Tek bir tür ID ile filtreleme
    }
}
@Composable
fun MoodSelectionScreen(viewModel: DiscoverViewModel,selectedTab: Int,navController: NavController) {
    val moodsForMovies = listOf("Mutlu", "Üzgün", "Heyecanlı", "Romantik", "Korkmuş", "Motivasyona İhtiyacım Var")
    val moodsForShows = listOf("Mutlu", "Üzgün",  "Motivasyona İhtiyacım Var")
    var selectedMood by remember { mutableStateOf(moodsForMovies[0]) }
    val moodMovies by viewModel.moodMovie.observeAsState()
    val moodTVShow by viewModel.moodTvShows.observeAsState()
    Column(modifier = Modifier.padding(16.dp).height(450.dp)) {
        Text("Ruh Halini Seç", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (selectedTab == 0) {
            MoodDropdownMenu(selectedMood, onMoodSelected = { mood ->
                selectedMood = mood
                viewModel.fetchByMood(mood,selectedTab) // Mood'a göre filmleri getir
            }, moods = moodsForMovies)
        }else{
            MoodDropdownMenu(selectedMood, onMoodSelected = { mood ->
                selectedMood = mood
                viewModel.fetchByMood(mood,selectedTab) // Mood'a göre filmleri getir
            }, moods = moodsForShows)
        }


        // Seçilen ruh haline göre film listesi göster
        if (selectedTab == 0) {
            moodMovies?.let { MovieList(it, navController) }
        } else {
            moodTVShow?.let { TVShowList(it, navController) }
        }
    }
}
@Composable
fun MoodDropdownMenu(
    selectedMood: String,
    onMoodSelected: (String) -> Unit,
    moods: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = selectedMood.ifEmpty { "Ruh Hali Seç" })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            moods.forEach { mood ->
                DropdownMenuItem(
                    text = { Text(mood) }, // Güncel versiyon
                    onClick = {
                        onMoodSelected(mood)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun GenreDropdownMenu(
    selectedGenre: Int,
    onGenreSelected: (Int) -> Unit,
    selectedTab : Int

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
            if (selectedTab == 0){
                movieGenres.entries.forEach { entry  ->
                    DropdownMenuItem(
                        text = { Text( text = entry.key, color = Color.White)}, // Film türünü yazdırıyoruz
                        onClick = {
                            onGenreSelected(entry.value)
                            // Seçilen türü geri döndür
                            expanded = false // Menüyü kapat
                        }, modifier = Modifier.background(color = Color.Black)
                    )
                }
            }else
            {
                showGenres.entries.forEach { entry  ->
                    DropdownMenuItem(
                        text = { Text( text = entry.key, color = Color.White)}, // Film türünü yazdırıyoruz
                        onClick = {
                            onGenreSelected(entry.value)
                            // Seçilen türü geri döndür
                            expanded = false // Menüyü kapat
                        }, modifier = Modifier.background(color = Color.Black)
                    )
                }
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
                navController.navigate("tvShowDetail/${tvShow.id}")
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



