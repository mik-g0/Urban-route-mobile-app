package com.example.myapplication2.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_routes")
data class SavedRouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routeId: Int,
    val title: String,
    val city: String,
    val description: String,
    val distance: String,
    val imageRes: Int
)