package com.example.searchdemo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CityDao {
    @Insert()
    fun insert(city: City)

    @Update
    fun update(city: City)

    @Query("SELECT * FROM cities WHERE name LIKE :query || '%' ORDER BY name ASC")
    fun searchCities(query: String): LiveData<List<City>>
}