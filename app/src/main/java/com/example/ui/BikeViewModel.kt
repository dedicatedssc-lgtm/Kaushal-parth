package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ble.BleManager
import com.example.data.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BikeViewModel(application: Application) : AndroidViewModel(application) {
    private val bleManager = BleManager(application)
    private val preferences = AppPreferences(application)

    val scanResults = bleManager.scanResults
    val connectionState = bleManager.connectionState

    private val _devicePassword = MutableStateFlow("")
    val devicePassword: StateFlow<String> = _devicePassword.asStateFlow()

    private val _savedMac = MutableStateFlow<String?>(null)
    val savedMac: StateFlow<String?> = _savedMac.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.devicePassword.collectLatest { pwd ->
                _devicePassword.value = pwd ?: ""
            }
        }
        viewModelScope.launch {
            preferences.deviceMac.collectLatest { mac ->
                _savedMac.value = mac
                if (mac != null && connectionState.value == BleManager.ConnectionState.DISCONNECTED) {
                    bleManager.connect(mac)
                }
            }
        }
    }

    fun startScan() {
        bleManager.startScan()
    }

    fun stopScan() {
        bleManager.stopScan()
    }

    fun connect(address: String) {
        viewModelScope.launch {
            preferences.saveDeviceMac(address)
        }
        bleManager.connect(address)
    }

    fun disconnect() {
        viewModelScope.launch {
            preferences.clearDeviceMac()
        }
        bleManager.disconnect()
    }

    fun savePassword(password: String) {
        viewModelScope.launch {
            preferences.saveDevicePassword(password)
        }
    }

    fun sendCommand(command: String) {
        bleManager.sendCommand(_devicePassword.value, command)
    }
}
