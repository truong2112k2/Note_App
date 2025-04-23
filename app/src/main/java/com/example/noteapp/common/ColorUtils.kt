package com.example.noteapp.common

import androidx.core.graphics.ColorUtils
import kotlin.random.Random

object ColorUtils {
    fun getRandomLightColorExcludingBlue(): Int {
        val hueRanges = listOf(
            0..200,   // Reds to greens
            280..359  // Pinks to reds
        )
        val selectedRange = hueRanges.random()
        val hue = Random.nextInt(selectedRange.first, selectedRange.last + 1).toFloat()
        val saturation = Random.nextFloat() * 0.3f + 0.7f // Saturation: 0.7 to 1.0
        val lightness = Random.nextFloat() * 0.2f + 0.8f  // Lightness: 0.8 to 1.0

        // Convert HSL to RGB
        val hsl = floatArrayOf(hue, saturation, lightness)
        return ColorUtils.HSLToColor(hsl)
    }
}