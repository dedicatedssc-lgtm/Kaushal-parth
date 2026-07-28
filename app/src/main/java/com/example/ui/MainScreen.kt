package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import com.example.ui.icons.RevikIcons_Motorbike
import com.example.ui.icons.RevikIcons_Headlight
import com.example.ui.components.RevikLogo
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MainScreen(navController: NavController, viewModel: BikeViewModel) {
    var isUnlocked by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Ride, 1 = Lights

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
        Column(modifier = Modifier.fillMaxSize()) {
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

                // Settings and Status Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = RevikWhite,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { navController.navigate("settings") }
                    )

                    val pillBgColor = CardLightGrey
                    val pillTextColor = if (isUnlocked) RevikLimeGreen else RevikBlue

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(pillBgColor)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isUnlocked) "Online" else "Offline",
                            color = pillTextColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.85f))

            // Center Content
            if (selectedTab == 0) {
                // Ride Tab Content - Main Control
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val cardSize = 250.dp
                    val trackWidth = 64.dp
                    val density = LocalDensity.current
                    val maxOffset = with(density) { (cardSize - trackWidth).toPx() }
                    
                    var offsetX by remember { mutableFloatStateOf(if (isUnlocked) maxOffset else 0f) }
                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier
                            .size(cardSize)
                            .shadow(16.dp, RoundedCornerShape(24.dp))
                            .background(CardLightGrey, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        // Text in background
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.offset(x = if (isUnlocked) (-30).dp else 30.dp)
                            ) {
                                Text(
                                    text = if (isUnlocked) "ON" else "OFF",
                                    color = RevikBlack,
                                    fontSize = 56.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = if (isUnlocked) "Hold to lock" else "Hold to unlock",
                                    color = TextDarkGrey,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Draggable Track
                        val animatedOffsetX by animateFloatAsState(
                            targetValue = offsetX,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                        )
                        val trackColor by animateColorAsState(
                            targetValue = if (isUnlocked) RevikLimeGreen else SliderOffBlue
                        )

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                                .fillMaxHeight()
                                .width(trackWidth)
                                .background(trackColor)
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        val newOffset = offsetX + delta
                                        offsetX = newOffset.coerceIn(0f, maxOffset)
                                    },
                                    onDragStopped = {
                                        scope.launch {
                                            if (offsetX > maxOffset / 2) {
                                                offsetX = maxOffset
                                                isUnlocked = true
                                            } else {
                                                offsetX = 0f
                                                isUnlocked = false
                                            }
                                        }
                                    }
                                )
                        ) {
                            // Grips
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp, 40.dp)
                                            .background(RevikWhite.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Lights Control Screen - Empty
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Empty center for future light control elements
                }
            }

            Spacer(modifier = Modifier.weight(1.45f))

            // Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .padding(horizontal = 48.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(RevikLimeGreen, RoundedCornerShape(32.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ride Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedTab == 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .background(RevikDeepPurple, RoundedCornerShape(32.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = RevikIcons_Motorbike,
                                    contentDescription = "Ride",
                                    tint = RevikBlack,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = RevikIcons_Motorbike,
                                contentDescription = "Ride",
                                tint = RevikBlack,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(0.6f)
                            .background(RevikWhite)
                    )

                    // Lights Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedTab == 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .background(RevikDeepPurple, RoundedCornerShape(32.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = RevikIcons_Headlight,
                                    contentDescription = "Lights",
                                    tint = RevikBlack,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = RevikIcons_Headlight,
                                contentDescription = "Lights",
                                tint = RevikBlack,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Bottom Left Text
        val pageText = if (selectedTab == 1) "PAGE 4 lights" else if (isUnlocked) "PAGE 3 on" else "PAGE 2 off"
        Text(
            text = pageText,
            fontSize = 12.sp,
            color = RevikWhite,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 12.dp)
        )
    }
}

