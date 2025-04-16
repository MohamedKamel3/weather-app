package com.example.weather_app.Composable

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.searchdemo.database.City
import com.example.searchdemo.database.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    database: CityDatabase
) {
    // Sample list of cities - in a real app, this would come from a data source
    val allCities = listOf<City>()

    var searchQuery by remember { mutableStateOf("") }
    var filteredCities by remember { mutableStateOf(allCities) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var emtpyList = listOf<City>()


    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Spacer(modifier = Modifier.height(10.dp))

        // Search bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            BackButton(context = context)
            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    scope.launch {
                        val results = if (query.isEmpty()) {
                            emtpyList
                        } else {
                            withContext(Dispatchers.IO) {
                                database.cityDao().searchCities(query)
                            }
                        }
                        filteredCities = results
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search for a city") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray,
                    cursorColor = Color.Black
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // List of cities
        if (filteredCities.isEmpty()) {
            Text(
                text = "No cities found",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredCities) { city ->
                    Log.d("CityItem", "City: $city")
                    CityItem(city = "${city.name}, ${city.country}", context = context)
                }
            }
        }
    }
}