import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import java.util.Locale

fun getFullLocationName(
    context: Context,
    latitude: Double,
    longitude: Double,
    callback: (String) -> Unit
) {
    val geocoder = Geocoder(context, Locale.getDefault())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                if (addresses.isNotEmpty()) {
                    val city = addresses[0].locality ?: ""
                    val country = addresses[0].countryName ?: ""
                    callback(listOf(city, country).filter { it.isNotEmpty() }.joinToString(", "))
                } else {
                    callback("Unknown Location")
                }
            }
        })
    } else {
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality ?: ""
                val country = addresses[0].countryName ?: ""
                callback(listOf(city, country).filter { it.isNotEmpty() }.joinToString(", "))
            } else {
                callback("Unknown Location")
            }
        } catch (e: Exception) {
            Log.e("GeocoderError", "Error getting location: ${e.message}")
            callback("Location not found")
        }
    }
}