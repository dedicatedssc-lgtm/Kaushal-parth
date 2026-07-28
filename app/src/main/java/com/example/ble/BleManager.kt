package com.example.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

@SuppressLint("MissingPermission")
class BleManager(private val context: Context) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var bluetoothGatt: BluetoothGatt? = null
    private var writeCharacteristic: BluetoothGattCharacteristic? = null

    private val _scanResults = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val scanResults: StateFlow<List<BluetoothDevice>> = _scanResults.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    enum class ConnectionState {
        DISCONNECTED, CONNECTING, CONNECTED
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            if (device.name != null) {
                val currentList = _scanResults.value.toMutableList()
                if (currentList.none { it.address == device.address }) {
                    currentList.add(device)
                    _scanResults.value = currentList
                }
            }
        }
        
        override fun onScanFailed(errorCode: Int) {
            Log.e("BleManager", "Scan failed: $errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    _connectionState.value = ConnectionState.CONNECTED
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    _connectionState.value = ConnectionState.DISCONNECTED
                    bluetoothGatt?.close()
                    bluetoothGatt = null
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"))
                writeCharacteristic = service?.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"))
            }
        }
    }

    fun startScan() {
        _scanResults.value = emptyList()
        bluetoothLeScanner?.startScan(scanCallback)
    }

    fun stopScan() {
        bluetoothLeScanner?.stopScan(scanCallback)
    }

    fun connect(address: String) {
        val device = bluetoothAdapter?.getRemoteDevice(address)
        device?.let {
            _connectionState.value = ConnectionState.CONNECTING
            bluetoothGatt = it.connectGatt(context, false, gattCallback)
        }
    }

    fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    fun sendCommand(password: String, command: String) {
        val char = writeCharacteristic ?: return
        val payload = "$password:$command"
        char.value = payload.toByteArray(Charsets.UTF_8)
        bluetoothGatt?.writeCharacteristic(char)
    }
}
