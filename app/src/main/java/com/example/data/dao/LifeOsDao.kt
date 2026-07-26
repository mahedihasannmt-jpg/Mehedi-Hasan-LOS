package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LifeOsDao {

    // --- TASKS ---
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate = :date ORDER BY priority DESC, id DESC")
    fun getTasksForDate(date: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)


    // --- ROUTINES ---
    @Query("SELECT * FROM routines ORDER BY startTime ASC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Query("UPDATE routines SET isCompleted = :completed WHERE id = :id")
    suspend fun setRoutineCompleted(id: Long, completed: Boolean)


    // --- HABITS & LOGS ---
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    fun getHabitLogsForDate(date: String): Flow<List<HabitLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: HabitLogEntity)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteHabitLog(habitId: Long, date: String)


    // --- GOALS & PROJECTS ---
    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)


    // --- TRANSACTIONS ---
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date = :date ORDER BY id DESC")
    fun getTransactionsForDate(date: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)


    // --- DEEN LOGS ---
    @Query("SELECT * FROM deen_logs WHERE date = :date LIMIT 1")
    fun getDeenLogForDate(date: String): Flow<DeenLogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDeenLog(log: DeenLogEntity)


    // --- LEARNING ITEMS ---
    @Query("SELECT * FROM learning_items ORDER BY id DESC")
    fun getAllLearningItems(): Flow<List<LearningItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearningItem(item: LearningItemEntity): Long

    @Update
    suspend fun updateLearningItem(item: LearningItemEntity)

    @Delete
    suspend fun deleteLearningItem(item: LearningItemEntity)


    // --- REFLECTIONS ---
    @Query("SELECT * FROM reflections ORDER BY date DESC")
    fun getAllReflections(): Flow<List<ReflectionEntity>>

    @Query("SELECT * FROM reflections WHERE date = :date AND type = :type LIMIT 1")
    fun getReflectionForDate(date: String, type: String): Flow<ReflectionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: ReflectionEntity)


    // --- WORKSPACES ---
    @Query("SELECT * FROM workspaces ORDER BY id ASC")
    fun getAllWorkspaces(): Flow<List<WorkspaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkspace(workspace: WorkspaceEntity): Long

    @Query("DELETE FROM tasks")
    suspend fun clearAllTasks()

    @Query("DELETE FROM routines")
    suspend fun clearAllRoutines()

    @Query("DELETE FROM habits")
    suspend fun clearAllHabits()

    @Query("DELETE FROM habit_logs")
    suspend fun clearAllHabitLogs()

    @Query("DELETE FROM goals")
    suspend fun clearAllGoals()

    @Query("DELETE FROM projects")
    suspend fun clearAllProjects()

    @Query("DELETE FROM transactions")
    suspend fun clearAllTransactions()

    @Query("DELETE FROM deen_logs")
    suspend fun clearAllDeenLogs()

    @Query("DELETE FROM learning_items")
    suspend fun clearAllLearningItems()

    @Query("DELETE FROM reflections")
    suspend fun clearAllReflections()

    @Query("DELETE FROM workspaces")
    suspend fun clearAllWorkspaces()
}
