package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkNavy
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun DeenAmalScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    val deenLog by viewModel.deenLog.collectAsState()

    val fajr = deenLog?.fajr ?: true
    val dhuhr = deenLog?.dhuhr ?: true
    val asr = deenLog?.asr ?: true
    val maghrib = deenLog?.maghrib ?: false
    val isha = deenLog?.isha ?: false
    val quranPages = deenLog?.quranPageCount ?: 5

    val completedCount = listOf(fajr, dhuhr, asr, maghrib, isha).count { it }

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
                        text = "🕌 দ্বীন ও আমল (Deen & Amal)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "দৈনিক ৫ ওয়াক্ত সালাত, কুরআন তেলাওয়াত ও জিকির",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Deen Progress Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "আজকের সালাত অগ্রগতি",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "$completedCount / ৫ ওয়াক্ত আদায়",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "জামাতে আদায়ের চেষ্টা করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF10B981)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF10B981).copy(alpha = 0.2f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Mosque,
                                contentDescription = "Mosque",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5 Prayers Check-list
        item {
            Text(
                text = "৫ ওয়াক্ত সালাত ট্র্যাকার",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            PrayerItem(name = "ফজর (Fajr)", isDone = fajr, onToggle = { viewModel.updateDeenPrayer(fajr = !fajr) })
        }
        item {
            PrayerItem(name = "জোহর (Dhuhr)", isDone = dhuhr, onToggle = { viewModel.updateDeenPrayer(dhuhr = !dhuhr) })
        }
        item {
            PrayerItem(name = "আসর (Asr)", isDone = asr, onToggle = { viewModel.updateDeenPrayer(asr = !asr) })
        }
        item {
            PrayerItem(name = "মাগরিব (Maghrib)", isDone = maghrib, onToggle = { viewModel.updateDeenPrayer(maghrib = !maghrib) })
        }
        item {
            PrayerItem(name = "এশা (Isha)", isDone = isha, onToggle = { viewModel.updateDeenPrayer(isha = !isha) })
        }

        // Quran & Other Amal
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📖 কুরআন তেলাওয়াত ও জিকির",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "আজকের তেলাওয়াতকৃত পৃষ্ঠা:", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { viewModel.updateDeenPrayer(pages = (quranPages - 1).coerceAtLeast(0)) }) {
                                Text("-", fontWeight = FontWeight.Bold)
                            }
                            Text(text = "$quranPages পৃষ্ঠা", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.updateDeenPrayer(pages = quranPages + 1) }) {
                                Text("+", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerItem(
    name: String,
    isDone: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) Color(0xFFECFDF5) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isDone) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDone) Color(0xFF10B981) else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isDone) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isDone) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Done", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("আদায়কৃত")
                } else {
                    Text("আদায়ে টিক দিন")
                }
            }
        }
    }
}
