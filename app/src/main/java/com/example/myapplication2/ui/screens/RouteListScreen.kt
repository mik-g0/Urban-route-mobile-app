package com.example.myapplication2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.example.myapplication2.ui.models.MainViewModel


@Composable
fun RouteListScreen(vm: MainViewModel, navController: NavHostController) {
    val city = vm.selectedCity ?: return
    val cityRoutes = vm.routes.filter { it.city == city }
    val horizontalPadding = 12.dp
    val topHeight = 56.dp
    val bottomHeight = 56.dp

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE5EFF5))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topHeight,
                    bottom = bottomHeight,
                    start = horizontalPadding,
                    end = horizontalPadding
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            cityRoutes.forEachIndexed { index, route ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val globalIndex = vm.routes.indexOf(route)
                            navController.navigate("route_detail/$globalIndex")
                        },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = route.imageRes),
                            contentDescription = route.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(route.title, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(route.description, fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(route.distance, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Верхняя панель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topHeight)
                .background(Color(0xFF90AFC5))
                .align(Alignment.TopCenter)
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Маршруты города $city", fontSize = 20.sp, color = Color.White)
        }

        // Нижняя панель с кнопками
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomHeight)
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
                    onClick = { navController?.navigate("saved_routes") },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Saved Routes", tint = Color.Red)
                }
            }
        }
    }
}


