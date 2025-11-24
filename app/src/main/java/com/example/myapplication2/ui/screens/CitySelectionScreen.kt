package com.example.myapplication2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication2.ui.models.MainViewModel

@Composable
fun CitySelectionScreen(
    vm: MainViewModel,
    onCitySelected: (String) -> Unit
) {
    val cities = listOf(
        "Москва", "Санкт-Петербург", "Казань",
        "Красноярск", "Астрахань", "Калининград",
        "Сочи", "Самара", "Новосибирск"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEE8E1))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF90AFC5))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Выбор города", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        cities.forEach { city ->
            Button(
                onClick = { onCitySelected(city) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (city == vm.selectedCity) Color(0xFF884421) else Color(0xFFF1F1F1),
                    contentColor = if (city == vm.selectedCity) Color.White else Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(city)
            }
        }
    }
}
