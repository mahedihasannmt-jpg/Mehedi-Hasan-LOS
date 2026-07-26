package com.example.data.model.repository

import com.example.data.dao.LifeOsDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class LifeOsRepository(private val dao: LifeOsDao) {

    val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
    val allRoutines: Flow<List<RoutineEntity>> = dao.getAllRoutines()
    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val allGoals: Flow<List<GoalEntity>> = dao.getAllGoals()
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
    val allLearningItems: Flow<List<LearningItemEntity>> = dao.getAllLearningItems()
    val allReflections: Flow<List<ReflectionEntity>> = dao.getAllReflections()
    val allWorkspaces: Flow<List<WorkspaceEntity>> = dao.getAllWorkspaces()

    fun getTodayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun getTasksForDate(date: String): Flow<List<TaskEntity>> = dao.getTasksForDate(date)
    fun getTransactionsForDate(date: String): Flow<List<TransactionEntity>> = dao.getTransactionsForDate(date)
    fun getHabitLogsForDate(date: String): Flow<List<HabitLogEntity>> = dao.getHabitLogsForDate(date)
    fun getDeenLogForDate(date: String): Flow<DeenLogEntity?> = dao.getDeenLogForDate(date)
    fun getReflectionForDate(date: String, type: String): Flow<ReflectionEntity?> = dao.getReflectionForDate(date, type)

    // Task Operations
    suspend fun addTask(task: TaskEntity): Long = dao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)
    suspend fun toggleTaskStatus(task: TaskEntity) {
        val newStatus = if (task.status == "Completed") "Pending" else "Completed"
        dao.updateTask(task.copy(status = newStatus))
    }

    // Routine Operations
    suspend fun addRoutine(routine: RoutineEntity): Long = dao.insertRoutine(routine)
    suspend fun updateRoutine(routine: RoutineEntity) = dao.updateRoutine(routine)
    suspend fun deleteRoutine(routine: RoutineEntity) = dao.deleteRoutine(routine)
    suspend fun toggleRoutineCompleted(routine: RoutineEntity) {
        dao.updateRoutine(routine.copy(isCompleted = !routine.isCompleted))
    }

    // Habit Operations
    suspend fun addHabit(habit: HabitEntity): Long = dao.insertHabit(habit)
    suspend fun updateHabit(habit: HabitEntity) = dao.updateHabit(habit)
    suspend fun deleteHabit(habit: HabitEntity) = dao.deleteHabit(habit)
    suspend fun toggleHabitLog(habitId: Long, date: String, currentlyDone: Boolean) {
        if (currentlyDone) {
            dao.deleteHabitLog(habitId, date)
        } else {
            dao.insertHabitLog(HabitLogEntity(habitId = habitId, date = date, isCompleted = true))
        }
    }

    // Goal & Project Operations
    suspend fun addGoal(goal: GoalEntity): Long = dao.insertGoal(goal)
    suspend fun updateGoal(goal: GoalEntity) = dao.updateGoal(goal)
    suspend fun deleteGoal(goal: GoalEntity) = dao.deleteGoal(goal)

    suspend fun addProject(project: ProjectEntity): Long = dao.insertProject(project)
    suspend fun updateProject(project: ProjectEntity) = dao.updateProject(project)
    suspend fun deleteProject(project: ProjectEntity) = dao.deleteProject(project)

    // Transaction Operations
    suspend fun addTransaction(transaction: TransactionEntity): Long = dao.insertTransaction(transaction)
    suspend fun deleteTransaction(transaction: TransactionEntity) = dao.deleteTransaction(transaction)

    // Deen Operations
    suspend fun saveDeenLog(log: DeenLogEntity) = dao.insertOrUpdateDeenLog(log)

    // Learning Operations
    suspend fun addLearningItem(item: LearningItemEntity): Long = dao.insertLearningItem(item)
    suspend fun updateLearningItem(item: LearningItemEntity) = dao.updateLearningItem(item)
    suspend fun deleteLearningItem(item: LearningItemEntity) = dao.deleteLearningItem(item)

    // Reflection Operations
    suspend fun saveReflection(reflection: ReflectionEntity) = dao.insertReflection(reflection)

    // Workspace Operations
    suspend fun addWorkspace(workspace: WorkspaceEntity): Long = dao.insertWorkspace(workspace)

    // Seed Demo Data if Database is Empty
    suspend fun seedDemoDataIfEmpty() {
        val today = getTodayString()

        // Seed Tasks
        val initialTasks = listOf(
            TaskEntity(
                title = "ফজরের পর কুরআন ও জিকির",
                dueDate = today,
                dueTime = "05:30",
                priority = "🔴 Must Do",
                category = "Deen",
                status = "Completed"
            ),
            TaskEntity(
                title = "মাদ্রাসার ক্লাসের পড়া প্রস্তুতি (মুতালাআ)",
                dueDate = today,
                dueTime = "06:30",
                priority = "🔴 Must Do",
                category = "Study",
                status = "Pending"
            ),
            TaskEntity(
                title = "Fatafat Admin Order & Delivery Check",
                dueDate = today,
                dueTime = "15:00",
                priority = "🔴 Must Do",
                category = "Work",
                project = "Fatafat Growth",
                status = "Pending"
            ),
            TaskEntity(
                title = "Facebook Marketing Campaign Audit",
                dueDate = today,
                dueTime = "17:00",
                priority = "🟡 Should Do",
                category = "Marketing",
                project = "Digital Marketing",
                status = "Pending"
            ),
            TaskEntity(
                title = "AI & Android App Development Practice",
                dueDate = today,
                dueTime = "20:00",
                priority = "🟢 If Time",
                category = "Learning",
                status = "Pending"
            )
        )
        for (task in initialTasks) {
            dao.insertTask(task)
        }

        // Seed Routines
        val initialRoutines = listOf(
            RoutineEntity(startTime = "05:00", endTime = "06:00", durationMinutes = 60, activity = "ফজর সালাত, কুরআন ও যিকর", category = "🕌 Deen", isCompleted = true),
            RoutineEntity(startTime = "06:00", endTime = "08:30", durationMinutes = 150, activity = "ব্যক্তিগত উন্নয়ন ও মুতালাআ", category = "📚 Study"),
            RoutineEntity(startTime = "09:00", endTime = "13:30", durationMinutes = 270, activity = "মাদ্রাসা ক্লাস ও দ্বীনি শিক্ষা", category = "📚 Study"),
            RoutineEntity(startTime = "13:30", endTime = "15:00", durationMinutes = 90, activity = "জোহর সালাত, দুপুরের খাবার ও বিশ্রাম", category = "😴 Rest"),
            RoutineEntity(startTime = "15:00", endTime = "17:00", durationMinutes = 120, activity = "Fatafat Admin & Operation Work", category = "💼 Work"),
            RoutineEntity(startTime = "17:00", endTime = "19:00", durationMinutes = 120, activity = "মার্কেটিং, ক্লায়েন্ট ও বিজনেস ডেভেলপমেন্ট", category = "📈 Business"),
            RoutineEntity(startTime = "19:00", endTime = "22:00", durationMinutes = 180, activity = "এশার সালাত, নৈশ মুতালাআ ও এআই লার্নিং", category = "🧠 Learning"),
            RoutineEntity(startTime = "22:00", endTime = "22:30", durationMinutes = 30, activity = "ডেইলি রিভিউ ও ঘুমানোর প্রস্তুতি", category = "👤 Personal")
        )
        for (routine in initialRoutines) {
            dao.insertRoutine(routine)
        }

        // Seed Habits
        val initialHabits = listOf(
            HabitEntity(name = "৫ ওয়াক্ত সালাত জামাতে", category = "Deen", currentStreak = 12, bestStreak = 30),
            HabitEntity(name = "দৈনিক ১ পারা কুরআন তেলাওয়াত", category = "Deen", currentStreak = 7, bestStreak = 15),
            HabitEntity(name = "২ ঘণ্টা মুতালাআ / Study", category = "Study", currentStreak = 5, bestStreak = 14),
            HabitEntity(name = "Fatafat দৈনিক হিসাব চেক", category = "Work", currentStreak = 9, bestStreak = 21),
            HabitEntity(name = "৩০ মিনিট হাঁটা ও ব্যায়াম", category = "Health", currentStreak = 4, bestStreak = 10),
            HabitEntity(name = "রাত ১১টার মধ্যে ঘুমানো", category = "Rest", currentStreak = 3, bestStreak = 8)
        )
        for (habit in initialHabits) {
            val id = dao.insertHabit(habit)
            dao.insertHabitLog(HabitLogEntity(habitId = id, date = today, isCompleted = true))
        }

        // Seed Goals & Projects
        val goal1 = GoalEntity(title = "মাসে ৳৫০,০০০ আয় নিশ্চিত করা", description = "ফাতাফাত ও ডিজিটাল সার্ভিস থেকে আয়ের প্রবৃদ্ধি", deadline = "2026-12-31", targetAmount = 50000.0, currentProgress = 55)
        val goal2 = GoalEntity(title = "মাদ্রাসার বাৎসরিক পরীক্ষায় প্রথম হওয়া", description = "নিয়মিত মুতালাআ ও কিতাব অধ্যয়নের মাধ্যমে ফলাফল নিশ্চিতকরণ", deadline = "2026-11-30", currentProgress = 75)
        val goal3 = GoalEntity(title = "এআই ও অ্যাপ ডেভেলপমেন্ট মাস্টার করা", description = "অত্যাধুনিক প্রযুক্তি শিখে নতুন সার্ভিস তৈরি", deadline = "2026-10-15", currentProgress = 60)

        val g1Id = dao.insertGoal(goal1)
        val g2Id = dao.insertGoal(goal2)
        val g3Id = dao.insertGoal(goal3)

        dao.insertProject(ProjectEntity(goalId = g1Id, name = "Fatafat Growth & Order System", workspace = "Fatafat", progress = 60))
        dao.insertProject(ProjectEntity(goalId = g1Id, name = "Digital Marketing Client Acquisition", workspace = "Marketing", progress = 50))
        dao.insertProject(ProjectEntity(goalId = g2Id, name = "Backbenchers University Preparation", workspace = "Study", progress = 80))

        // Seed Transactions
        dao.insertTransaction(TransactionEntity(type = "Income", amount = 6500.0, category = "Fatafat Income", date = today, note = "আজকের ডেলভারি ফি ও প্রোডাক্ট সেল"))
        dao.insertTransaction(TransactionEntity(type = "Income", amount = 3500.0, category = "Marketing Service", date = today, note = "ক্লায়েন্ট ফেসবুক এড সেটআপ"))
        dao.insertTransaction(TransactionEntity(type = "Expense", amount = 1200.0, category = "Business Expense", date = today, note = "ফাতাফাত ডেলিভারি ফুয়েল ও সার্ভিস চার্জ"))
        dao.insertTransaction(TransactionEntity(type = "Expense", amount = 850.0, category = "Personal Expense", date = today, note = "মাদ্রাসা কিতাব ক্রয় ও টুকটাক খরচ"))

        // Seed Learning Items
        dao.insertLearningItem(
            LearningItemEntity(
                subject = "Digital Marketing",
                courseName = "Meta Ads Advanced Blueprint",
                moduleName = "Audience Targeting & ROAS",
                currentLesson = "Custom Audiences & Lookalike Modeling",
                progressPercentage = 70,
                studyTimeMinutes = 45,
                notes = "পিক্সেল ট্র্যাকিং ও কনভার্সন অপটিমাইজেশন নিশ্চিত করতে হবে।"
            )
        )
        dao.insertLearningItem(
            LearningItemEntity(
                subject = "AI & Android",
                courseName = "Jetpack Compose & Gemini OS",
                moduleName = "Room & Coroutines",
                currentLesson = "Offline First Architecture",
                progressPercentage = 85,
                studyTimeMinutes = 60,
                notes = "StateFlow ও Clean MVVM ব্যবহার শেখা হলো।"
            )
        )

        // Seed Deen Log
        dao.insertOrUpdateDeenLog(
            DeenLogEntity(
                date = today,
                fajr = true,
                dhuhr = true,
                asr = true,
                maghrib = false,
                isha = false,
                jamaatCount = 3,
                quranPageCount = 6,
                zikrDone = true,
                tahajjudDone = true,
                islamicReadingMinutes = 30
            )
        )

        // Seed Workspaces
        dao.insertWorkspace(WorkspaceEntity(name = "Fatafat", description = "অ্যাডমিন, অর্ডার ম্যানেজমেন্ট, ডেলিভারি, ফাইন্যান্স ও অপারেশনস"))
        dao.insertWorkspace(WorkspaceEntity(name = "Marketing", description = "ফেসবুক এডস, মেটা ক্যাম্পেইন, কন্টেন্ট ও ক্লায়েন্ট ওয়ার্ক"))
    }

    suspend fun resetDemoData() {
        dao.clearAllTasks()
        dao.clearAllRoutines()
        dao.clearAllHabits()
        dao.clearAllHabitLogs()
        dao.clearAllGoals()
        dao.clearAllProjects()
        dao.clearAllTransactions()
        dao.clearAllDeenLogs()
        dao.clearAllLearningItems()
        dao.clearAllReflections()
        dao.clearAllWorkspaces()

        seedDemoDataIfEmpty()
    }
}
