
<h1 align="center">
  <br>
 <img src="app/src/main/res/drawable/baklava_team.png" alt="Baklava" width="300" ></img>
</h1>
<h1 align="center">
 Kotlin Weather App by ( Baklava Team )
</h1>
<p align="center">
   • <a href="#figma-design">Figma Design</a>
   • <a href="#📌key-features">Key Features</a>
   • <a href="#how-to-use">How To Use</a>
  </p>

 <img src="Screenshot 2025-03-02 185413.png" alt="Main" width="200" ></img>
  <img src="Screenshot 2025-03-02 185434.png" alt="first" width="200" ></img>
    <img src="Screenshot 2025-03-12 220851.png" alt="first" width="200" ></img>

## Figma Design 

 <img src="app/src/main/res/drawable/Screenshot 2025-03-20 233120.png" alt="first" ></img>


  <h1>🌤 Kotlin Weather App</h1>
    <p>This is a modern Weather App built using <strong>Kotlin</strong>, integrating <strong>Tomorrow.io API</strong> with <strong>Retrofit</strong>. The UI is designed in <strong>Figma</strong> and follows MVVM architecture for clean and maintainable code.</p>
    
  📌## Key Features
    <ul>
        <li><strong>Real-Time Weather Data:</strong> Temperature, humidity, wind speed, visibility.</li>
        <li><strong>Hourly & 5-Day Forecast:</strong> View upcoming weather conditions.</li>
        <li><strong>Search & Multiple Locations:</strong> Find weather for different cities.</li>
        <li><strong>Offline Access:</strong> Stores weather data using Room Database.</li>
        <li><strong>Dark & Light Mode:</strong> Adaptive UI with Material Design 3.</li>
        <li><strong>Animations:</strong> Smooth UI interactions with Lottie & MotionLayout.</li>
    </ul>
    
 ##🛠 Tech Stack & Tools
    <ul>
        <li><strong>Development:</strong> Kotlin, Android Studio, Jetpack Compose/XML</li>
        <li><strong>Networking:</strong> Retrofit + Gson, OkHttp</li>
        <li><strong>State Management:</strong> MVVM, LiveData, StateFlow</li>
        <li><strong>Storage:</strong> Room Database, DataStore</li>
        <li><strong>Location Services:</strong> FusedLocationProviderClient</li>
        <li><strong>Testing:</strong> JUnit, Espresso</li>
        <li><strong>CI/CD:</strong> Firebase Crashlytics, Fastlane</li>
    </ul>
    
   ##🛠 How It Works
    <ol>
        <li><strong>Launch the App:</strong> Fetches current location & weather.</li>
        <li><strong>View Weather Details:</strong> Displays temperature, wind speed, and humidity.</li>
        <li><strong>Check Forecast:</strong> Hourly & 5-day updates.</li>
        <li><strong>Search Locations:</strong> Enter a city name for weather info.</li>
        <li><strong>Toggle Units:</strong> Switch between °C & °F.</li>
    </ol>
    
 ##📂 API Integration (Retrofit Example)
    <pre>
<code>
interface WeatherService {
    @GET("/v4/weather/forecast")
    suspend fun getWeather(@Query("location") location: String, @Query("apikey") apiKey: String): Response<WeatherResponse>
}
    </code>
    </pre>
    
 ##🚀 Installation & Setup
    <ol>
        <li>Clone this repository: <code>git clone https://github.com/your-repo.git</code></li>
        <li>Open in Android Studio.</li>
        <li>Add your <strong>Tomorrow.io API Key</strong> in <code>gradle.properties</code>.</li>
        <li>Run the project on an emulator or physical device.</li>
    </ol>


## Support

<a href="https://buymeacoffee.com/mohamedmkaj" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>


---
