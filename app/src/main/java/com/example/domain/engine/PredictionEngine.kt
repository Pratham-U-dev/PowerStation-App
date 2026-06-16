package com.example.domain.engine

import com.example.domain.models.BatteryData

object PredictionEngine {
    // Current load runtime
    fun calculateCurrentLoadRuntime(data: BatteryData): String {
        val power = data.power
        if (power >= -1f) return "Infinite (No Load)" // Discharging is negative current, or if power is very low. Wait, if discharging is negative, we should use absolute. 
        // Let's assume current is positive when discharging for the UI, or just use absolute.
        val absPower = kotlin.math.abs(power)
        if (absPower < 1f) return "--"
        
        val hoursRemaining = data.remainingEnergyWh / absPower
        return formatTime(hoursRemaining)
    }

    // Specific device runtimes
    fun calculateDeviceRuntime(data: BatteryData, devicePowerWatts: Float): String {
        if (data.remainingEnergyWh <= 0f) return "0 Hours"
        val hoursRemaining = data.remainingEnergyWh / devicePowerWatts
        return formatTime(hoursRemaining)
    }

    // Device charing (Times)
    fun calculateDeviceCharges(data: BatteryData, deviceCapacityWh: Float): String {
        if (data.remainingEnergyWh <= 0f) return "0 Times"
        val charges = data.remainingEnergyWh / deviceCapacityWh
        return String.format("%.1f Times", charges)
    }

    private fun formatTime(hoursDecimal: Float): String {
        val totalMinutes = (hoursDecimal * 60).toLong()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        if (hours > 0) {
            return "$hours Hours $minutes Minutes"
        }
        return "$minutes Minutes"
    }

    fun getIntelligenceSuggestions(data: BatteryData): List<String> {
        val suggestions = mutableListOf<String>()
        
        if (data.temperature > 40f) {
            suggestions.add("Battery temperature is high. Consider reducing load or improving ventilation.")
        } else if (data.temperature in 15f..35f) {
            suggestions.add("Battery temperature is optimal.")
        }

        if (data.soc > 80) {
            suggestions.add("Battery is healthy and sufficiently charged.")
        } else if (data.soc < 20) {
            suggestions.add("Battery should be recharged soon. Runtime is limited.")
        }

        if (data.power < -200f) { // High discharge
            suggestions.add("High load detected. This may exhaust the battery quickly.")
        }

        suggestions.add("Can charge a typical smartphone (15Wh) ~${calculateDeviceCharges(data, 15f).replace(" Times", "")} times.")

        return suggestions
    }
}
