package com.example.weather_app.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.R

class Forecast_Adapter(val activity: Activity, val data: ArrayList<VDaysForecast>) :
    RecyclerView.Adapter<Forecast_Adapter.MVH>() {
    class MVH(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.imgWeatherIcon)
        val temp = view.findViewById<TextView>(R.id.tvTemperature)
        val date = view.findViewById<TextView>(R.id.tvDate)
        val day = view.findViewById<TextView>(R.id.tvDay)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_10day, parent, false)
        return MVH(view)
    }

    override fun getItemCount() = 5

    override fun onBindViewHolder(holder: MVH, position: Int) {
        holder.icon.setImageResource(data[position].icon)
        holder.temp.text = data[position].temperature.toString()
        holder.date.text = data[position].date
        holder.day.text = data[position].day
    }
}