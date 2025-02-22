package com.example.filmapp.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF242A32), Color(0xFF1B0505)))),
        contentAlignment = Alignment.Center
    ) {
        val alpha = remember { Animatable(0f) }
        LaunchedEffect(key1 = true) {
            alpha.animateTo(1f, animationSpec = tween(2000))
            delay(2000)
            if (FirebaseAuth.getInstance().currentUser != null) {
                navController.navigate("homeScreen") {
                    popUpTo("splash_screen") { inclusive = true }
                }
            } else {
                navController.navigate("sign_in")
            }
        }
        // Logoyu tam ekran kaplayacak şekilde ayarla
        Image(
            painter = painterResource(id = R.drawable.lumix),
            contentDescription = "Uygulama Logosu",
            modifier = Modifier.fillMaxSize()
        )
        // Metni en alta yerleştir ve soluklaştır
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp), // İsteğe bağlı olarak metnin alttan uzaklığını ayarlayabilirsiniz
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "İlk hareketten sonsuz hikayelere...",
                style = MaterialTheme.typography.h6, // Yazı boyutunu küçülttüm
                color = Color.White.copy(alpha = 0.7f), // Metni soluklaştırdım
                textAlign = TextAlign.Center // Metni ortaladım
            )
        }
    }
}




