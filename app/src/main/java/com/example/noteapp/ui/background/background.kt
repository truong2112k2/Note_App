package com.example.noteapp.ui.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GradientBackground() {
    val primaryColor = MaterialTheme.colorScheme.primary  // ✅ lấy ra trước

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = primaryColor, size = size)

        // Vùng vàng chính - mạnh và to
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor,
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
                    Color.Transparent),
                radius = size.minDimension * 0.7f
            ),
            center = Offset(size.width * 0.2f, size.height * 0.75f)
        )
    }
}

@Composable
fun StarryBackground() {
    val backgroundColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Vẽ nền
        drawRect(color = backgroundColor, size = size)

        // Tạo danh sách ngôi sao ngẫu nhiên
        val numberOfStars = 100
        repeat(numberOfStars) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height
            val radius = Random.nextFloat() * 3f + 1f  // Kích thước từ 1 đến 4
            drawCircle(
                color = Color.White.copy(alpha = Random.nextFloat() * 0.8f + 0.2f), // ánh sáng lấp lánh
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}

