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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel, onAddTaskClick: () -> Unit, onBack: () -> Unit) {
    val zonaMexico = ZoneId.of("America/Mexico_City")
    val fechaActual = LocalDate.now(zonaMexico).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val tasksSorted = viewModel.tasks.sortedWith(
        compareByDescending<Task> { it.priorityName == "ALTA" }.thenBy { it.isCompleted }
    )

    Box(modifier = Modifier.fillMaxSize().background(CharcoalGrey).padding(horizontal = 40.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(60.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Text("<", color = GrassGreen, fontSize = 35.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("HOY", color = GrassGreen, fontSize = 40.sp, fontWeight = FontWeight.Black)
                    Text(fechaActual, color = Color.White.copy(0.5f), fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(items = tasksSorted, key = { it.id }) { task ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.StartToEnd) {
                                viewModel.deleteTask(task)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = true,
                        enableDismissFromEndToStart = false,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.Red)
                                    .padding(start = 30.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                            }
                        }
                    ) {
                        TaskCardSolida(task = task, onToggle = { viewModel.toggleTask(task) })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddTaskClick,
            containerColor = CharcoalGrey,
            contentColor = GrassGreen,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 40.dp).size(75.dp).border(2.dp, GrassGreen, CircleShape)
        ) {
            Text("+", fontSize = 45.sp)
        }
    }
}

@Composable
fun TaskCardSolida(task: Task, onToggle: () -> Unit) {
    val isDone = task.isCompleted
    val isHigh = task.priorityName == "ALTA"
    val borderColor = if (isDone) GrassGreen else if (isHigh) Color(0xFFFFCC00) else Color.White.copy(0.2f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF3D3D3D))
            .border(2.dp, borderColor, RoundedCornerShape(22.dp))
            .clickable { onToggle() }
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isHigh) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFCC00)))
            Spacer(modifier = Modifier.width(15.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(task.name.uppercase(), color = if (isDone) GrassGreen else Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(task.time, color = Color.Gray, fontSize = 14.sp)
        }
        Box(modifier = Modifier.size(35.dp).border(2.dp, GrassGreen, CircleShape).background(if (isDone) GrassGreen else Color.Transparent, CircleShape), contentAlignment = Alignment.Center) {
            if (isDone) Text("✓", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
