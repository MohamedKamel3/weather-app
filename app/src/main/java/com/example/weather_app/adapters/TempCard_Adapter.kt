package com.example.weather_app.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Models.tempcard
import com.example.weather_app.R

class TempCard_Adapter(val activity: Activity, val data: ArrayList<tempcard>) :
    RecyclerView.Adapter<TempCard_Adapter.MVH>() {
    class MVH(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.TempTempCard_IV)
        val temp = view.findViewById<TextView>(R.id.TempTempCard_TV)
        val time = view.findViewById<TextView>(R.id.TimeTempCard_TV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVH {
        val view = activity.layoutInflater.inflate(R.layout.tempcard, parent, false)
        return MVH(view)
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: MVH, position: Int) {

        holder.icon.setImageResource(data[position].icon)
        holder.temp.text = data[position].temp
        holder.time.text = data[position].time
    }
}