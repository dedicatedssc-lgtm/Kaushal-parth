package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun RevikLogo(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * (5f / 24f)
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f
        
        val path1 = Path().apply {
            moveTo(10f * scaleX, 4f * scaleY)
            lineTo(20f * scaleX, 4f * scaleY)
            lineTo(20f * scaleX, 14f * scaleY)
        }
        
        val path2 = Path().apply {
            moveTo(14f * scaleX, 20f * scaleY)
            lineTo(4f * scaleX, 20f * scaleY)
            lineTo(4f * scaleX, 10f * scaleY)
        }
        
        drawPath(
            path = path1,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Bevel
            )
        )
        
        drawPath(
            path = path2,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Bevel
            )
        )
    }
}
