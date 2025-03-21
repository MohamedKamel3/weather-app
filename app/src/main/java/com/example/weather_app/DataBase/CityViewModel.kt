package com.example.searchdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.searchdemo.database.City
import com.example.searchdemo.database.CityDatabase

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CityDatabase.getInstance(application)

    fun searchCities(c: City) = repository.cityDao().searchCities("${c.name},${c.country}")

    fun insertCity(city: City) = repository.cityDao().insert(city)
}