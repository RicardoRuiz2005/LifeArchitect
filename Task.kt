package com.example.lifearchitect

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val time: String,
    val isCompleted: Boolean = false,
    val hasAlarm: Boolean = false,
    val isDaily: Boolean = true,
    val isPersistent: Boolean = false,
    val priorityName: String = "MEDIA"
)

@Entity(tableName = "daily_progress_table")
data class DailyProgress(
    @PrimaryKey val date: String,
    val percentage: Int
)
