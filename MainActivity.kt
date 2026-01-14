package com.example.lifearchitect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Programar reseteo real
        val resetRequest = PeriodicWorkRequestBuilder<DailyResetWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyReset",
            ExistingPeriodicWorkPolicy.KEEP,
            resetRequest
        )

        setContent {
            Surface(color = Color(0xFF121212)) {
                NavigationGraph()
            }
        }
    }
}
