package com.example.weather_app

import SharedPrefHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.Adapters.WeatherAdapter
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.tools.addWeatherIfNotExists

class SearchView : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivitySearchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val scale = resources.displayMetrics.density
            val desiredPx = (16 * scale + 0.5f).toInt()
            v.setPadding(
                systemBars.left + desiredPx,
                systemBars.top + desiredPx,
                systemBars.right + desiredPx,
                systemBars.bottom
            )
            insets
        }

        binding.recycler.adapter = WeatherAdapter(this, SharedPrefHelper.getWeatherList(this))

        binding.backbutton.setOnClickListener {
            finish()
        }

        binding.SeachED.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableStart = binding.SeachED.compoundDrawables[0] // Left Drawable

                if (drawableStart != null) {
                    val drawableWidth = drawableStart.bounds.width()
                    val touchX = event.rawX

                    // Check if the touch is within the drawableStart area
                    if (touchX <= (binding.SeachED.left + drawableWidth + binding.SeachED.paddingStart)) {
                        val cityName = binding.SeachED.text.toString().trim()

                        if (cityName.isNotEmpty()) {

                            addWeatherIfNotExists(this, newWeatherData)

                            val intent = Intent()
                            intent.putExtra("CITY_NAME", cityName)
                            setResult(RESULT_OK, intent)

                            finish()
                        }
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

//        // 🔹 عند البحث عن مدينة جديدة
//        binding.searchButton.setOnClickListener {
//            val cityName = binding.searchInput.text.toString().trim()
//
//            if (cityName.isNotEmpty()) {
//                // 1️⃣ حفظ المدينة الجديدة في SharedPreferences
//                val newWeatherData =
//                    WeatherData(cityName, 0, "Loading...") // البيانات ستتحدث لاحقًا
//                addWeatherIfNotExists(this, newWeatherData)
//
//                // 2️⃣ إرسال اسم المدينة إلى MainActivity
//                val intent = Intent()
//                intent.putExtra("CITY_NAME", cityName)
//                setResult(RESULT_OK, intent)
//
//                // 3️⃣ إغلاق SearchView والعودة إلى MainActivity
//                finish()
//            }
//        }
    }
}