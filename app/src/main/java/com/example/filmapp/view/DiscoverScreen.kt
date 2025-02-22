package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.filmapp.R

import com.example.filmapp.data.MovieDetailForDiscover

import com.example.filmapp.data.TvShowDetailForDiscover

import com.example.filmapp.models.DiscoverViewModel
import kotlinx.coroutines.launch

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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel = viewModel(), navController: NavController) {
    val movies by viewModel.movies.observeAsState()
    val tvShows by viewModel.tvShows.observeAsState()
    val tabs = listOf("Movies", "TV Shows")
    var selectedTab by remember { mutableStateOf(0) }
    var selectedGenre: Int? by remember { mutableStateOf(null) }
    var minRating by remember { mutableStateOf(0f) }
    var selectedMood by remember { mutableStateOf("") }
    var showMovieList by remember { mutableStateOf(true) }

    // Drawer state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Main content with drawer
    ModalDrawer(
        drawerState = drawerState,
        drawerBackgroundColor = Color.Transparent,
        drawerContent = {
            FilterDrawerContent(
                selectedTab = selectedTab,
                selectedGenre = selectedGenre,
                minRating = minRating,
                selectedMood = selectedMood,
                onGenreSelected = { genreId ->
                    selectedGenre = if (selectedGenre == genreId) null else genreId
                    selectedMood = "" // Clear mood when genre is selected
                    showMovieList = true
                    if (selectedGenre != null) {
                        filterItems(viewModel, selectedTab, selectedGenre!!, minRating)
                    }
                    scope.launch { drawerState.close() } // Close drawer after selection
                },
                onRatingChange = { rating ->
                    minRating = rating
                    if (selectedGenre != null) {
                        filterItems(viewModel, selectedTab, selectedGenre!!, minRating)
                    }
                },
                onMoodSelected = { mood ->
                    selectedMood = mood
                    selectedGenre = null // Clear genre when mood is selected
                    viewModel.fetchByMood(mood, selectedTab)
                    showMovieList = true
                    scope.launch { drawerState.close() } // Close drawer after selection
                }
            )
        }
    ) {
        Scaffold(

            topBar = {
                TopAppBar(
                    title = { Text("Discover", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF242A32), Color(0xFF1B0505))))
            ) {

            Column(
                Modifier
                    .padding(0.dp)
                    .background(color = Color.Transparent)
                    .fillMaxSize()
            ) {
                // Tabs for Movies/TV Shows
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                selectedMood = ""
                                // Reset filters when switching tabs
                                if (selectedGenre != null) {
                                    filterItems(viewModel, index, selectedGenre!!, minRating)
                                } else if (selectedMood.isNotEmpty()) {
                                    viewModel.fetchByMood(selectedMood, index)
                                } else {
                                    // if (index == 0) viewModel.fetchMovies() else viewModel.fetchTvShows()
                                }
                            }
                        ) {
                            Text(
                                title,
                                color = Color.White,
                                modifier = Modifier.padding(paddingValues = paddingValues)
                            )
                        }
                    }
                }

                // Show the applied filter if any
                AppliedFiltersSection(
                    selectedTab = selectedTab,
                    selectedGenre = selectedGenre,
                    selectedMood = selectedMood,
                    onClearGenre = {
                        selectedGenre = null
                        // if (selectedTab == 0) viewModel.fetchMovies() else viewModel.fetchTvShows()
                    },
                    onClearMood = {
                        selectedMood = ""
                        //  if (selectedTab == 0) viewModel.fetchMovies() else viewModel.fetchTvShows()
                    }
                )

                // Content area
                if (showMovieList) {
                    if (selectedTab == 0) {
                        movies?.let { MovieList(it, navController) }
                    } else {
                        tvShows?.let { TVShowList(it, navController) }
                    }
                }
            }
        }
    }
    }
}

@Composable
fun AppliedFiltersSection(
    selectedTab: Int,
    selectedGenre: Int?,
    selectedMood: String,
    onClearGenre: () -> Unit,
    onClearMood: () -> Unit
) {
    if (selectedGenre != null || selectedMood.isNotEmpty()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Applied Filters:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedGenre != null) {
                    val genreMap = if (selectedTab == 0) movieGenres else showGenres
                    val genreName = genreMap.entries.find { it.value == selectedGenre }?.key ?: "Unknown"
                    FilterChip(
                        text = "Genre: $genreName",
                        onClear = onClearGenre
                    )
                }
                if (selectedMood.isNotEmpty()) {
                    FilterChip(
                        text = "Mood: $selectedMood",
                        onClear = onClearMood
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, onClear: () -> Unit) {
    Surface(
        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .clickable { onClear() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun FilterDrawerContent(
    selectedTab: Int,
    selectedGenre: Int?,
    minRating: Float,
    selectedMood: String,
    onGenreSelected: (Int) -> Unit,
    onRatingChange: (Float) -> Unit,
    onMoodSelected: (String) -> Unit
) {
    var currentFilterTab by remember { mutableStateOf(0) }
    val filterTabs = listOf("Genre", "Mood")

    Column(
        Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.Transparent)
            .padding(16.dp)
    ) {
        Text(
            "Discover Options",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Filter type tabs
        TabRow(
            selectedTabIndex = currentFilterTab,
            containerColor = Color.Transparent
        ) {
            filterTabs.forEachIndexed { index, title ->
                Tab(
                    selected = currentFilterTab == index,
                    onClick = { currentFilterTab = index },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        title,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter content based on selected tab
        when (currentFilterTab) {
            0 -> {
                GenreFilterContent(selectedTab, selectedGenre, onGenreSelected)
                // Rating slider Genre sekmesinde kalıyor
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    Text(
                        "Minimum Rating: ${minRating.toInt()}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = minRating,
                        onValueChange = onRatingChange,
                        valueRange = 0f..10f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color(0xFF1E88E5),
                            inactiveTrackColor = Color(0xFF424242)
                        )
                    )
                }
            }
            1 -> MoodFilterContent(selectedTab, selectedMood, onMoodSelected)
        }
    }
}

@Composable
fun GenreFilterContent(
    selectedTab: Int,
    selectedGenre: Int?,
    onGenreSelected: (Int) -> Unit
) {
    Text(
        "Select Genre",
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    Column(Modifier.verticalScroll(rememberScrollState())) {
        val genreEntries = if (selectedTab == 0) movieGenres.entries.toList() else showGenres.entries.toList()

        genreEntries.forEach { entry ->
            GenreItem(
                genre = entry.key,
                genreId = entry.value,
                isSelected = selectedGenre == entry.value,
                onGenreClick = onGenreSelected
            )
            Divider(color = Color(0xFF2A2A2A), thickness = 0.5.dp)
        }
    }
}

@Composable
fun GenreItem(
    genre: String,
    genreId: Int,
    isSelected: Boolean,
    onGenreClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGenreClick(genreId) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = genre,
            color = if (isSelected) Color(0xFF1E88E5) else Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E88E5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MoodFilterContent(
    selectedTab: Int,
    selectedMood: String,
    onMoodSelected: (String) -> Unit
) {
    val moods = if (selectedTab == 0)
        listOf("Mutlu", "Üzgün", "Heyecanlı", "Romantik", "Korkmuş", "Motivasyona İhtiyacım Var")
    else
        listOf("Mutlu", "Üzgün", "Motivasyona İhtiyacım Var")

    val moodColors = mapOf(
        "Mutlu" to Color(0xFFFFC107),
        "Üzgün" to Color(0xFF5C6BC0),
        "Heyecanlı" to Color(0xFFFF5722),
        "Romantik" to Color(0xFFE91E63),
        "Korkmuş" to Color(0xFF673AB7),
        "Motivasyona İhtiyacım Var" to Color(0xFF4CAF50)
    )

    val moodIcons = mapOf(
        "Mutlu" to "😊",
        "Üzgün" to "😢",
        "Heyecanlı" to "🤩",
        "Romantik" to "❤️",
        "Korkmuş" to "😨",
        "Motivasyona İhtiyacım Var" to "💪"
    )

    Text(
        "How do you feel today?",
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(moods) { mood ->
            MoodCard(
                mood = mood,
                icon = moodIcons[mood] ?: "😐",
                color = moodColors[mood] ?: Color.Gray,
                isSelected = selectedMood == mood,
                onMoodClick = onMoodSelected
            )
        }
    }
}

@Composable
fun MoodCard(
    mood: String,
    icon: String,
    color: Color,
    isSelected: Boolean,
    onMoodClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .clickable { onMoodClick(mood) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isSelected) color else color.copy(alpha = 0.6f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mood,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

// Existing functions with minor modifications for consistent UI
private fun filterItems(viewModel: DiscoverViewModel, selectedTab: Int, selectedGenre: Int, minRating: Float) {
    if (selectedTab == 0) {
        viewModel.fetchFilteredMovies(selectedGenre, minRating)
    } else {
        viewModel.fetchFilteredTvShows(selectedGenre, minRating)
    }
}

@Composable
fun MovieList(movies: List<MovieDetailForDiscover>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieDetailItem(
                movie = movie,
                navController = navController,
            )
        }
    }
}

@Composable
fun MovieDetailItem(movie: MovieDetailForDiscover, navController: NavController) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate("movieDetail/${movie.id}")
            }
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
            contentDescription = movie.title,
            modifier = Modifier
                .height(225.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        movie.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
        }
    }
}

@Composable
fun TVShowList(tvShows: List<TvShowDetailForDiscover>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tvShows) { show ->
            TvShowDetailItem(
                tvShow = show,
                navController = navController,
            )
        }
    }
}

@Composable
fun TvShowDetailItem(tvShow: TvShowDetailForDiscover, navController: NavController) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate("tvShowDetail/${tvShow.id}")
            }
    ) {
        Image(
            painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
            contentDescription = tvShow.name,
            modifier = Modifier
                .height(225.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .shadow(8.dp, shape = RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = tvShow.name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}



