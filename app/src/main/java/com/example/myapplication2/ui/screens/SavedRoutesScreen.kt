package com.example.myapplication2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication2.ui.models.MainViewModel
import com.example.myapplication2.ui.models.RouteDetail


@Composable
fun SavedRoutesScreen(vm: MainViewModel, navController: NavHostController) {
    val savedRoutes = vm.savedRoutes
    var selectedRoute by remember { mutableStateOf<RouteDetail?>(null) }
    val topHeight = 56.dp
    val bottomNavHeight = 56.dp
    val actionButtonsHeight = 56.dp
    val horizontalPadding = 12.dp

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F0EB))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topHeight,
                    bottom = bottomNavHeight + if (selectedRoute != null) actionButtonsHeight else 0.dp
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Сохранённые маршруты", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (savedRoutes.isEmpty()) {
                Text("Нет сохранённых маршрутов", color = Color.Gray)
            } else {
                savedRoutes.forEach { route ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedRoute = route },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(route.title, fontSize = 16.sp)
                            Text(route.description, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // Верхняя панель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topHeight)
                .background(Color(0xFF9FB7C9))
                .align(Alignment.TopCenter)
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Сохранённые маршруты", fontSize = 20.sp, color = Color.White)
        }

        if (selectedRoute != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(actionButtonsHeight)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = horizontalPadding)
                    .offset(y = -bottomNavHeight),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {  selectedRoute?.city?.let { city ->
                            // Обновляем выбранный город и координаты для карты
                            vm.selectCityForMap(city)

                            // Переходим на экран карты
                            navController.navigate("city_map")
                        }},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA592F)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Отправиться", color = Color.White)
                    }

                    Button(
                        onClick = {
                            selectedRoute?.let {
                                vm.removeSavedRoute(it)
                                selectedRoute = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Удалить", color = Color.White)
                    }
                }
            }
        }

        // Нижняя панель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomNavHeight)
                .background(Color(0xFF90AFC5))
                .align(Alignment.BottomCenter)
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate("routes") },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "All Routes")
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Saved Routes", tint = Color.Red)
                }
            }
        }
    }
}
