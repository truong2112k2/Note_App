package com.example.noteapp.ui.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GradientBackground() {
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = primaryColor, size = size)


        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor,
                    Color(0xFFFFF59D),
                    Color.Transparent
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


