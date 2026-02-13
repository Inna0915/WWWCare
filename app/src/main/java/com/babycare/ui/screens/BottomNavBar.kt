package com.babycare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babycare.ui.theme.*

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "首页", Icons.Default.Home)
    object Calendar : BottomNavItem("calendar", "日历", Icons.Default.CalendarToday)
    object Add : BottomNavItem("add", "", Icons.Default.Add)
    object Discover : BottomNavItem("discover", "发现", Icons.Outlined.Explore)
    object Profile : BottomNavItem("profile", "我的", Icons.Default.Person)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Calendar,
        BottomNavItem.Add,
        BottomNavItem.Discover,
        BottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                when (item) {
                    is BottomNavItem.Add -> {
                        // 中间的添加按钮
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = CircleShape,
                                    spotColor = PrimaryPink.copy(alpha = 0.4f)
                                )
                                .clip(CircleShape)
                                .background(PrimaryPink)
                                .clickable(onClick = onAddClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    else -> {
                        val isSelected = currentRoute == item.route
                        val color = if (isSelected) PrimaryPink else TextSecondary

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onItemSelected(item.route) }
                                .padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = color,
                                modifier = Modifier.size(24.dp)
                            )
                            if (item.title.isNotEmpty()) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = color,
                                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
