import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings

fun openWiFiSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    } else {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true
    }
}