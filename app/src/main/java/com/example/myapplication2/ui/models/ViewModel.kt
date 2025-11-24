package com.example.myapplication2.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication2.R
import androidx.compose.runtime.mutableStateListOf
import com.example.myapplication2.ui.screens.RouteDetail
import com.example.myapplication2.ui.screens.Stop

class MainViewModel : ViewModel() {
    var selectedCity by mutableStateOf<String?>(null)
        private set

    val routes = mutableStateListOf<RouteDetail>()

    val savedRoutes = mutableStateListOf<RouteDetail>()

    init {
        routes.addAll(sampleRoutes())
    }

    fun selectCity(city: String) {
        selectedCity = city
    }

    fun saveRoute(route: RouteDetail) {
        if (!savedRoutes.contains(route)) {
            savedRoutes.add(route)
        }
    }

    fun removeSavedRoute(route: RouteDetail) {
        savedRoutes.remove(route)
    }

    private fun sampleRoutes(): List<RouteDetail> {
        return listOf(
            RouteDetail(
                title = "Пешее путешествие по достопримечательностям у берега Енисея в Центральном районе",
                city = "Красноярск",
                weather = "+12",
                description = "Короткое описание маршрута у берега Енисея...",
                distance = "1.6 км",
                stops = listOf(Stop("Набережная Енисея"), Stop("Городской театр")
                ),
                imageRes = R.drawable.route1
            ),
            RouteDetail(
                title = "Прогулка по Театральной площади Красноярска",
                city = "Красноярск",
                weather = "+12",
                description = "Описание прогулки по Театральной площади...",
                distance = "0.7 км",
                stops = listOf(Stop("Театр"), Stop("Памятник")),
                imageRes = R.drawable.route2
            ),
            RouteDetail(
                title = "Маршрут по Восточным столбам",
                city = "Красноярск",
                weather = "+12",
                description = "Описание маршрута по столбам...",
                distance = "1.9 км",
                stops = listOf(Stop("Столбы"), Stop("Смотровая площадка")),
                imageRes = R.drawable.route3
            ),
            RouteDetail(
                title = "Экскурсия по самым главным музеям Красноярска",
                city = "Красноярск",
                weather = "+12",
                description = "Маршрут по музеям города...",
                distance = "2.4 км",
                stops = listOf(Stop("Музей 1"), Stop("Музей 2")),
                imageRes = R.drawable.route4
            )
        )
    }
}
