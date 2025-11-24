package com.example.myapplication2.ui.models

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.myapplication2.ui.data.AppDatabase
import com.example.myapplication2.ui.data.RouteEntity
import com.example.myapplication2.ui.data.SavedRouteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

// JSON парсер
private val json = Json { ignoreUnknownKeys = true }

@Serializable
private data class GeoResponse(val results: List<GeoItem>?)
@Serializable
private data class GeoItem(val latitude: Double, val longitude: Double)

@Serializable
private data class WeatherResp(val current_weather: CurrentWeather?)
@Serializable
private data class CurrentWeather(val temperature: Double)


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"

    private val db = Room.databaseBuilder(application, AppDatabase::class.java, "routes_db").build()
    private val routeDao = db.routeDao()
    private val savedRouteDao = db.savedRouteDao()

    var cityTemperature by mutableStateOf("—")
    var selectedCity by mutableStateOf<String?>(null)
        private set

    var routeStops = mutableStateMapOf<String, List<Stop>>()
    val routes = mutableStateListOf<RouteDetail>()
    val savedRoutes = mutableStateListOf<RouteDetail>()

    // кеш температур по городу (сессия). Можно дополнить меткой времени для TTL.
    private val cityTemperatures = mutableStateMapOf<String, String>()

    // OkHttp client с таймаутами (быстрый и защищённый от долгих ожиданий)
    private val okClient = OkHttpClient.Builder()
        .callTimeout(1500, TimeUnit.MILLISECONDS)   // общее время вызова
        .connectTimeout(700, TimeUnit.MILLISECONDS)
        .readTimeout(1200, TimeUnit.MILLISECONDS)
        .build()

    private val cityMap = mapOf(
        "Москва" to "Moscow",
        "Санкт-Петербург" to "Saint Petersburg",
        "Казань" to "Kazan",
        "Красноярск" to "Krasnoyarsk",
        "Астрахань" to "Astrakhan",
        "Калининград" to "Kaliningrad",
        "Сочи" to "Sochi",
        "Самара" to "Samara",
        "Новосибирск" to "Novosibirsk"
    )
    val cityCoordinates = mapOf(
        "Москва" to LatLng(55.7558, 37.6173),
        "Санкт-Петербург" to LatLng(59.9343, 30.3351),
        "Казань" to LatLng(55.7903, 49.1347),
        "Красноярск" to LatLng(56.0097, 92.7917),
        "Астрахань" to LatLng(46.3496, 48.0408),
        "Калининград" to LatLng(54.7104, 20.4522),
        "Сочи" to LatLng(43.5855, 39.7203),
        "Самара" to LatLng(53.1959, 50.1002),
        "Новосибирск" to LatLng(55.0084, 82.9357)
    )
    // Состояние для выбранного города
    var selectedCityLatLng by mutableStateOf<LatLng?>(null)

    // Функция для выбора города и координат
    fun selectCityForMap(city: String) {
        selectedCity = city
        selectedCityLatLng = cityCoordinates[city]
    }
    private fun getEnglishCity(city: String): String {
        return cityMap[city] ?: city
    }
    // suspend: по названию города возвращает строку вида "+8°C" или "—"
    suspend fun fetchTempOpenMeteoOkHttp(city: String): String = withContext(Dispatchers.IO) {
        val engCity = getEnglishCity(city)
        try {
            val encoded = URLEncoder.encode(engCity, StandardCharsets.UTF_8.toString())

            // 1) Геокодинг: получаем координаты города
            val geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=$encoded&count=1"
            val geoReq = Request.Builder().url(geoUrl).get().build()
            okClient.newCall(geoReq).execute().use { geoResp ->
                if (!geoResp.isSuccessful) return@withContext "—"
                val geoBody = geoResp.body?.string().orEmpty()
                val geo = json.decodeFromString<GeoResponse>(geoBody)
                val item = geo.results?.firstOrNull() ?: return@withContext "—"
                val lat = item.latitude
                val lon = item.longitude

                // 2) Получаем текущую погоду по координатам
                val weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true"
                val wReq = Request.Builder().url(weatherUrl).get().build()
                okClient.newCall(wReq).execute().use { wResp ->
                    if (!wResp.isSuccessful) return@withContext "—"
                    val wBody = wResp.body?.string().orEmpty()
                    val w = json.decodeFromString<WeatherResp>(wBody)
                    val temp = w.current_weather?.temperature ?: return@withContext "—"
                    val sign = if (temp > 0) "+" else ""
                    val result = "${sign}${temp.toInt()}°C"
                    Log.d("MainViewModel", "City=$city, English=$engCity, temp=$result")
                    return@withContext result
                }
            }
        } catch (e: Exception) {
            Log.w("MainViewModel", "fetchTempOpenMeteoOkHttp failed for city=$city", e)
            return@withContext "—"
        }
    }
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
            selectedCity = city

            // 1) мгновенно загрузим маршруты из БД и покажем UI с кешированной температурой (или "—")
            val entities = routeDao.getRoutesByCity(city)
            val cached = cityTemperatures[city] ?: "—"
            val list = entities.map { e ->
                RouteDetail(
                    id = e.id,
                    title = e.title,
                    city = e.city,
                    description = e.description,
                    distance = e.distance,
                    stops = routeStops[e.title] ?: listOf(),
                    imageRes = e.imageRes,
                    weather = cached
                )
            }
            routes.clear()
            routes.addAll(list)
            cityTemperature = cached

            // 2) если в кеше нет (или хочешь обновить) — запустим быстрый фоновой запрос
            if (cached == "—") {
                // fetch в фоне; функция имеет встроенные таймауты
                val temp = fetchTempOpenMeteoOkHttp(city)
                // записываем в кеш
                cityTemperatures[city] = temp
                cityTemperature = temp

                if (temp != "—") {
                    // Обновляем существующие элементы routes (копирование с новым weather)
                    val updated = routes.map { r -> if (r.city == city) r.copy(weather = temp) else r }
                    routes.clear()
                    routes.addAll(updated)
                }
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
    override fun onCleared() {
        super.onCleared()
        // okHttp не требует явного close(), но можно по желанию очищать ресурсы
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