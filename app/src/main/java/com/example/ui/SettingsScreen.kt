package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ble.BleManager
import com.example.ui.theme.*
import com.example.ui.components.RevikLogo

@Composable
fun SettingsScreen(navController: NavController, viewModel: BikeViewModel) {
    val connectionState by viewModel.connectionState.collectAsState()
    val isConnected = connectionState == BleManager.ConnectionState.CONNECTED

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientEnd),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RevikLogo(
                        modifier = Modifier.size(24.dp),
                        color = RevikLimeGreen
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REVIK ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RevikWhite,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "LINK",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RevikLimeGreen,
                        letterSpacing = 1.sp
                    )
                }

                // Back arrow and Status Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RevikWhite,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.navigateUp() }
                    )

                    val pillBgColor = CardLightGrey
                    val pillTextColor = if (isConnected) RevikLimeGreen else RevikBlue

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(pillBgColor)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isConnected) "Online" else "Offline",
                            color = pillTextColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Center Content: 2x2 Grid of buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsGridButton(
                        text = "Devices",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("devices") }
                    )
                    SettingsGridButton(
                        text = "Auto connect",
                        modifier = Modifier.weight(1f),
                        onClick = { /* Auto connect action */ }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsGridButton(
                        text = "Devices Password",
                        modifier = Modifier.weight(1f),
                        onClick = { /* Password action */ }
                    )
                    SettingsGridButton(
                        text = "Contact Us",
                        modifier = Modifier.weight(1f),
                        onClick = { /* Contact Us action */ }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            Spacer(modifier = Modifier.height(104.dp)) // padding to match Bottom Nav Bar space of MainScreen
        }
        
        // Bottom Left Text
        Text(
            text = "PAGE 5 setting",
            fontSize = 12.sp,
            color = RevikWhite,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 12.dp)
        )
    }
}

@Composable
fun SettingsGridButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(90.dp) // Shorter button
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .background(RevikLimeGreen, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextDarkGrey,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
