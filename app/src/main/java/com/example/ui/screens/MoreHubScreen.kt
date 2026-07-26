package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.LifeOsViewModel

data class MoreMenuItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun MoreHubScreen(
    viewModel: LifeOsViewModel,
    onNavigate: (String) -> Unit
) {
    val menuItems = listOf(
        MoreMenuItem("deen", "🕌 Deen & Amal", "দ্বীন, সালাত, কুরআন ও জিকির", Icons.Default.Mosque, Color(0xFF10B981)),
        MoreMenuItem("study", "📚 Study & Learning", "মুতালাআ, কোর্স ও শিক্ষা", Icons.Default.MenuBook, Color(0xFF2563EB)),
        MoreMenuItem("work", "💼 Work Hub", "Fatafat ও Marketing কাজ", Icons.Default.Work, Color(0xFFF97316)),
        MoreMenuItem("finance", "💰 Finance", "আয়, খরচ ও সঞ্চয় (৳)", Icons.Default.AccountBalanceWallet, Color(0xFF059669)),
        MoreMenuItem("habits", "⚡ Habit Tracker", "দৈনিক অভ্যাস ও স্ট্রিক", Icons.Default.OfflineBolt, Color(0xFFD97706)),
        MoreMenuItem("review", "📊 Weekly Review", "সাপ্তাহিক পর্যালোচনা ও প্ল্যান", Icons.Default.BarChart, Color(0xFF8B5CF6)),
        MoreMenuItem("ai", "🤖 AI Assistant", "Gemini দিয়ে জীবন সাজান", Icons.Default.Psychology, Color(0xFFEC4899)),
        MoreMenuItem("search", "🔍 Search", "গ্লোবাল সার্চ ফিচার", Icons.Default.Search, Color(0xFF64748B)),
        MoreMenuItem("settings", "⚙️ Settings", "প্রোফাইল ও সেটিংস", Icons.Default.Settings, Color(0xFF475569))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "☰ জীবন পরিচালনা কেন্দ্র (Life Modules)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "আপনার জীবনের প্রতিটি বিভাগ এক জায়গায়",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(menuItems) { item ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(item.id) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(item.color.copy(alpha = 0.15f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
