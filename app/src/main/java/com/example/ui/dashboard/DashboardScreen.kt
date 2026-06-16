package com.example.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import com.example.domain.engine.PredictionEngine
import com.example.domain.models.ConnectionState
import com.example.ui.PowerStationViewModel
import com.example.ui.components.BatteryGauge
import com.example.ui.theme.*

@Composable
fun DashboardScreen(viewModel: PowerStationViewModel) {
    val connectionState by viewModel.connectionState.collectAsState()
    val batteryData by viewModel.batteryData.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        TopBar(connectionState) {
            if (connectionState == ConnectionState.DISCONNECTED) viewModel.connect() else viewModel.disconnect()
        }

        Spacer(modifier = Modifier.height(16.dp))

        BatteryGauge(soc = batteryData.soc)

        Spacer(modifier = Modifier.height(24.dp))

        StatusCard(status = batteryData.status.name, reserved = batteryData.reservedEnergyWh)

        Spacer(modifier = Modifier.height(24.dp))

        MetricsGrid(
            voltage = batteryData.voltage,
            current = batteryData.current,
            temp = batteryData.temperature,
            power = batteryData.power
        )

        Spacer(modifier = Modifier.height(24.dp))

        PredictionsSection(
            currentLoadRuntime = PredictionEngine.calculateCurrentLoadRuntime(batteryData),
            laptopRuntime = PredictionEngine.calculateDeviceRuntime(batteryData, 60f),
            fanRuntime = PredictionEngine.calculateDeviceRuntime(batteryData, 30f),
            phoneCharges = PredictionEngine.calculateDeviceCharges(batteryData, 15f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val suggestions by viewModel.intelligenceSuggestions.collectAsState()
        if (suggestions.isNotEmpty()) {
            IntelligenceSection(suggestions)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun IntelligenceSection(suggestions: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Battery Intelligence",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                suggestions.forEach { suggestion ->
                    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = PowerYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = suggestion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(connectionState: ConnectionState, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "SYSTEM ONLINE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                color = Emerald500
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "DIY PowerStation v1",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        OutlinedButton(
            onClick = onToggle,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Zinc900.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(1.dp, if (connectionState == ConnectionState.CONNECTED) Emerald500.copy(alpha=0.3f) else Zinc800),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (connectionState == ConnectionState.CONNECTED) Emerald500 else Zinc500, androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = if (connectionState == ConnectionState.CONNECTED) "BLE Connected" else "BLE Disconnect", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun StatusCard(status: String, reserved: Int) {
    val isCharging = status == com.example.domain.models.BatteryStatus.CHARGING.name
    val color = if (reserved > 0) PowerRed else if (isCharging) Emerald400 else Zinc400
    val bgColor = if (reserved > 0) PowerRed.copy(alpha=0.1f) else Emerald500.copy(alpha=0.1f)
    val borderColor = if (reserved > 0) PowerRed.copy(alpha=0.3f) else Emerald500.copy(alpha=0.3f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(bgColor, androidx.compose.foundation.shape.CircleShape)
                .border(1.dp, borderColor, androidx.compose.foundation.shape.CircleShape)
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            val text = if (reserved > 0) "Reserved ${reserved}Wh" else "${status.replace("_", " ")}"
            Text(text = text, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold), color = color)
        }
    }
}

@Composable
fun MetricsGrid(voltage: Float, current: Float, temp: Float, power: Float) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricBox("Voltage", String.format("%.2f", voltage), "V", Modifier.weight(1f))
            MetricBox("Current", String.format("%.2f", current), "A", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricBox("Energy", String.format("%.1f", kotlin.math.abs(power)), "Wh", Modifier.weight(1f))
            MetricBox("Internal", String.format("%.1f", temp), "°C", Modifier.weight(1f))
        }
    }
}

@Composable
fun MetricBox(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(DarkSurface, RoundedCornerShape(16.dp))
            .border(1.dp, Zinc800, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = Zinc500
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = Zinc400,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun PredictionsSection(currentLoadRuntime: String, laptopRuntime: String, fanRuntime: String, phoneCharges: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RUNTIME PREDICTIONS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = Zinc400
            )
            Text(
                text = "EKF Optimized",
                style = MaterialTheme.typography.labelSmall,
                color = Emerald500
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PredictionBox("Total Load", currentLoadRuntime, true)
            }
            item {
                PredictionBox("Laptop Charge", laptopRuntime, false)
            }
            item {
                PredictionBox("Fan Runtime", fanRuntime, false)
            }
        }
    }
}

@Composable
fun PredictionBox(title: String, value: String, isPrimary: Boolean) {
    val bgColor = if (isPrimary) Emerald600.copy(alpha = 0.1f) else DarkSurface
    val borderColor = if (isPrimary) Emerald500.copy(alpha = 0.2f) else Zinc800
    val titleColor = if (isPrimary) Emerald400 else Zinc400

    Box(
        modifier = Modifier
            .width(130.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
