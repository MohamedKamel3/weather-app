package com.example.weather_app.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.R

class WeatherAdapter(
    private val activity: Activity,
    private var weatherList: MutableList<WeatherData>,
    private val onItemClick: (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityName: TextView = view.findViewById(R.id.city)
        val temperature: TextView = view.findViewById(R.id.Temperature_TV)
        val description: TextView = view.findViewById(R.id.describe)
        val icon: ImageView = view.findViewById(R.id.imgWeatherIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]

        holder.cityName.text = weather.cityName
        holder.temperature.text = getFormattedTemperature(weather)
        holder.description.text = weather.description
        holder.icon.setImageResource(weather.icon)
        holder.itemView.setOnClickListener { onItemClick(weather) }
    }

    fun deleteItem(position: Int) {
        weatherList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = weatherList.size

    /**
     * Updates the adapter with new weather data.
     */
    fun updateData(newList: List<WeatherData>) {
        weatherList.clear()
        weatherList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getFormattedTemperature(weather: WeatherData): String {
        val isCelsius = SharedPrefHelper.getTemperatureUnit(activity)
        return if (isCelsius) {
            "${weather.tempCelsius}°C"
        } else {
            "${weather.tempFahrenheit}°F"
        }
    }
}