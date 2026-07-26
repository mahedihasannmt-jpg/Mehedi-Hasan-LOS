package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.LifeOsDao
import com.example.data.model.*

@Database(
    entities = [
        TaskEntity::class,
        RoutineEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        GoalEntity::class,
        ProjectEntity::class,
        TransactionEntity::class,
        DeenLogEntity::class,
        LearningItemEntity::class,
        ReflectionEntity::class,
        WorkspaceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LifeOsDatabase : RoomDatabase() {
    abstract fun dao(): LifeOsDao

    companion object {
        @Volatile
        private var INSTANCE: LifeOsDatabase? = null

        fun getDatabase(context: Context): LifeOsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LifeOsDatabase::class.java,
                    "mahedi_life_os_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
