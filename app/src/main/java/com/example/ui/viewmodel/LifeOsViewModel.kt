package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.db.LifeOsDatabase
import com.example.data.model.*
import com.example.data.model.repository.LifeOsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LifeOsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LifeOsDatabase.getDatabase(application)
    val repository = LifeOsRepository(db.dao())

    // --- State & Navigation ---
    private val _currentTab = MutableStateFlow("home") // home, routine, tasks, goals, more
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val _currentMoreScreen = MutableStateFlow<String?>(null) // finance, deen, study, work, habits, review, ai, settings, search
    val currentMoreScreen: StateFlow<String?> = _currentMoreScreen.asStateFlow()

    private val _userName = MutableStateFlow("মাহেদী")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _showQuickAddSheet = MutableStateFlow(false)
    val showQuickAddSheet: StateFlow<Boolean> = _showQuickAddSheet.asStateFlow()

    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse: StateFlow<String?> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // --- Database Flows ---
    val todayDate: String = repository.getTodayString()

    val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val routines: StateFlow<List<RoutineEntity>> = repository.allRoutines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habitLogs: StateFlow<List<HabitLogEntity>> = repository.getHabitLogsForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goals: StateFlow<List<GoalEntity>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deenLog: StateFlow<DeenLogEntity?> = repository.getDeenLogForDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val learningItems: StateFlow<List<LearningItemEntity>> = repository.allLearningItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reflections: StateFlow<List<ReflectionEntity>> = repository.allReflections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workspaces: StateFlow<List<WorkspaceEntity>> = repository.allWorkspaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Aggregated Computed Progress ---
    val todayProgressPercent: StateFlow<Int> = combine(tasks, routines, habits, habitLogs, deenLog) { taskList, routineList, habitList, logList, deen ->
        val todayTasks = taskList.filter { it.dueDate.isEmpty() || it.dueDate == todayDate }
        val taskProgress = if (todayTasks.isNotEmpty()) {
            (todayTasks.count { it.status == "Completed" }.toDouble() / todayTasks.size) * 100
        } else 100.0

        val routineProgress = if (routineList.isNotEmpty()) {
            (routineList.count { it.isCompleted }.toDouble() / routineList.size) * 100
        } else 100.0

        val habitProgress = if (habitList.isNotEmpty()) {
            (logList.size.toDouble() / habitList.size) * 100
        } else 100.0

        val deenProgress = if (deen != null) {
            val count = listOf(deen.fajr, deen.dhuhr, deen.asr, deen.maghrib, deen.isha).count { it }
            (count / 5.0) * 100
        } else 60.0

        val overall = (taskProgress * 0.4) + (routineProgress * 0.2) + (habitProgress * 0.2) + (deenProgress * 0.2)
        overall.coerceIn(0.0, 100.0).toInt()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayIncome: StateFlow<Double> = transactions.map { list ->
        list.filter { it.date == todayDate && it.type == "Income" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayExpense: StateFlow<Double> = transactions.map { list ->
        list.filter { it.date == todayDate && it.type == "Expense" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentBalance: StateFlow<Double> = transactions.map { list ->
        val inc = list.filter { it.type == "Income" }.sumOf { it.amount }
        val exp = list.filter { it.type == "Expense" }.sumOf { it.amount }
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        viewModelScope.launch {
            repository.seedDemoDataIfEmpty()
        }
    }

    // --- Navigation Actions ---
    fun setTab(tab: String) {
        _currentTab.value = tab
        if (tab != "more") {
            _currentMoreScreen.value = null
        }
    }

    fun openMoreSubScreen(screen: String) {
        _currentTab.value = "more"
        _currentMoreScreen.value = screen
    }

    fun closeMoreSubScreen() {
        _currentMoreScreen.value = null
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setShowQuickAdd(show: Boolean) {
        _showQuickAddSheet.value = show
    }

    // --- DB Mutation Helpers ---
    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch { repository.toggleTaskStatus(task) }
    }

    fun addTask(title: String, category: String, priority: String, dueTime: String = "", notes: String = "", project: String = "") {
        viewModelScope.launch {
            repository.addTask(
                TaskEntity(
                    title = title,
                    category = category,
                    priority = priority,
                    dueDate = todayDate,
                    dueTime = dueTime,
                    notes = notes,
                    project = project
                )
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    fun toggleRoutine(routine: RoutineEntity) {
        viewModelScope.launch { repository.toggleRoutineCompleted(routine) }
    }

    fun addRoutine(activity: String, time: String, duration: Int, category: String) {
        viewModelScope.launch {
            repository.addRoutine(
                RoutineEntity(
                    startTime = time,
                    durationMinutes = duration,
                    activity = activity,
                    category = category
                )
            )
        }
    }

    fun toggleHabit(habitId: Long, isDone: Boolean) {
        viewModelScope.launch { repository.toggleHabitLog(habitId, todayDate, isDone) }
    }

    fun addTransaction(type: String, amount: Double, category: String, note: String) {
        viewModelScope.launch {
            repository.addTransaction(
                TransactionEntity(
                    type = type,
                    amount = amount,
                    category = category,
                    date = todayDate,
                    note = note
                )
            )
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch { repository.deleteTransaction(transaction) }
    }

    fun updateDeenPrayer(fajr: Boolean? = null, dhuhr: Boolean? = null, asr: Boolean? = null, maghrib: Boolean? = null, isha: Boolean? = null, pages: Int? = null) {
        viewModelScope.launch {
            val current = deenLog.value ?: DeenLogEntity(date = todayDate)
            val updated = current.copy(
                fajr = fajr ?: current.fajr,
                dhuhr = dhuhr ?: current.dhuhr,
                asr = asr ?: current.asr,
                maghrib = maghrib ?: current.maghrib,
                isha = isha ?: current.isha,
                quranPageCount = pages ?: current.quranPageCount
            )
            repository.saveDeenLog(updated)
        }
    }

    fun addGoal(title: String, description: String, deadline: String) {
        viewModelScope.launch {
            repository.addGoal(GoalEntity(title = title, description = description, deadline = deadline))
        }
    }

    fun saveWeeklyReflection(q1: String, q2: String, q3: String, q4: String, q5: String) {
        viewModelScope.launch {
            repository.saveReflection(
                ReflectionEntity(
                    date = todayDate,
                    type = "WEEKLY",
                    q1_best = q1,
                    q2_incomplete = q2,
                    q3_reason_time = q3,
                    q4_top3_next = q4,
                    q5_improvement = q5
                )
            )
        }
    }

    fun resetData() {
        viewModelScope.launch { repository.resetDemoData() }
    }

    // --- AI Assistant Logic ---
    fun askAi(prompt: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResponse.value = null

            val contextSummary = """
                User Profile: ${userName.value}
                Today Tasks: ${tasks.value.joinToString { "${it.priority} - ${it.title} [${it.status}]" }}
                Today Routines: ${routines.value.joinToString { "${it.startTime} - ${it.activity}" }}
                Active Goals: ${goals.value.joinToString { it.title }}
                Habits Status: ${habits.value.size} tracked
                Today Income: ৳${todayIncome.value}, Today Expense: ৳${todayExpense.value}
            """.trimIndent()

            val systemInstruction = "You are MAHEDI LIFE OS AI Assistant. Respond in helpful, encouraging Bangla (with natural technical English terms where appropriate). Use the provided context summary to give realistic, balanced, actionable advice that protects prayer times, sleep, and key responsibilities."

            val userQuery = "$prompt\n\n--- CURRENT LIFE OS CONTEXT ---\n$contextSummary"
            val result = GeminiClient.generateText(userQuery, systemInstruction)

            _aiResponse.value = result
            _isAiLoading.value = false
        }
    }

    fun planMyDayAi() {
        askAi("আমার আজকের কাজ, রুটিন এবং টার্গেট পর্যালোচনা করে আজকের জন্য একটি সুনির্দিষ্ট, বাস্তবসম্মত সময়সূচী (Plan My Day) তৈরি করে দাও।")
    }

    fun organizeMessAi(rawInput: String) {
        askAi("আমার এই অগোছালো লেখাটি থেকে ক্যাটাগরি, প্রায়োরিটি সহ সুনির্দিষ্ট Tasks তৈরি করে দাও: $rawInput")
    }
}
