package com.example.myapplication2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication2.ui.models.MainViewModel
import com.example.myapplication2.ui.models.RouteDetail


@Composable
fun RouteDetailScreen(
    route: RouteDetail,
    navController: NavHostController? = null,
    vm: MainViewModel
) {
    val topHeight = 56.dp
    val bottomHeight = 56.dp
    val actionButtonsHeight = 56.dp
    val horizontalPadding = 12.dp

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F0EB))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topHeight,
                    bottom = bottomHeight + actionButtonsHeight,
                    start = horizontalPadding,
                    end = horizontalPadding
                )
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(route.title, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(route.description, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))

            route.stops.forEach { stop ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stop.title,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(id = route.imageRes),
                    contentDescription = route.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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
            Text("Выбранный маршрут", fontSize = 20.sp, color = Color.White)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(actionButtonsHeight)
                .align(Alignment.BottomCenter)
                .padding(horizontal = horizontalPadding)
                .offset(y = -bottomHeight),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { vm.selectedCity?.let { city ->
                        vm.selectCityForMap(city)
                        navController?.navigate("city_map")
                    } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA592F)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Отправиться", color = Color.White)
                }

                Button(
                    onClick = { vm.saveRoute(route) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA592F)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Сохранить", color = Color.White)
                }
            }
        }

        // Нижняя панель
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
                    onClick = { navController?.navigate("routes") },
                    modifier = Modifier
                        .size(36.dp)
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
