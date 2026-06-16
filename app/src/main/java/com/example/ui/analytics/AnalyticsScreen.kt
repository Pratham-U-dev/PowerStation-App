package com.example.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Zinc800
import com.example.ui.theme.Zinc900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Analytics",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text("1 Day") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Emerald500.copy(alpha = 0.2f), selectedLabelColor = Emerald400)
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text("7 Day") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text("30 Day") }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Placeholder for Beautiful Custom Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(DarkSurface, RoundedCornerShape(20.dp))
                .border(1.dp, Zinc800, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("24 Hour Power Trajectory", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    val dx = size.width / 10
                    path.moveTo(0f, size.height * 0.8f)
                    path.cubicTo(dx * 2, size.height * 0.8f, dx * 3, size.height * 0.2f, dx * 5, size.height * 0.5f)
                    path.cubicTo(dx * 7, size.height * 0.8f, dx * 8, size.height * 0.9f, size.width, size.height * 0.3f)
                    
                    drawPath(
                        path = path,
                        color = Emerald500,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(DarkSurface, RoundedCornerShape(20.dp))
                .border(1.dp, Zinc800, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("SOC % Trend", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(16.dp))
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    path.moveTo(0f, size.height * 0.2f)
                    path.lineTo(size.width * 0.3f, size.height * 0.3f)
                    path.lineTo(size.width * 0.6f, size.height * 0.8f)
                    path.lineTo(size.width, size.height * 0.9f)
                    
                    drawPath(
                        path = path,
                        color = Indigo400,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
    }
}
