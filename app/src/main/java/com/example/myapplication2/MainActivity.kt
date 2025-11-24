package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication2.ui.screens.CitySelectionScreen
import com.example.myapplication2.ui.models.MainViewModel
import com.example.myapplication2.ui.screens.RouteListScreen
import com.example.myapplication2.ui.screens.RouteDetailScreen
import com.example.myapplication2.ui.screens.SavedRoutesScreen
import com.example.myapplication2.ui.screens.CityMapScreen
import com.example.myapplication2.ui.screens.AddEditRouteScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp(vm: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "city_selection",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Выбор города
            composable("city_selection") {
                CitySelectionScreen(
                    vm = vm,
                    onCitySelected = { city ->
                        vm.loadRoutesForCity(city)
                        navController.navigate("routes")
                    },
                    onAddRouteClick = { navController.navigate("add_edit_route") }
                )
            }

            // Список маршрутов
            composable("routes") {
                RouteListScreen(vm = vm, navController = navController)
            }

            // Детали маршрута
            composable(
                route = "route_detail/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) { backStackEntry ->
                val idx = backStackEntry.arguments?.getInt("index") ?: 0
                val route = vm.routes.getOrNull(idx)
                if (route != null) {
                    RouteDetailScreen(route = route, navController = navController, vm = vm)
                }
            }

            // Сохраненные маршруты
            composable("saved_routes") {
                SavedRoutesScreen(vm = vm, navController = navController)
            }

            // Добавление / редактирование маршрута
            composable("add_edit_route") {
                AddEditRouteScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("city_map") {
                CityMapScreen(vm = vm)
            }

        }
    }
}