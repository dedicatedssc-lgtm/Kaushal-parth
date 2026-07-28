package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.BikeViewModel
import com.example.ui.DevicesScreen
import com.example.ui.MainScreen
import com.example.ui.SettingsScreen
import com.example.ui.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalPermissionsApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
          )
        } else {
          listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
          )
        }
                
        val permissionState = rememberMultiplePermissionsState(permissions = permissions)
                
        LaunchedEffect(Unit) {
          permissionState.launchMultiplePermissionRequest()
        }

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          if (permissionState.allPermissionsGranted) {
            val navController = rememberNavController()
            val viewModel: BikeViewModel = viewModel()
                        
            NavHost(navController = navController, startDestination = "splash") {
              composable("splash") {
                SplashScreen(navController)
              }
              composable("main") {
                MainScreen(navController, viewModel)
              }
              composable("devices") {
                DevicesScreen(navController, viewModel)
              }
              composable("settings") {
                SettingsScreen(navController, viewModel)
              }
            }
          }
        }
      }
    }
  }
}
