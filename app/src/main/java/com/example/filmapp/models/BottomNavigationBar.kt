package com.example.filmapp.models


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmapp.R


@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(null, Icons.Filled.Home, "homeScreen"),
        BottomNavItem(null, Icons.Filled.Search, "explore"),
        BottomNavItem(null, Icons.Outlined.BookmarkBorder, "saved"),
        BottomNavItem(null, Icons.Default.History, "watched"),
        // BottomNavItem(null, Icons.Filled.Person, "profile")
    )

    var selectedItem by rememberSaveable { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .background(color = Color(0xFF1B0708))
            .height(70.dp)
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
            elevation = 10.dp
        ) {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selectedItem == index) Color.Cyan else Color.White,
                            modifier = Modifier.size(30.dp) // Instagram'daki gibi büyük ikonlar
                        )
                    },
                    label = {
                        item.title?.let {
                            Text(
                                it,
                                fontSize = 12.sp, // Yazıyı küçültüp estetik hale getirdik
                                color = if (selectedItem == index) Color.Cyan else Color.White,
                                fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item.route)
                    },
                    alwaysShowLabel = false,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}




data class BottomNavItem(val title: String?, val icon: ImageVector, val route: String)

