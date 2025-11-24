package com.example.myapplication2.ui.models

data class RouteDetail(
    val title: String,
    val city: String,
    val weather: String,
    val description: String,
    val distance: String,
    val stops: List<Stop>,
    val imageRes: Int
)