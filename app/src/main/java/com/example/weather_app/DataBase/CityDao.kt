package com.example.searchdemo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCities(cities: List<City>)

    @Query(
        """
    SELECT * FROM cities 
    WHERE name LIKE '%' || :query || '%' 
       OR country LIKE '%' || :query || '%'
    ORDER BY name ASC
"""
    )
    fun searchCities(query: String): List<City>

    @Query("SELECT * FROM cities")
    fun getAllCities(): List<City>

}