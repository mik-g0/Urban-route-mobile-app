package com.example.myapplication2.ui.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [RouteEntity::class, SavedRouteEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun savedRouteDao(): SavedRouteDao
}