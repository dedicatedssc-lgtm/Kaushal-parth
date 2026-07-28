package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.ui.components.RevikLogo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var isScaled by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isScaled) 1f else 0.8f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "zoom_in"
    )

    LaunchedEffect(Unit) {
        isScaled = true
        delay(2500)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientEnd),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).scale(scale),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RevikLogo(
                modifier = Modifier.size(64.dp),
                color = RevikLimeGreen
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "REVIK",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = RevikWhite,
                letterSpacing = 4.sp
            )
            Text(
                text = "LINK",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = RevikLimeGreen,
                letterSpacing = 4.sp
            )
        }
        
        Text(
            text = "PAGE 1 app on",
            fontSize = 12.sp,
            color = RevikWhite,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 12.dp)
        )
    }
}
