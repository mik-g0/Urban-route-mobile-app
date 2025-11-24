package com.example.myapplication2.ui.models

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication2.ui.data.AppDatabase
import com.example.myapplication2.ui.data.RouteEntity
import com.example.myapplication2.ui.data.SavedRouteEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(application, AppDatabase::class.java, "routes_db").build()
    private val routeDao = db.routeDao()
    private val savedRouteDao = db.savedRouteDao()

    var selectedCity by mutableStateOf<String?>(null)
        private set

    var routeStops = mutableStateMapOf<String, List<Stop>>()
    val routes = mutableStateListOf<RouteDetail>()
    val savedRoutes = mutableStateListOf<RouteDetail>()

    // =================== CRUD для маршрутов ===================

    fun addRoute(route: RouteEntity) {
        viewModelScope.launch {
            routeDao.insertRoute(route)
            if (route.city == selectedCity) loadRoutesForCity(route.city)
        }
    }

    fun updateRoute(route: RouteEntity) {
        viewModelScope.launch {
            routeDao.insertRoute(route)
            if (route.city == selectedCity) loadRoutesForCity(route.city)
        }
    }

    fun deleteRoute(route: RouteEntity) {
        viewModelScope.launch {
            routeDao.deleteRoute(route)
            if (route.city == selectedCity) loadRoutesForCity(route.city)
        }
    }

    fun loadRoutesForCity(city: String) {
        viewModelScope.launch {
            try {
                selectedCity = city
                val entities = routeDao.getRoutesByCity(city)
                val list = entities.map { entity ->
                    RouteDetail(
                        id = entity.id,                  // <- вот это нужно добавить
                        title = entity.title,
                        city = entity.city,
                        description = entity.description,
                        distance = entity.distance,
                        stops = routeStops[entity.title] ?: listOf(),
                        imageRes = entity.imageRes
                    )
                }
                routes.clear()
                routes.addAll(list)
            } catch (e: Exception) {
                e.printStackTrace() // лог ошибки, чтобы понять причину
            }
        }
    }


    // =================== CRUD для сохранённых маршрутов ===================

    fun saveRoute(route: RouteDetail) {
        viewModelScope.launch {
            val entity = SavedRouteEntity(
                routeId = 0,
                title = route.title,
                city = route.city,
                description = route.description,
                distance = route.distance,
                imageRes = route.imageRes
            )
            savedRouteDao.insertSavedRoute(entity)
            loadSavedRoutes()
        }
    }

    fun loadSavedRoutes() {
        viewModelScope.launch {
            val entities = savedRouteDao.getAllSavedRoutes()
            val list = entities.map { entity ->
                RouteDetail(
                    id = entity.id,                  // <- вот это нужно добавить
                    title = entity.title,
                    city = entity.city,
                    description = entity.description,
                    distance = entity.distance,
                    stops = routeStops[entity.title] ?: listOf(),
                    imageRes = entity.imageRes
                )
            }
            savedRoutes.clear()
            savedRoutes.addAll(list)
        }
    }

    fun updateSavedRoute(route: SavedRouteEntity) {
        viewModelScope.launch {
            savedRouteDao.insertSavedRoute(route)
            loadSavedRoutes()
        }
    }

    fun removeSavedRoute(route: RouteDetail) {
        viewModelScope.launch {
            val entities = savedRouteDao.getAllSavedRoutes()
            val entityToDelete = entities.find { it.title == route.title && it.city == route.city }
            if (entityToDelete != null) {
                savedRouteDao.deleteSavedRoute(entityToDelete)
                loadSavedRoutes()
            }
        }
    }
}


/**
 * class MainViewModel : ViewModel() {
 *     var selectedCity by mutableStateOf<String?>(null)
 *         private set
 *
 *     val routes = mutableStateListOf<RouteDetail>()
 *
 *     val savedRoutes = mutableStateListOf<RouteDetail>()
 *
 *     init {
 *         routes.addAll(sampleRoutes())
 *     }
 *
 *     fun selectCity(city: String) {
 *         selectedCity = city
 *     }
 *
 *     fun saveRoute(route: RouteDetail) {
 *         if (!savedRoutes.contains(route)) {
 *             savedRoutes.add(route)
 *         }
 *     }
 *
 *     fun removeSavedRoute(route: RouteDetail) {
 *         savedRoutes.remove(route)
 *     }
 *
 *     private fun sampleRoutes(): List<RouteDetail> {
 *         return listOf(
 *             RouteDetail(
 *                 title = "Пешее путешествие по достопримечательностям у берега Енисея в Центральном районе",
 *                 city = "Красноярск",
 *                 weather = "+12",
 *                 description = "Короткое описание маршрута у берега Енисея...",
 *                 distance = "1.6 км",
 *                 stops = listOf(Stop("Набережная Енисея"), Stop("Городской театр")
 *                 ),
 *                 imageRes = R.drawable.route1
 *             ),
 *             RouteDetail(
 *                 title = "Прогулка по Театральной площади Красноярска",
 *                 city = "Красноярск",
 *                 weather = "+12",
 *                 description = "Описание прогулки по Театральной площади...",
 *                 distance = "0.7 км",
 *                 stops = listOf(Stop("Театр"), Stop("Памятник")),
 *                 imageRes = R.drawable.route2
 *             ),
 *             RouteDetail(
 *                 title = "Маршрут по Восточным столбам",
 *                 city = "Красноярск",
 *                 weather = "+12",
 *                 description = "Описание маршрута по столбам...",
 *                 distance = "1.9 км",
 *                 stops = listOf(Stop("Столбы"), Stop("Смотровая площадка")),
 *                 imageRes = R.drawable.route3
 *             ),
 *             RouteDetail(
 *                 title = "Экскурсия по самым главным музеям Красноярска",
 *                 city = "Красноярск",
 *                 weather = "+12",
 *                 description = "Маршрут по музеям города...",
 *                 distance = "2.4 км",
 *                 stops = listOf(Stop("Музей 1"), Stop("Музей 2")),
 *                 imageRes = R.drawable.route4
 *             )
 *         )
 *     }
 * }
 */