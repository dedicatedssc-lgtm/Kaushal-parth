package com.example.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ble.BleManager
import com.example.ui.theme.*

@SuppressLint("MissingPermission")
@Composable
fun DevicesScreen(navController: NavController, viewModel: BikeViewModel) {
    val scanResults by viewModel.scanResults.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    DisposableEffect(Unit) {
        viewModel.startScan()
        onDispose {
            viewModel.stopScan()
        }
    }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(GradientStart, GradientEnd),
        start = Offset.Zero,
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
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = RevikWhite
                    )
                }
                Text(
                    text = "CONNECT DEVICE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RevikWhite,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            if (connectionState == BleManager.ConnectionState.CONNECTING) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = RevikLimeGreen,
                    trackColor = GradientStart
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scanResults) { device ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TextDarkGrey.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.connect(device.address)
                                navController.navigateUp()
                            }
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = device.name ?: "Unknown Device",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RevikWhite
                            )
                            Text(
                                text = device.address,
                                fontSize = 12.sp,
                                color = CardLightGrey,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
