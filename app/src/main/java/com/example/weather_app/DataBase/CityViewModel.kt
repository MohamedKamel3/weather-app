package com.example.searchdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.searchdemo.database.City
import com.example.searchdemo.database.CityDatabase

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CityDatabase.getInstance(application)

    fun searchCities(c: String) = repository.cityDao().searchCities(c)

    fun insertCities(city: List<City>) = repository.cityDao().insertCities(city)
}