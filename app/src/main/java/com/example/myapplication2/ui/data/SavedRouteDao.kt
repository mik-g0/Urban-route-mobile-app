package com.example.myapplication2.ui.data

import androidx.room.*

@Dao
interface SavedRouteDao {
    @Query("SELECT * FROM saved_routes")
    suspend fun getAllSavedRoutes(): List<SavedRouteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedRoute(route: SavedRouteEntity)

    @Delete
    suspend fun deleteSavedRoute(route: SavedRouteEntity)
}