package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun SearchScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState()
    val routines by viewModel.routines.collectAsState()
    val goals by viewModel.goals.collectAsState()

    val filteredTasks = tasks.filter { query.isNotBlank() && it.title.contains(query, ignoreCase = true) }
    val filteredRoutines = routines.filter { query.isNotBlank() && it.activity.contains(query, ignoreCase = true) }
    val filteredGoals = goals.filter { query.isNotBlank() && it.title.contains(query, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("খুঁজুন... (যেমন: ফাতাফাত, পড়া, সালাত)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (query.isBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "খুঁজতে টাইপ করুন...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredTasks.isNotEmpty()) {
                    item { Text("টাস্কসমূহ (${filteredTasks.size})", fontWeight = FontWeight.Bold) }
                    items(filteredTasks, key = { "t_${it.id}" }) { task ->
                        TaskHomeCard(task = task, onToggle = { viewModel.toggleTask(task) })
                    }
                }

                if (filteredRoutines.isNotEmpty()) {
                    item { Text("রুটিনসমূহ (${filteredRoutines.size})", fontWeight = FontWeight.Bold) }
                    items(filteredRoutines, key = { "r_${it.id}" }) { routine ->
                        RoutineTimelineCard(routine = routine, onToggle = { viewModel.toggleRoutine(routine) })
                    }
                }

                if (filteredGoals.isNotEmpty()) {
                    item { Text("লক্ষ্যসমূহ (${filteredGoals.size})", fontWeight = FontWeight.Bold) }
                    items(filteredGoals, key = { "g_${it.id}" }) { goal ->
                        GoalCard(goal = goal)
                    }
                }
            }
        }
    }
}
