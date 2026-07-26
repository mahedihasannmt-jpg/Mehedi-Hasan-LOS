package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.LifeOsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddSheet(
    viewModel: LifeOsViewModel,
    onDismiss: () -> Unit
) {
    var selectedType by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedType == null) {
                Text(
                    text = "Quick Add (দ্রুত নতুন কিছু যোগ করুন)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickAddOption(
                        label = "Task",
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF2563EB),
                        onClick = { selectedType = "TASK" }
                    )
                    QuickAddOption(
                        label = "Routine",
                        icon = Icons.Default.Schedule,
                        color = Color(0xFFF97316),
                        onClick = { selectedType = "ROUTINE" }
                    )
                    QuickAddOption(
                        label = "Income",
                        icon = Icons.Default.ArrowUpward,
                        color = Color(0xFF10B981),
                        onClick = { selectedType = "INCOME" }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickAddOption(
                        label = "Expense",
                        icon = Icons.Default.ArrowDownward,
                        color = Color(0xFFEF4444),
                        onClick = { selectedType = "EXPENSE" }
                    )
                    QuickAddOption(
                        label = "Goal",
                        icon = Icons.Default.Flag,
                        color = Color(0xFF8B5CF6),
                        onClick = { selectedType = "GOAL" }
                    )
                    QuickAddOption(
                        label = "Note",
                        icon = Icons.Default.Notes,
                        color = Color(0xFF64748B),
                        onClick = { selectedType = "NOTE" }
                    )
                }
            } else {
                // Form based on selection
                when (selectedType) {
                    "TASK" -> QuickAddTaskForm(viewModel = viewModel, onComplete = onDismiss)
                    "ROUTINE" -> QuickAddRoutineForm(viewModel = viewModel, onComplete = onDismiss)
                    "INCOME" -> QuickAddTransactionForm(viewModel = viewModel, isIncome = true, onComplete = onDismiss)
                    "EXPENSE" -> QuickAddTransactionForm(viewModel = viewModel, isIncome = false, onComplete = onDismiss)
                    "GOAL" -> QuickAddGoalForm(viewModel = viewModel, onComplete = onDismiss)
                    "NOTE" -> QuickAddNoteForm(viewModel = viewModel, onComplete = onDismiss)
                }
            }
        }
    }
}

@Composable
fun QuickAddOption(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color.copy(alpha = 0.15f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun QuickAddTaskForm(viewModel: LifeOsViewModel, onComplete: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("🔴 Must Do") }
    var category by remember { mutableStateOf("Work") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("নতুন Task যোগ করুন", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task এর শিরোনাম") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("প্রায়োরিটি:", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("🔴 Must Do", "🟡 Should Do", "🟢 If Time").forEach { p ->
                FilterChip(
                    selected = priority == p,
                    onClick = { priority = p },
                    label = { Text(p, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("ক্যাটাগরি:", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Deen", "Study", "Work", "Business", "Marketing").forEach { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { category = cat },
                    label = { Text(cat, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.addTask(title = title, category = category, priority = priority)
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("সংরক্ষণ করুন")
        }
    }
}

@Composable
fun QuickAddRoutineForm(viewModel: LifeOsViewModel, onComplete: () -> Unit) {
    var activity by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("08:00") }
    var category by remember { mutableStateOf("💼 Work") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("নতুন Routine যোগ করুন", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = activity,
            onValueChange = { activity = it },
            label = { Text("কার্যক্রম / কাজ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("সময় (যেমন 09:00)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (activity.isNotBlank()) {
                    viewModel.addRoutine(activity = activity, time = time, duration = 30, category = category)
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("রুটিনে যোগ করুন")
        }
    }
}

@Composable
fun QuickAddTransactionForm(viewModel: LifeOsViewModel, isIncome: Boolean, onComplete: () -> Unit) {
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(if (isIncome) "Fatafat Income" else "Personal Expense") }
    var note by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isIncome) "আয় যোগ করুন (+৳)" else "খরচ যোগ করুন (-৳)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) Color(0xFF10B981) else Color(0xFFEF4444)
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("টাকার পরিমাণ (৳)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("খাত / ক্যাটাগরি") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("নোট (ঐচ্ছিক)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val amt = amountText.toDoubleOrNull() ?: 0.0
                if (amt > 0) {
                    viewModel.addTransaction(
                        type = if (isIncome) "Income" else "Expense",
                        amount = amt,
                        category = category,
                        note = note
                    )
                    onComplete()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = if (isIncome) Color(0xFF10B981) else Color(0xFFEF4444)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("সংরক্ষণ করুন")
        }
    }
}

@Composable
fun QuickAddGoalForm(viewModel: LifeOsViewModel, onComplete: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("নতুন Goal (লক্ষ্য) যোগ করুন", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("লক্ষ্যের শিরোনাম") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("সংক্ষিপ্ত বিবরণ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.addGoal(title = title, description = description, deadline = "2026-12-31")
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("গোল সেভ করুন")
        }
    }
}

@Composable
fun QuickAddNoteForm(viewModel: LifeOsViewModel, onComplete: () -> Unit) {
    var noteText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("দ্রুত নোট লিখুন", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("আপনার নোট...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (noteText.isNotBlank()) {
                    viewModel.addTask(
                        title = noteText,
                        category = "Personal",
                        priority = "🟢 If Time",
                        notes = "Quick note"
                    )
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("নোট সেভ করুন")
        }
    }
}
