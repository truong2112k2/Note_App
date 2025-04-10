package com.example.noteapp.ui.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun MeshGradientBackground() {
    val primaryColor = MaterialTheme.colorScheme.primary  // ✅ lấy ra trước

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.White, size = size)

        // Vùng vàng chính - mạnh và to
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor,
                //     Color(0xFFFFEB3B),     // vàng đậm
                    Color(0xFFFFF59D),     // vàng nhạt
                    Color.Transparent      // kết thúc
                ),
                radius = size.maxDimension * 0.9f
            ),
            center = Offset(size.width * 0.4f, size.height * 0.5f)
        )

        // Vùng phụ 1
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor,
//                    Color(0xFFFFF176),
                    Color.Transparent),
                radius = size.minDimension * 0.6f
            ),
            center = Offset(size.width * 0.8f, size.height * 0.3f)
        )

        // Vùng phụ 2
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor,
                    //Color(0xFFFFFF8D),
                    Color.Transparent),
                radius = size.minDimension * 0.7f
            ),
            center = Offset(size.width * 0.2f, size.height * 0.75f)
        )
    }
}
