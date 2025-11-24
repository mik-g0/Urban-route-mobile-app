package com.example.myapplication2.ui.screens

import androidx.compose.material3.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.myapplication2.ui.models.MainViewModel
import com.example.myapplication2.ui.data.RouteEntity
import com.example.myapplication2.R


@Composable
fun AddEditRouteScreen(
    vm: MainViewModel,
    onBack: () -> Unit
) {
    val cities = listOf("Москва", "Санкт-Петербург", "Казань", "Красноярск", "Астрахань")

    var title by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val lastRoutes by remember { derivedStateOf { vm.routes.takeLast(5) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5EFF5))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Добавление маршрута", fontSize = 22.sp, color = Color(0xFF90AFC5), modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(16.dp))

        TextField(value = title, onValueChange = { title = it }, label = { Text("Название маршрута") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        Text("Выберите город")
        cities.forEach { c ->
            Button(
                onClick = { city = c },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (c == city) Color(0xFF90AFC5) else Color(0xFFF1F1F1),
                    contentColor = if (c == city) Color.White else Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                Text(c)
            }
        }

        Spacer(Modifier.height(8.dp))

        TextField(value = description, onValueChange = { description = it }, label = { Text("Описание") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        TextField(value = distance, onValueChange = { distance = it }, label = { Text("Дистанция (например, 1.5 км)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    title.isBlank() -> errorMessage = "Введите название"
                    city.isBlank() -> errorMessage = "Выберите город"
                    description.isBlank() -> errorMessage = "Введите описание"
                    !distance.matches("""\d+(\.\d+)?\s?км""".toRegex()) -> errorMessage = "Дистанция должна быть в формате '1.5 км'"
                    else -> {
                        errorMessage = ""
                        val entity = RouteEntity(
                            id = 0,
                            title = title,
                            city = city,
                            description = description,
                            distance = distance,
                            imageRes = R.drawable.route1
                        )
                        vm.addRoute(entity)
                        title = ""
                        city = ""
                        description = ""
                        distance = ""
                    }
                }
            },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA592F)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red)
        }

        Spacer(Modifier.height(16.dp))
        Text("Последние маршруты:", fontSize = 16.sp)

        lastRoutes.forEach { route ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(route.title, fontSize = 16.sp)
                        Text("${route.city} • ${route.distance}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            val entity = RouteEntity(
                                id = route.id,
                                title = route.title,
                                city = route.city,
                                description = route.description,
                                distance = route.distance,
                                imageRes = route.imageRes
                            )
                            vm.deleteRoute(entity)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
