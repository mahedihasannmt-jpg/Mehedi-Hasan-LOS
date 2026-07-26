package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun AIAssistantScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    var userPrompt by remember { mutableStateOf("") }
    var messText by remember { mutableStateOf("") }

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
                        text = "🤖 AI Assistant & Assistant",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gemini AI দিয়ে আপনার দৈনন্দিন পরিকল্পনা ও সংগঠন সহজ করুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Quick AI Actions
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = "AI", tint = Color(0xFFEC4899))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "স্মার্ট প্ল্যানার (Smart Quick Actions)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.planMyDayAi() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Plan")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("📅 Plan My Day (আজকের পরিকল্পনা সাজাও)")
                    }
                }
            }
        }

        // Organize My Mess Input
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "🧹 Organize My Mess (অগোছালো চিন্তা সাজাও)", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = messText,
                        onValueChange = { messText = it },
                        placeholder = { Text("যেমন: কাল সকালে ক্লাসে যেতে হবে, ফাতাফাত ২টা ডেলিভারি, জোহরের পর মার্কেটিং পোস্ট সেভ করা আর রাত্রে পড়া...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (messText.isNotBlank()) {
                                viewModel.organizeMessAi(messText)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("অর্গানাইজ করুন")
                    }
                }
            }
        }

        // Custom Query Input
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userPrompt,
                    onValueChange = { userPrompt = it },
                    placeholder = { Text("AI Assistant কে যেকোনো প্রশ্ন করুন...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (userPrompt.isNotBlank()) {
                            viewModel.askAi(userPrompt)
                            userPrompt = ""
                        }
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = OrangePrimary)
                }
            }
        }

        // AI Response Output
        if (isAiLoading) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = OrangePrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Gemini AI চিন্তা করছে...", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        if (aiResponse != null) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI", tint = OrangePrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "AI পরামর্শ ও দিকনির্দেশনা", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = aiResponse!!,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }
                }
            }
        }
    }
}
