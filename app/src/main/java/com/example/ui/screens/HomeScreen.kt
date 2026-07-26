package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskEntity
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(viewModel: LifeOsViewModel) {
    val userName by viewModel.userName.collectAsState()
    val progressPercent by viewModel.todayProgressPercent.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val routines by viewModel.routines.collectAsState()
    val todayIncome by viewModel.todayIncome.collectAsState()
    val todayExpense by viewModel.todayExpense.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 4..11 -> "শুভ সকাল"
        in 12..16 -> "শুভ অপরাহ্ন"
        in 17..20 -> "শুভ সন্ধ্যা"
        else -> "শুভ রাত্রি"
    }

    val todayFormatted = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("bn", "BD"))
        sdf.format(Date())
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "progressAnimation"
    )

    val mustDoTasks = tasks.filter { it.priority == "🔴 Must Do" && it.status == "Pending" }.take(3)
    val shouldDoTasks = tasks.filter { it.priority == "🟡 Should Do" && it.status == "Pending" }.take(2)
    val completedCount = tasks.count { it.status == "Completed" }

    val nextRoutine = routines.firstOrNull { !it.isCompleted } ?: routines.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Dynamic Greeting & Date ---
        item {
            Column {
                Text(
                    text = "আসসালামু আলাইকুম, $userName!",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$greeting, আজ $todayFormatted",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = "Quote",
                            tint = OrangePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "আজকের দিনটা আল্লাহর দেওয়া একটি নতুন সুযোগ।",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // --- 2. Overall Progress Card ---
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(DarkNavy, Color(0xFF1E293B))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "আজকের Overall Progress",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color(0xFF94A3B8)
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "$progressPercent%",
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "আলহামদুলিল্লাহ",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = OrangePrimary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = OrangePrimary.copy(alpha = 0.2f),
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Bolt",
                                        tint = OrangePrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = OrangePrimary,
                            trackColor = Color(0xFF334155)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "সম্পন্ন হয়েছে: $completedCount টি টাস্ক ও রুটিন",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }

        // --- 3. Quick Score & Category Cards ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "ক্যাটাগরি ভিত্তিক অগ্রগতি",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryScoreCard(
                        title = "🕌 Deen",
                        status = "4/5 সালাত",
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    CategoryScoreCard(
                        title = "📚 Study",
                        status = "2.5 ঘণ্টা",
                        color = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryScoreCard(
                        title = "💼 Work",
                        status = "অর্ডার সিঙ্ক",
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    CategoryScoreCard(
                        title = "📈 Score",
                        status = "$progressPercent% স্কোর",
                        color = OrangePrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- 4. 🔴 Must Do Today Section ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔴 Must Do Today (জরুরী কাজ)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.setTab("tasks") }) {
                    Text("সব দেখুন", color = OrangePrimary)
                }
            }
        }

        if (mustDoTasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Complete",
                            tint = Color(0xFF10B981)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "মাশাআল্লাহ! আজকের জরুরী কাজগুলো সম্পন্ন হয়েছে।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF065F46),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            items(mustDoTasks, key = { it.id }) { task ->
                TaskHomeCard(task = task, onToggle = { viewModel.toggleTask(task) })
            }
        }

        // --- 5. 🟡 Should Do Preview ---
        if (shouldDoTasks.isNotEmpty()) {
            item {
                Text(
                    text = "🟡 Should Do (গুরুত্বপূর্ণ)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            items(shouldDoTasks, key = { it.id }) { task ->
                TaskHomeCard(task = task, onToggle = { viewModel.toggleTask(task) })
            }
        }

        // --- 6. ⏰ Next Up Routine ---
        if (nextRoutine != null) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "⏰ পরবর্তী রুটিন",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFDBEAFE),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = nextRoutine.activity,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "সময়: ${nextRoutine.startTime} • ক্যাটাগরি: ${nextRoutine.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFBFDBFE)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleRoutine(nextRoutine) },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White)
                        ) {
                            Icon(
                                imageVector = if (nextRoutine.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Toggle Routine",
                                tint = if (nextRoutine.isCompleted) Color(0xFF10B981) else Color(0xFF2563EB)
                            )
                        }
                    }
                }
            }
        }

        // --- 7. 💰 Today's Money Overview ---
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openMoreSubScreen("finance") }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = OrangePrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💰 আজকের হিসাব (Finance)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Details",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MoneyItem(label = "আজকের আয়", amount = "৳$todayIncome", color = Color(0xFF10B981))
                        MoneyItem(label = "আজকের খরচ", amount = "৳$todayExpense", color = Color(0xFFEF4444))
                        MoneyItem(label = "মোট ব্যালেন্স", amount = "৳$currentBalance", color = OrangePrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryScoreCard(
    title: String,
    status: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TaskHomeCard(
    task: TaskEntity,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.status == "Completed") MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status == "Completed",
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = OrangePrimary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.status == "Completed") TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = OrangePrimary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = task.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = OrangePrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (task.dueTime.isNotBlank()) {
                        Text(
                            text = "⏰ ${task.dueTime}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoneyItem(label: String, amount: String, color: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
