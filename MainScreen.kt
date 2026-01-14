package com.example.lifearchitect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(viewModel: TaskViewModel, onNavigateToTasks: () -> Unit) {
    var selectedPeriod by remember { mutableStateOf("DÍA") }
    val tasks = viewModel.tasks
    val total = tasks.size
    val completadas = tasks.count { it.isCompleted }
    val porcentaje = if (total > 0) completadas.toFloat() / total else 0f

    Column(
        modifier = Modifier.fillMaxSize().background(CharcoalGrey).padding(horizontal = 40.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LifeArchitect", color = GrassGreen, fontSize = 42.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black.copy(0.2f), RoundedCornerShape(32.dp)).border(2.dp, GrassGreen.copy(0.3f), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedPeriod == "DÍA") {
                // CÍRCULO ACTUAL
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(220.dp)) {
                        drawArc(Color.White.copy(0.05f), 0f, 360f, false, style = Stroke(24.dp.toPx(), cap = StrokeCap.Round))
                        drawArc(GrassGreen, -90f, 360f * porcentaje, false, style = Stroke(24.dp.toPx(), cap = StrokeCap.Round))
                    }
                    Text("${(porcentaje * 100).toInt()}%", color = GrassGreen, fontSize = 48.sp, fontWeight = FontWeight.Black)
                }
            } else {
                // GRÁFICA DE PUNTOS (Simulada por ahora con datos de prueba)
                PointGraph(listOf(100, 80, 100, 40, 90, 100, 70))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("DÍA", "SEM", "MES", "AÑO").forEach { period ->
                Text(
                    text = period,
                    color = if (selectedPeriod == period) GrassGreen else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { selectedPeriod = period }.padding(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth().height(90.dp).background(CharcoalGrey, RoundedCornerShape(24.dp)).border(2.dp, GrassGreen, RoundedCornerShape(24.dp)).clickable { onNavigateToTasks() }.padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("HOY", color = GrassGreen, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun PointGraph(data: List<Int>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp).padding(40.dp)) {
        val spacing = size.width / (data.size - 1).coerceAtLeast(1)
        val maxHeight = size.height

        // Líneas de fondo
        drawLine(Color.White.copy(0.1f), Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
        drawLine(Color.White.copy(0.1f), Offset(0f, maxHeight), Offset(size.width, maxHeight), 1.dp.toPx())

        data.forEachIndexed { index, percent ->
            val x = index * spacing
            val y = maxHeight - (maxHeight * (percent / 100f))

            // Conector
            if (index > 0) {
                val prevX = (index - 1) * spacing
                val prevY = maxHeight - (maxHeight * (data[index - 1] / 100f))
                drawLine(GrassGreen.copy(0.3f), Offset(prevX, prevY), Offset(x, y), 2.dp.toPx())
            }

            // Punto (Si es 100% brilla más)
            drawCircle(
                color = if (percent == 100) GrassGreen else Color.White,
                radius = if (percent == 100) 6.dp.toPx() else 4.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}
