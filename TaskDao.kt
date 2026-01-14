package com.example.lifearchitect

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks_table SET isCompleted = 0 WHERE isDaily = 1")
    suspend fun resetDailyTasks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: DailyProgress)

    @Query("SELECT * FROM daily_progress_table ORDER BY date DESC LIMIT 30")
    fun getHistory(): Flow<List<DailyProgress>>
}
