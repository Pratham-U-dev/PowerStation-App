package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.analytics.AnalyticsScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.health.HealthScreen
import com.example.ui.reservation.ReservationScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.BottomNavBg
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Zinc500

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Reservation : Screen("reservation", "Reserve", Icons.Filled.LockClock)
    object Health : Screen("health", "Health", Icons.Filled.HealthAndSafety)
    object Analytics : Screen("analytics", "Analytics", Icons.Filled.Analytics)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

val items = listOf(
    Screen.Dashboard,
    Screen.Analytics,
    Screen.Health,
    Screen.Reservation,
    Screen.Settings
)

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: PowerStationViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = BottomNavBg,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Emerald500,
                            selectedTextColor = Emerald500,
                            indicatorColor = Emerald500.copy(alpha = 0.1f),
                            unselectedIconColor = Zinc500,
                            unselectedTextColor = Zinc500
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Health.route) { HealthScreen(viewModel) }
            composable(Screen.Reservation.route) { ReservationScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
