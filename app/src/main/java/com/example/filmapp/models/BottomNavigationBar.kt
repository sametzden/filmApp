package com.example.filmapp.models


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.filmapp.R


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(null, Icons.Filled.Home, "homeScreen"),
        BottomNavItem(null, Icons.Filled.Search, "explore"),
        BottomNavItem(null, Icons.Outlined.BookmarkBorder, "saved"),
        BottomNavItem(null, Icons.Default.History, "watched"),
        //BottomNavItem(null, Icons.Filled.Person, "profile")
    )

    var selectedItem by rememberSaveable { mutableStateOf(0) }

    Box(
        modifier = Modifier

            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottombar),
            "",
            contentScale = ContentScale.Fit,
            alpha = 0.5f // Şeffaflık
        )
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.White
        ) {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                    label = {
                        item.title?.let {
                            Text(
                                it,
                                color = if (selectedItem == index) Color.Cyan else Color.White
                            )
                        }
                    },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item.route)
                    }
                )
            }
        }
    }
}



data class BottomNavItem(val title: String?, val icon: ImageVector, val route: String)

