package com.example.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("BATTERY CONFIGURATION", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem("Battery Capacity (Ah)", "100 Ah")
        SettingsItem("Nominal Voltage", "12.8 V")
        SettingsItem("Battery Chemistry", "LiFePO4")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("THRESHOLDS & LIMITS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem("SOC Warning Level", "30%")
        SettingsItem("Critical Battery Level", "10%")
        SettingsItem("Temperature Warning Limit", "45 °C")
        SettingsItem("Temperature Shutdown Limit", "60 °C")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("DEFAULTS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        SettingsItem("Energy Reservation Default", "50 Wh")
    }
}

@Composable
fun SettingsItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
}
