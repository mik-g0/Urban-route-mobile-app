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
import com.example.myapplication2.R
import com.example.myapplication2.ui.models.MainViewModel
import com.example.myapplication2.ui.models.RouteDetail


@Composable
fun MyTripScreen(
    route: RouteDetail,
    navController: NavHostController,
    vm: MainViewModel
) {
    val horizontalPadding = 12.dp
    val topHeight = 56.dp
    val bottomHeight = 56.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F0EB))
    ) {
        // Верхняя панель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topHeight)
                .background(Color(0xFF9FB7C9))
                .padding(horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Моё путешествие", fontSize = 20.sp, color = Color.White)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.map),
                    contentDescription = "Маршрут на карте",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Нижняя панель
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomHeight)
                .background(Color(0xFF90AFC5))
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
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "All Routes")
                }

                IconButton(
                    onClick = { navController.navigate("saved_routes") },
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Saved Routes", tint = Color.Red)
                }
            }
        }
    }
}
