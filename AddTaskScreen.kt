package com.example.lifearchitect

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: TaskViewModel, onBack: () -> Unit) {
    var taskName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("08:00 AM") }
    var isHighPriority by remember { mutableStateOf(false) }
    var missionType by remember { mutableStateOf("UNA VEZ") }
    var hasAlarm by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val timePicker = TimePickerDialog(context, { _, h, m ->
        selectedTime = String.format("%02d:%02d %s", if(h>12) h-12 else if(h==0) 12 else h, m, if(h>=12) "PM" else "AM")
    }, 8, 0, false)

    Column(modifier = Modifier.fillMaxSize().background(CharcoalGrey).padding(horizontal = 40.dp)) {
        Spacer(modifier = Modifier.height(60.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Text("<", color = GrassGreen, fontSize = 35.sp, fontWeight = FontWeight.Bold)
            }
            Text("CONFIGURAR", color = GrassGreen, fontSize = 28.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clickable { isHighPriority = !isHighPriority }
                    .background(if (isHighPriority) Color(0xFFFFCC00).copy(0.2f) else Color.Transparent, RoundedCornerShape(12.dp))
                    .border(2.dp, if (isHighPriority) Color(0xFFFFCC00) else Color.White.copy(0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("ALTA", color = if (isHighPriority) Color(0xFFFFCC00) else Color.Gray, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = taskName, onValueChange = { if (it.length <= 25) taskName = it },
            textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White),
            label = { Text("ID DE LA MISIÓN", color = GrassGreen.copy(0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrect = false),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GrassGreen, unfocusedBorderColor = Color.White.copy(0.1f))
        )

        Spacer(modifier = Modifier.height(45.dp))

        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White.copy(0.05f), RoundedCornerShape(24.dp)).padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(45.dp)
        ) {
            Column {
                Text("FRECUENCIA", color = Color.White.copy(0.6f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("UNA VEZ", "DIARIO", "PENDIENTE").forEach { type ->
                        val isSelected = missionType == type
                        Box(
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).clickable { missionType = type }
                                .background(if (isSelected) GrassGreen.copy(0.15f) else Color.Transparent, RoundedCornerShape(12.dp))
                                .border(2.dp, if (isSelected) GrassGreen else Color.White.copy(0.1f), RoundedCornerShape(12.dp))
                                .padding(vertical = 16.dp), contentAlignment = Alignment.Center
                        ) {
                            Text(type, color = if (isSelected) GrassGreen else Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth().clickable { timePicker.show() }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("HORA", color = Color.White, fontSize = 22.sp)
                Text(selectedTime, color = GrassGreen, fontSize = 24.sp, fontWeight = FontWeight.Black)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("ALARMA", color = Color.White, fontSize = 22.sp)
                Switch(checked = hasAlarm, onCheckedChange = { hasAlarm = it }, colors = SwitchDefaults.colors(checkedThumbColor = GrassGreen))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            enabled = !isSaving,
            onClick = {
                if (taskName.isNotBlank() && !isSaving) {
                    isSaving = true
                    scope.launch {
                        viewModel.addTask(
                            taskName, selectedTime, hasAlarm,
                            missionType == "DIARIO", missionType == "PENDIENTE",
                            if (isHighPriority) "ALTA" else "MEDIA"
                        )
                        delay(300)
                        onBack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(85.dp).padding(bottom = 20.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if(isSaving) Color.Gray else GrassGreen)
        ) {
            Text(if(isSaving) "VINCULANDO..." else "VINCULAR A SISTEMA", color = CharcoalGrey, fontWeight = FontWeight.Black, fontSize = 24.sp)
        }
    }
}
