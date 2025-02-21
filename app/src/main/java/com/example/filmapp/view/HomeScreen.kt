package com.example.filmapp.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.filmapp.data.MediaItem
import com.example.filmapp.data.Movie
import com.example.filmapp.data.Person
import com.example.filmapp.data.TVShow
import com.example.filmapp.models.DiscoverViewModel
import com.example.filmapp.models.MovieViewModel
import com.example.filmapp.models.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sin


@Composable
fun HomeScreen(navController: NavController, viewModel: MovieViewModel, discoverViewModel: DiscoverViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")
    val searchViewModel = SearchViewModel()
    var isSearchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.size(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Arama ve Profil butonunu ayırmak için
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "What do you want to watch?",
                    color = Color.White,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(top = 30.dp, bottom = 15.dp, start = 16.dp) // Start padding eklendi
                )

                // Profil Butonu
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            SearchScreen(
                searchViewModel,
                navController,
                onSearchActiveChange = { isSearchActive = it }
            )

            if (!isSearchActive) {
                // Sekmeler (Movies - TV Shows)
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.background(color = Color.Black),
                    containerColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            height = 2.dp,
                            color = Color.White
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = if (index == 0) Icons.Default.Movie else Icons.Default.Tv,
                                        contentDescription = title,
                                        tint = if (selectedTab == index) Color.White else Color.Gray
                                    )
                                    Text(
                                        text = title,
                                        color = if (selectedTab == index) Color.White else Color.Gray,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.animateContentSize()
                                    )
                                }
                            }
                        )
                    }
                }

                // Seçili Sekmeye Göre İçerik
                if (selectedTab == 0) {
                    MovieList(navController, viewModel)
                } else {
                    TVShowList(navController, viewModel)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController, onSearchActiveChange: (Boolean) -> Unit) {
    var query by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        isDropdownVisible = query.length > 2
        onSearchActiveChange(isDropdownVisible) // Arama aktifse HomeScreen'deki bileşenleri gizle
        if (isDropdownVisible) {
            viewModel.searchMoviesAndTvShows(query)
        }
    }

    val results by viewModel.searchResults

    Box {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color(0x20ffffff), shape = RoundedCornerShape(50.dp))
                .padding(horizontal = 50.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .padding(top = 3.dp)
            )
            Spacer(Modifier.width(10.dp))
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search Movies & TV Shows", color = Color(0xffbdbdbd) )},
                colors =TextFieldDefaults.textFieldColors(
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.White,
                    focusedTextColor = Color.White,
                    containerColor = Color.Transparent,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            isDropdownVisible = false
                            onSearchActiveChange(false) // **Odak kaybolunca HomeScreen içeriğini geri getir**
                        }
                    },
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                shape = RoundedCornerShape(50.dp),
                singleLine = true
            )
        }

        if (isDropdownVisible && results.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .align(Alignment.TopStart)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(results.take(5)) { item ->
                     SearchResultItem(item, navController)
                    }
                }
            }
        }
    }
}




@Composable
fun SearchResultItem(item: MediaItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Eğer Movie ise MovieDetail sayfasına, TVShow ise TVShowDetail sayfasına git
                if (item is Movie) {
                    navController.navigate("movieDetail/${item.id}")
                } else if (item is TVShow) {
                    navController.navigate("tvShowDetail/${item.id}")
                } else if (item is Person) {
                    navController.navigate("actorDetail/${item.id}")
                }
            }
            .padding(16.dp)
    ) {
        if(item is Person){
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${item.profilePath}",
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            item.name.let { Text(text = it, fontSize = 18.sp, color = Color.White) }
        }else{
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${item.poster_path}",
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                if (item is Movie) {
                    item.title?.let { Text(text = it, fontSize = 18.sp, color = Color.White) }

                } else if (item is TVShow) {
                    item.name?.let { Text(text = it, fontSize = 18.sp, color = Color.White) }
                }
                Text(text ="Rating is " + item.vote_average?.toInt().toString(), fontSize = 18.sp, color = Color.White)


            }
        }






    }
}


@Composable
fun MovieList(navController: NavController, viewModel: MovieViewModel) {
    val nowPlaying = viewModel.nowPlayingMovies
    val popularMovies = viewModel.popularMovies1
    val topRatedMovies = viewModel.topRatedMovies
    val upcomingMovies = viewModel.upcomingMovies
    var selectedTab by remember { mutableStateOf(0) }
    val movieTabs = listOf("Popular", "Up Coming", "Top Rated", "Now Playing")
    viewModel.watchedItems()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // TabRow ekleniyor
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Black),
                    containerColor = Color.Black,
                    contentColor = Color.Black
                ) {
                    movieTabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTab == index) Color.Cyan else Color.White
                                )
                            }
                        )
                    }
                }

                // Seçilen tab'a göre içerik gösterimi
                when (selectedTab) {
                    0 -> PopularMoviesSection(navController, popularMovies)
                    1 -> UpcomingMoviesSection(navController, upcomingMovies)
                    2 -> TopRatedMoviesSection(navController, topRatedMovies)
                    3 -> NowPlayingMoviesSection(navController, nowPlaying,)
                }


            }
        }
    }
}
@Composable
fun PopularMoviesSection(
    navController: NavController,
    popularMovies: List<Movie>,

) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Popular Movies", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {
                navController.navigate("seeAllScreen/popularMovies")
            })
            {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(popularMovies.take(15)) { movie ->
                MovieItem(movie = movie, navController)
            }
        }
    }
}

@Composable
fun UpcomingMoviesSection(
    navController: NavController,
    upcomingMovies: List<Movie>,

) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Up Coming Movies", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {

                navController.navigate("seeAllScreen/upcomingMovies")
            }) {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(upcomingMovies) { movie ->
                MovieItem(movie = movie, navController)
            }
        }
    }
}

@Composable
fun TopRatedMoviesSection(
    navController: NavController,
    topRatedMovies: List<Movie>,

) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Top Rated Movies", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {

                navController.navigate("seeAllScreen/topRatedMovies")
            }) {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(topRatedMovies.take(15)) { movie ->
                MovieItem(movie = movie, navController)
            }
        }
    }
}

@Composable
fun NowPlayingMoviesSection(
    navController: NavController,
    nowPlaying: List<Movie>,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Now Playing Movies", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {
                navController.navigate("seeAllScreen/nowPlayingMovies")
            }) {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(nowPlaying) { movie ->
                MovieItem(movie = movie, navController)
            }
        }
    }
}






@Composable
fun TVShowList(navController: NavController, viewModel: MovieViewModel) {
    val popularShows = viewModel.popularTVShows1
    val topRatedTVShows =viewModel.topRatedTVShows
    val nowPlayingTVShows  =viewModel.nowPlayingTVShows
    var selectedTab1 by remember { mutableStateOf(0) }
    val showTabs = listOf("Popular", "Top Rated", "Now Playing")
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        // TabRow ekleniyor
        TabRow(
            selectedTabIndex = selectedTab1,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
            containerColor = Color.Black,
            contentColor = Color.Black
        ) {
            showTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab1 == index,
                    onClick = { selectedTab1 = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab1== index) Color.Cyan else Color.White
                        )
                    }
                )
            }
        }
        // Seçilen tab'a göre içerik gösterimi
        when (selectedTab1) {
            0 -> PopularShowSection(navController, popularShows)
            1 -> TopRatedShowSection(navController, topRatedTVShows)
            2 -> NowPlayingShowSection(navController, nowPlayingTVShows )
        }

    }

}

@Composable
fun PopularShowSection(
    navController: NavController,
    TVShows: List<TVShow>,
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Popular Shows", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {
                navController.navigate("seeAllTVShowsScreen/popularShows")
            })
            {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(TVShows.take(15)) { show ->

                TVShowItem(tvShow = show, navController)
            }
        }
    }
}
@Composable
fun TopRatedShowSection(
    navController: NavController,
    TopRatedTVShows: List<TVShow>,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Top Rated Shows", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {
                navController.navigate("seeAllTVShowsScreen/topRatedShows")
            })
            {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(TopRatedTVShows.take(15)) { show ->
                TVShowItem(tvShow = show, navController)
            }
        }
    }
}
@Composable
fun NowPlayingShowSection(
    navController: NavController,
    NowPlayingTVShows: List<TVShow>,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Now Playing Shows", color = Color.White, fontSize = 18.sp)
            TextButton(onClick = {
                navController.navigate("seeAllTVShowsScreen/nowPlayingShows")
            })
            {
                Text("See All", color = Color.Cyan, fontSize = 14.sp)
            }
        }
        LazyRow {
            items(NowPlayingTVShows.take(15)) { show ->
                TVShowItem(tvShow = show, navController)
            }
        }
    }
}


@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Genişlik arttırıldı
            .clickable {
                navController.navigate("movieDetail/${movie.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp) // Köşe yuvarlama arttırıldı
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // Yükseklik arttırıldı
        ) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Başlık ve Puan (Resim üzerine entegre)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient( // Gradyan arka plan
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = movie.title ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy( // Başlık boyutu büyütüldü
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, // İkon eklendi
                            contentDescription = "Puan",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie.vote_average),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TVShowItem(tvShow: TVShow, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Genişlik arttırıldı
            .clickable {
                navController.navigate("tvShowDetail/${tvShow.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp) // Köşe yuvarlama arttırıldı
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp) // Yükseklik arttırıldı
        ) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
                contentDescription = tvShow.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Başlık ve Puan (Resim üzerine entegre)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient( // Gradyan arka plan
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = tvShow.name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy( // Başlık boyutu büyütüldü
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (tvShow.vote_average != null) { // Puan null değilse göster
                            Icon(
                                imageVector = Icons.Default.Star, // İkon eklendi
                                contentDescription = "Puan",
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.1f", tvShow.vote_average),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("homeScreen", Icons.Default.Home, "Home")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}



