package com.example.lifearchitect

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TaskDatabase.getDatabase(application).taskDao()

    private val _tasks = mutableStateOf<List<Task>>(emptyList())
    val tasks: List<Task> get() = _tasks.value.sortedBy { it.id }

    private val _history = mutableStateOf<List<DailyProgress>>(emptyList())

    init {
        viewModelScope.launch {
            dao.getAllTasks().collectLatest { _tasks.value = it }
        }
        viewModelScope.launch {
            dao.getHistory().collectLatest { _history.value = it }
        }
    }

    fun getHistoryData(days: Int): List<Int> {
        return _history.value.take(days).map { it.percentage }.reversed()
    }

    fun addTask(name: String, time: String, hasAlarm: Boolean, isDaily: Boolean, isPersistent: Boolean, priority: String) {
        viewModelScope.launch {
            dao.insertTask(
                Task(
                    name = name,
                    time = time,
                    hasAlarm = hasAlarm,
                    isDaily = isDaily,
                    isPersistent = isPersistent,
                    priorityName = priority
                )
            )
        }
    }

    fun toggleTask(task: Task) = viewModelScope.launch {
        dao.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.deleteTask(task)
    }
}
