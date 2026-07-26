package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ReflectionEntity
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.LifeOsViewModel

@Composable
fun WeeklyReviewScreen(
    viewModel: LifeOsViewModel,
    onBack: () -> Unit
) {
    var q1 by remember { mutableStateOf("") }
    var q2 by remember { mutableStateOf("") }
    var q3 by remember { mutableStateOf("") }
    var q4 by remember { mutableStateOf("") }
    var q5 by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

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
                        text = "📊 সাপ্তাহিক পর্যালোচনা (Weekly Review)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "আত্মপর্যালোচনা, অর্জন ও আগামী সপ্তাহের পরিকল্পনা",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("১. এই সপ্তাহে আমার সবচেয়ে বড় অর্জন কোনটি?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = q1,
                        onValueChange = { q1 = it },
                        placeholder = { Text("উদাহরণ: আলহামদুলিল্লাহ ৫ ওয়াক্ত সালাত ও গুরুত্বপূর্ণ অর্ডারের কাজ সেড়েছি...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("২. কোন কোন কাজ অসম্পূর্ণ থেকে গেল এবং কেন?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = q2,
                        onValueChange = { q2 = it },
                        placeholder = { Text("উদাহরণ: মার্কেটিং ভিডিও অ্যাড তৈরি করতে সময় লেগেছে...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("৩. আমি কি আমার সময় সঠিকভাবে ব্যবহার করতে পেরেছি?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = q3,
                        onValueChange = { q3 = it },
                        placeholder = { Text("সময় ব্যবস্থাপনা মূল্যায়ন...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("৪. আগামী সপ্তাহের টপ ৩ অগ্রাধিকার কী কী?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = q4,
                        onValueChange = { q4 = it },
                        placeholder = { Text("১. মুতালাআ ২. ফাতাফাত সেলস ৩. ফেব এডস...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("৫. আমল, পড়ালেখা ও ব্যবসায় আমার কী উন্নতি দরকার?", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = q5,
                        onValueChange = { q5 = it },
                        placeholder = { Text("পরিকল্পনা ও সংশোধনের মন্তব্য...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.saveWeeklyReflection(q1, q2, q3, q4, q5)
                            isSaved = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isSaved) "সংরক্ষিত হয়েছে ✔" else "রিভিউ সংরক্ষণ করুন")
                    }
                }
            }
        }
    }
}
