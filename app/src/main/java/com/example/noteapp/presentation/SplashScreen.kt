package com.example.noteapp.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.noteapp.R
import com.example.noteapp.ui.background.GradientBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Sử dụng LaunchedEffect để delay và chuyển màn
    LaunchedEffect(key1 = true) {
        delay(2000) // 2 giây
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true } // Xóa splash khỏi back stack
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GradientBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_app),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier.size(80.dp)


            )
            Text(
                text = "Note App",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
