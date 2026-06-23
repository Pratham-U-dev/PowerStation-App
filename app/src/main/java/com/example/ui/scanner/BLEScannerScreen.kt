package com.example.ui.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.models.BleDeviceInfo
import com.example.domain.models.ConnectionState
import com.example.ui.PowerStationViewModel
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Zinc400
import com.example.ui.theme.Zinc800

@Composable
fun BLEScannerScreen(viewModel: PowerStationViewModel, onConnected: () -> Unit) {
    val connectionState by viewModel.connectionState.collectAsState()
    val scanResults by viewModel.scanResults.collectAsState()

    LaunchedEffect(connectionState) {
        if (connectionState == ConnectionState.CONNECTED) {
            onConnected()
        }
        if (connectionState == ConnectionState.SCANNING) {
            kotlinx.coroutines.delay(10000L)
            if (viewModel.connectionState.value == ConnectionState.SCANNING) {
                viewModel.stopScan()
            }
        }
    }

    LaunchedEffect(scanResults, connectionState) {
        if (connectionState == ConnectionState.SCANNING) {
            val matching = scanResults.filter { it.isPowerStation }
            if (matching.size == 1) {
                viewModel.connectToAddress(matching.first().address)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Icon(
            imageVector = Icons.Default.Bluetooth,
            contentDescription = null,
            tint = Emerald400,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Connect to PowerStation",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select your device from the list below",
            style = MaterialTheme.typography.bodyMedium,
            color = Zinc400
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        if (connectionState == ConnectionState.SCANNING) {
            CircularProgressIndicator(color = Emerald500, modifier = Modifier.size(48.dp))
        } else if (connectionState == ConnectionState.CONNECTING) {
            Text("Connecting...", color = Emerald500, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Emerald500, modifier = Modifier.size(48.dp))
        } else {
            Button(
                onClick = { viewModel.startScan() },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Scan for Devices", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (connectionState == ConnectionState.SCANNING && scanResults.isEmpty()) {
            Text("Scanning for devices...", color = Zinc400, style = MaterialTheme.typography.bodyMedium)
        } else if (connectionState != ConnectionState.SCANNING && connectionState != ConnectionState.CONNECTING && scanResults.isEmpty()) {
            Text("No devices found. Tap Scan to try again.", color = Zinc400, style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Show matching devices first
                val sortedResults = scanResults.sortedByDescending { it.isPowerStation }
                
                items(sortedResults) { device ->
                    DeviceRow(device = device, onClick = {
                         if (connectionState != ConnectionState.CONNECTING) {
                             viewModel.connectToAddress(device.address)
                         }
                    })
                }
            }
        }
    }
}

@Composable
fun DeviceRow(device: BleDeviceInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = device.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Zinc400
                )
            }
            if (device.isPowerStation) {
                Badge(
                    containerColor = Emerald500.copy(alpha = 0.2f),
                    contentColor = Emerald400,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text("PowerStation", modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${device.rssi} dBm",
                    style = MaterialTheme.typography.labelSmall,
                    color = Zinc400
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.SignalWifi4Bar,
                    contentDescription = "Signal",
                    tint = Zinc400,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
