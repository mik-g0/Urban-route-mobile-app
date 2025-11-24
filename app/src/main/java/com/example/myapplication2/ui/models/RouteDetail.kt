package com.example.myapplication2.ui.models

data class RouteDetail(
    val id: Int,
    val title: String,
    val city: String,
    val description: String,
    val distance: String,
    val stops: List<Stop>,
    val imageRes: Int,
    val weather: String = "—"
)