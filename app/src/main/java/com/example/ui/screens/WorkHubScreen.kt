package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun WorkHubScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    var selectedWorkspace by remember { mutableStateOf("Fatafat") }
    val tasks by viewModel.tasks.collectAsState()
    val projects by viewModel.projects.collectAsState()

    val workspaceProjects = projects.filter { it.workspace == selectedWorkspace }
    val workspaceTasks = tasks.filter { it.project.contains(selectedWorkspace, ignoreCase = true) || it.category == "Work" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "💼 কর্মক্ষেত্র (Work & Business Hub)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fatafat অ্যাডমিন, মেটা মার্কেটিং ও বিজনেস ওয়ার্কস্পেস",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Workspace Selector
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = selectedWorkspace == "Fatafat",
                    onClick = { selectedWorkspace = "Fatafat" },
                    label = { Text("Fatafat Admin & Delivery", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = OrangePrimary, selectedLabelColor = Color.White)
                )
                FilterChip(
                    selected = selectedWorkspace == "Marketing",
                    onClick = { selectedWorkspace = "Marketing" },
                    label = { Text("Marketing & Ads", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = OrangePrimary, selectedLabelColor = Color.White)
                )
            }
        }

        item {
            Text(text = "$selectedWorkspace প্রজেক্টসমূহ", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(workspaceProjects, key = { it.id }) { project ->
            ProjectCard(project = project)
        }

        item {
            Text(text = "$selectedWorkspace সাম্প্রতিক টাস্কসমূহ", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(workspaceTasks, key = { it.id }) { task ->
            TaskHomeCard(task = task, onToggle = { viewModel.toggleTask(task) })
        }
    }
}
