package com.example.demo.camerax

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class OvalShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val viewportWidth = 219f
        val viewportHeight = 299f

        val scaleX = size.width / viewportWidth
        val scaleY = size.height / viewportHeight

        val path = Path().apply {
            // Scale the coordinates to fit the given size
            moveTo(216.56f * scaleX, 135.59f * scaleY)
            cubicTo(
                216.56f * scaleX, 216.92f * scaleY,
                164.75f * scaleX, 296.53f * scaleY,
                108.81f * scaleX, 296.53f * scaleY
            )
            cubicTo(
                52.87f * scaleX, 296.53f * scaleY,
                2.44f * scaleX, 221.34f * scaleY,
                2.44f * scaleX, 140.01f * scaleY
            )
            cubicTo(
                2.44f * scaleX, 58.67f * scaleY,
                30.63f * scaleX, 2f * scaleY,
                108.81f * scaleX, 2f * scaleY
            )
            cubicTo(
                186.99f * scaleX, 2f * scaleY,
                216.56f * scaleX, 54.26f * scaleY,
                216.56f * scaleX, 135.59f * scaleY
            )

            close()
        }
        return Outline.Generic(path)
    }
}
