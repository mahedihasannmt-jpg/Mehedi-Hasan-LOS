package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.TransactionEntity
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun FinanceScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val todayIncome by viewModel.todayIncome.collectAsState()
    val todayExpense by viewModel.todayExpense.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()

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
                        text = "💰 হিসাব-নিকাশ (Personal Finance)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ব্যক্তিগত ও ব্যবসায়িক আয়-ব্যয়ের ট্র্যাকার",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Total Balance Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "বর্তমান ব্যালেন্স (Current Balance)",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "৳$currentBalance",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ArrowUpward, contentDescription = "Income", tint = Color(0xFF10B981))
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("আজকের আয়", style = MaterialTheme.typography.labelSmall, color = Color(0xFFCBD5E1))
                                Text("৳$todayIncome", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Expense", tint = Color(0xFFEF4444))
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text("আজকের খরচ", style = MaterialTheme.typography.labelSmall, color = Color(0xFFCBD5E1))
                                Text("৳$todayExpense", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.setShowQuickAdd(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("লেনদেন যোগ")
                }
            }
        }

        // Transactions History
        item {
            Text(
                text = "সর্বশেষ লেনদেনসমূহ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    text = "কোন লেনদেন রেকর্ড পাওয়া যায়নি।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(transactions, key = { it.id }) { tx ->
                TransactionCard(tx = tx, onDelete = { viewModel.deleteTransaction(tx) })
            }
        }
    }
}

@Composable
fun TransactionCard(
    tx: TransactionEntity,
    onDelete: () -> Unit
) {
    val isIncome = tx.type == "Income"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isIncome) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = tx.type,
                    tint = if (isIncome) Color(0xFF10B981) else Color(0xFFEF4444)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (tx.note.isNotBlank()) {
                        Text(
                            text = tx.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = tx.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${if (isIncome) "+" else "-"}৳${tx.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) Color(0xFF10B981) else Color(0xFFEF4444)
            )
        }
    }
}
