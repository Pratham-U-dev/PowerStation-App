package com.example.domain.repository

import com.example.domain.models.BatteryData
import com.example.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow

interface PowerStationRepository {
    val connectionState: Flow<ConnectionState>
    val batteryData: Flow<BatteryData>
    
    fun startScanningAndConnect()
    fun disconnect()
    fun setEnergyReservation(wh: Int)
    fun unlockEnergyReservation()
}
