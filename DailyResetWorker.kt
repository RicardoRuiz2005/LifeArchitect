package com.example.lifearchitect

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class DailyResetWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = TaskDatabase.getDatabase(applicationContext)
        val dao = database.taskDao()

        return try {
            // 1. Calcular porcentaje del día antes de borrar
            val tasks = dao.getAllTasks().first() // Obtenemos lista actual
            if (tasks.isNotEmpty()) {
                val percent = (tasks.count { it.isCompleted }.toFloat() / tasks.size * 100).toInt()
                // 2. Guardar en el historial
                dao.insertProgress(DailyProgress(LocalDate.now().toString(), percent))
            }

            // 3. Resetear tareas diarias
            dao.resetDailyTasks()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
