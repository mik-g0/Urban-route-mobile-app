package com.example.myapplication2.ui.data

import androidx.room.*

@Dao
interface RouteDao {
    @Query("SELECT * FROM routes WHERE city = :cityName")
    suspend fun getRoutesByCity(cityName: String): List<RouteEntity>

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<RouteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteEntity)

    @Delete
    suspend fun deleteRoute(route: RouteEntity)
}