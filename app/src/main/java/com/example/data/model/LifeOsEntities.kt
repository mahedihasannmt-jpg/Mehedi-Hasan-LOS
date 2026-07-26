package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val priority: String = "🔴 Must Do", // 🔴 Must Do, 🟡 Should Do, 🟢 If Time
    val category: String = "Work", // Deen, Study, Work, Business, Marketing, Learning, Personal
    val project: String = "",
    val status: String = "Pending", // Pending, In Progress, Completed
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: String,
    val endTime: String = "",
    val durationMinutes: Int = 30,
    val activity: String,
    val category: String = "💼 Work", // 🕌 Deen, 📚 Study, 💼 Work, 📈 Business, 🧠 Learning, 🏃 Health, 😴 Rest, 👤 Personal
    val daysOfWeek: String = "ALL",
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String = "Personal",
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val targetDaysPerWeek: Int = 7,
    val icon: String = "check"
)

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: String, // YYYY-MM-DD
    val isCompleted: Boolean = true
)

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val deadline: String = "",
    val targetAmount: Double = 0.0,
    val currentProgress: Int = 0, // 0 - 100
    val status: String = "Active" // Active, Completed, On Hold
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long = 0,
    val name: String,
    val workspace: String = "Fatafat",
    val deadline: String = "",
    val progress: Int = 0,
    val status: String = "Active"
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // Income, Expense
    val amount: Double,
    val category: String, // Business Income, Personal Expense, Fatafat Admin, Marketing, Savings, Loan
    val date: String, // YYYY-MM-DD
    val note: String = ""
)

@Entity(tableName = "deen_logs")
data class DeenLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val fajr: Boolean = false,
    val dhuhr: Boolean = false,
    val asr: Boolean = false,
    val maghrib: Boolean = false,
    val isha: Boolean = false,
    val jamaatCount: Int = 0,
    val quranPageCount: Int = 0,
    val zikrDone: Boolean = false,
    val tahajjudDone: Boolean = false,
    val islamicReadingMinutes: Int = 0,
    val customAmalDone: String = ""
)

@Entity(tableName = "learning_items")
data class LearningItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String, // Digital Marketing, Meta Ads, AI, Web Dev, Arabic, Nahw, Sarf
    val courseName: String,
    val moduleName: String,
    val currentLesson: String,
    val progressPercentage: Int = 0,
    val notes: String = "",
    val studyTimeMinutes: Int = 0
)

@Entity(tableName = "reflections")
data class ReflectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val type: String = "DAILY", // DAILY, WEEKLY
    val q1_best: String = "",
    val q2_incomplete: String = "",
    val q3_reason_time: String = "",
    val q4_top3_next: String = "",
    val q5_improvement: String = ""
)

@Entity(tableName = "workspaces")
data class WorkspaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = ""
)
