package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.QuickAddSheet
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LifeOsViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                LifeOsMainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LifeOsMainContent(viewModel: LifeOsViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentMoreScreen by viewModel.currentMoreScreen.collectAsState()
    val showQuickAddSheet by viewModel.showQuickAddSheet.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.setTab(tab) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowQuickAdd(true) },
                containerColor = OrangePrimary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Quick Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                currentMoreScreen != null -> {
                    when (currentMoreScreen) {
                        "deen" -> DeenAmalScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "finance" -> FinanceScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "study" -> StudyLearningScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "work" -> WorkHubScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "habits" -> HabitsScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "review" -> WeeklyReviewScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "ai" -> AIAssistantScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "search" -> SearchScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                        "settings" -> SettingsScreen(viewModel = viewModel, onBack = { viewModel.closeMoreSubScreen() })
                    }
                }
                currentTab == "home" -> HomeScreen(viewModel = viewModel)
                currentTab == "routine" -> RoutineScreen(viewModel = viewModel)
                currentTab == "tasks" -> TasksScreen(viewModel = viewModel)
                currentTab == "goals" -> GoalsProjectsScreen(viewModel = viewModel)
                currentTab == "more" -> MoreHubScreen(viewModel = viewModel, onNavigate = { sub -> viewModel.openMoreSubScreen(sub) })
            }

            if (showQuickAddSheet) {
                QuickAddSheet(
                    viewModel = viewModel,
                    onDismiss = { viewModel.setShowQuickAdd(false) }
                )
            }
        }
    }
}
