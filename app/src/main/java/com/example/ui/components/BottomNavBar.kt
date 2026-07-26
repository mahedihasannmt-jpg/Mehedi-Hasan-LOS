package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == "home",
            onClick = { onTabSelected("home") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "home") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) }
        )

        NavigationBarItem(
            selected = currentTab == "routine",
            onClick = { onTabSelected("routine") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "routine") Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday,
                    contentDescription = "Routine"
                )
            },
            label = { Text("Routine", style = MaterialTheme.typography.labelSmall) }
        )

        NavigationBarItem(
            selected = currentTab == "tasks",
            onClick = { onTabSelected("tasks") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "tasks") Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    contentDescription = "Tasks"
                )
            },
            label = { Text("Tasks", style = MaterialTheme.typography.labelSmall) }
        )

        NavigationBarItem(
            selected = currentTab == "goals",
            onClick = { onTabSelected("goals") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "goals") Icons.Filled.Flag else Icons.Outlined.Flag,
                    contentDescription = "Goals"
                )
            },
            label = { Text("Goals", style = MaterialTheme.typography.labelSmall) }
        )

        NavigationBarItem(
            selected = currentTab == "more",
            onClick = { onTabSelected("more") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "more") Icons.Filled.Menu else Icons.Outlined.Menu,
                    contentDescription = "More"
                )
            },
            label = { Text("More", style = MaterialTheme.typography.labelSmall) }
        )
    }
}
