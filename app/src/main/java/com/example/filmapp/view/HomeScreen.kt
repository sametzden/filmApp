package com.example.filmapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.filmapp.models.MovieViewModel
import com.example.filmapp.models.SearchViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun HomeScreen(navController: NavController, viewModel: MovieViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")
    val searchViewModel = SearchViewModel()
    var isSearchActive by remember { mutableStateOf(false) } // Arama açık mı kontrolü
    var currentUser =FirebaseAuth.getInstance().currentUser
   println(currentUser?.displayName.toString() + "homescreen cagırıldı")
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Üst Kısım (Başlık + Arama Çubuğu)

            Spacer(Modifier.size(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "What do you want to watch?", color = Color.White, fontSize = 22.sp,
                    modifier = Modifier.padding(top = 30.dp, bottom = 15.dp, start = 60.dp)
                )
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp).padding(top = 3.dp)
                    )
                }

            }


            SearchScreen(
                searchViewModel,
                navController,
                onSearchActiveChange = { isSearchActive = it }
            )

            if (!isSearchActive) { // **Sadece arama açık değilse diğer bileşenleri göster**


                // Sekmeler (Movies - TV Shows)
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
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search Movies & TV Shows") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        isDropdownVisible = false
                        onSearchActiveChange(false) // **Odak kaybolunca HomeScreen içeriğini geri getir**
                    }
                }
        )

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
                }else if (item is Person){
                    navController.navigate ("actorDetail/${item.id}")
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

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // TabRow ekleniyor
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth().background(color = Color.Black),
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
            3 -> NowPlayingMoviesSection(navController, nowPlaying, )
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

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // TabRow ekleniyor
        TabRow(
            selectedTabIndex = selectedTab1,
            modifier = Modifier.fillMaxWidth().background(color = Color.Black),
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
fun TVShowItem(tvShow: TVShow,navController: NavController) {

    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                navController.navigate("tvShowDetail/${tvShow.id}")
            }  // Poster genişliğini sabit tutuyoruz
    ) {
        // Dizinin posterini gösteriyoruz
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

        // Dizinin adı
        tvShow.name?.let {
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

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("homeScreen", Icons.Default.Home, "Home")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )
    var isSheetOpen by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { isSheetOpen = false }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Home", modifier = Modifier.clickable {
                    navController.navigate("homeScreen")
                    isSheetOpen = false
                })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Profile", modifier = Modifier.clickable {
                    navController.navigate("profile")
                    isSheetOpen = false
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersistentBottomSheet(navController: NavController) {
    BottomSheetScaffold(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Navigation", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("homeScreen") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Home")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Profile")
                }
            }
        },
        sheetPeekHeight = 100.dp // Alt kısımda sürekli görünür olacak yükseklik
    ) { paddingValues ->
        // Ana içeriğin buraya gelecek
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Main Content", fontSize = 24.sp)
        }
    }
}

