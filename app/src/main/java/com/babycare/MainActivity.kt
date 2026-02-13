package com.babycare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.babycare.ui.screens.*
import com.babycare.ui.theme.BabyCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BabyCareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BabyCareApp()
                }
            }
        }
    }
}

@Composable
fun BabyCareApp() {
    var currentScreen by remember { mutableStateOf("home") }
    var showQuickMenu by remember { mutableStateOf(false) }

    // 主界面框架
    when (currentScreen) {
        "home" -> HomeScreen(
            onAddClick = { showQuickMenu = true },
            onRecordClick = { /* 查看记录详情 */ }
        )
        "calendar" -> CalendarScreen(
            onChartClick = { currentScreen = "growth_chart" }
        )
        "discover" -> {
            // 发现页面
            Text("发现页面")
        }
        "profile" -> {
            // 我的页面
            Text("我的页面")
        }
        "diaper" -> DiaperScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "sleep" -> SleepTimerScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "growth" -> GrowthRecordScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "bottle" -> BottleFeedingScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "temperature" -> TemperatureScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "food" -> FoodScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        "vaccine" -> VaccineScreen(
            onBackClick = { currentScreen = "home" },
            onAddVaccineClick = { }
        )
        "growth_chart" -> GrowthChartScreen(
            onBackClick = { currentScreen = "calendar" },
            onRecordListClick = { currentScreen = "growth" }
        )
        "activity" -> ActivityTimerScreen(
            onBackClick = { currentScreen = "home" },
            onSaveClick = { currentScreen = "home" }
        )
        else -> HomeScreen(
            onAddClick = { showQuickMenu = true },
            onRecordClick = { }
        )
    }

    // 底部导航（只在主页面显示）
    if (currentScreen in listOf("home", "calendar", "discover", "profile")) {
        BottomNavBar(
            currentRoute = currentScreen,
            onItemSelected = { currentScreen = it },
            onAddClick = { showQuickMenu = true }
        )
    }

    // 快速添加菜单
    if (showQuickMenu) {
        QuickAddMenu(
            isVisible = showQuickMenu,
            onDismiss = { showQuickMenu = false },
            onItemClick = { item ->
                showQuickMenu = false
                currentScreen = when (item) {
                    "growth" -> "growth"
                    "temperature" -> "temperature"
                    "food" -> "food"
                    "vaccine" -> "vaccine"
                    "bottle" -> "bottle"
                    "sleep" -> "sleep"
                    "diaper" -> "diaper"
                    "activity" -> "activity"
                    else -> "home"
                }
            }
        )
    }
}
