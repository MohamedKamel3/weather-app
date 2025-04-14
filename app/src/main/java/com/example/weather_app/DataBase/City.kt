package com.example.searchdemo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)