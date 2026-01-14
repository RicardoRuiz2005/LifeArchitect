package com.example.lifearchitect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskScreen(viewModel: TaskViewModel, onNavigateBack: () -> Unit) {
    val darkBackground = Color(0xFF121212)
    val neonGreen = Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MISIONES",
                color = neonGreen,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "VOLVER",
                color = Color.Gray,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(
                items = viewModel.tasks,
                key = { it.id }
            ) { task ->
                TaskItemRow(
                    task = task,
                    onToggle = { viewModel.toggleTask(task) },
                    onDelete = { viewModel.deleteTask(task) }
                )
            }
        }
    }
}

@Composable
fun TaskItemRow(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val neonGreen = Color(0xFF4CAF50)
    val priorityColor = when(task.priorityName) {
        "ALTA" -> Color(0xFFFF5252)
        "MEDIA" -> neonGreen
        else -> Color(0xFF4FC3F7)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(20.dp))
            .border(
                width = 2.dp,
                color = if (task.isCompleted) neonGreen else priorityColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onToggle() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${task.time} | ${task.priorityName}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.6f))
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = if (task.isCompleted) neonGreen else Color.Transparent,
                    shape = CircleShape
                )
                .border(2.dp, neonGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(Icons.Default.Check, null, tint = Color.Black, modifier = Modifier.size(18.dp))
            }
        }
    }
}
