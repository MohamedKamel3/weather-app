package com.example.searchdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Fix: Use applicationContext
                    CityDatabase::class.java,
                    "city_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun importCitiesFromExcel(context: Context, cityDao: CityDao, fileName: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val inputStream: InputStream = context.assets.open(fileName)
                    WorkbookFactory.create(inputStream).use { workbook ->
                        val sheet = workbook.getSheetAt(0) // First sheet
                        val citiesList = mutableListOf<City>()

                        for (row in sheet) {
                            if (row.rowNum == 0) continue // Skip header row

                            try {
                                val cityName = row.getCell(0)?.stringCellValue?.trim() ?: ""

                                val lat = when (row.getCell(1)?.cellType) {
                                    org.apache.poi.ss.usermodel.CellType.NUMERIC -> row.getCell(1).numericCellValue
                                    org.apache.poi.ss.usermodel.CellType.STRING -> row.getCell(1).stringCellValue.toDoubleOrNull()
                                        ?: 0.0

                                    else -> 0.0
                                }

                                val lon = when (row.getCell(2)?.cellType) {
                                    org.apache.poi.ss.usermodel.CellType.NUMERIC -> row.getCell(2).numericCellValue
                                    org.apache.poi.ss.usermodel.CellType.STRING -> row.getCell(2).stringCellValue.toDoubleOrNull()
                                        ?: 0.0

                                    else -> 0.0
                                }

                                val country = row.getCell(3)?.stringCellValue?.trim() ?: ""

                                if (cityName.isNotEmpty() && country.isNotEmpty()) {
                                    citiesList.add(
                                        City(
                                            name = cityName,
                                            country = country,
                                            lat = lat,
                                            lon = lon
                                        )
                                    )
                                }

                                // Batch insert every 500 entries
                                if (citiesList.size >= 500) {
                                    withContext(Dispatchers.IO) { // Fix: Ensure we call suspend function in coroutine
                                        cityDao.insertCities(citiesList)
                                    }
                                    citiesList.clear()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace() // Log individual row errors
                            }
                        }

                        // Insert remaining cities
                        if (citiesList.isNotEmpty()) {
                            withContext(Dispatchers.IO) { // Fix: Call inside coroutine scope
                                cityDao.insertCities(citiesList)
                            }
                        }
                    }
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}