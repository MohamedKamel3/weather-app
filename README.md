
<h1 align="center">
  <br>
 <img src="app/src/main/res/drawable/baklava_team.png" alt="Baklava" width="300" ></img>
</h1>
<h1 align="center">
 Kotlin Weather App by ( Baklava Team )
</h1>
<p align="center">
   • <a href="#figma-design">Figma Design</a>
   • <a href="#key-features">Key Features</a>
   • <a href="#how-to-use">How To Use</a>
  </p>

 <img src="Screenshot 2025-03-02 185413.png" alt="Main" width="200" ></img>
  <img src="Screenshot 2025-03-02 185434.png" alt="first" width="200" ></img>
    <img src="Screenshot 2025-03-12 220851.png" alt="first" width="200" ></img>

## Figma Design 

 <img src="app/src/main/res/drawable/Screenshot 2025-03-20 233120.png" alt="first" ></img>


  <h1>🌤 Kotlin Weather App</h1>
    <p>This is a modern Weather App built using <strong>Kotlin</strong>, integrating <strong>Tomorrow.io API</strong> with <strong>Retrofit</strong>. The UI is designed in <strong>Figma</strong> and follows MVVM architecture for clean and maintainable code.</p>
    
   <h2>📌 Key Features</h2>
    <ul>
        <li><strong>Real-Time Weather Data:</strong> Temperature, humidity, wind speed, visibility.</li>
        <li><strong>Hourly & 5-Day Forecast:</strong> View upcoming weather conditions.</li>
        <li><strong>Search & Multiple Locations:</strong> Find weather for different cities.</li>
        <li><strong>Offline Access:</strong> Stores weather data using Room Database.</li>
        <li><strong>Dark & Light Mode:</strong> Adaptive UI with Material Design 3.</li>
        <li><strong>Animations:</strong> Smooth UI interactions with Lottie & MotionLayout.</li>
    </ul>
    
  <h2>🛠 Tech Stack & Tools</h2>
    <ul>
        <li><strong>Development:</strong> Kotlin, Android Studio, Jetpack Compose/XML</li>
        <li><strong>Networking:</strong> Retrofit + Gson, OkHttp</li>
        <li><strong>State Management:</strong> MVVM, LiveData, StateFlow</li>
        <li><strong>Storage:</strong> Room Database, DataStore</li>
        <li><strong>Location Services:</strong> FusedLocationProviderClient</li>
        <li><strong>Testing:</strong> JUnit, Espresso</li>
        <li><strong>CI/CD:</strong> Firebase Crashlytics, Fastlane</li>
    </ul>
    
   <h2>🛠 How It Works</h2>
    <ol>
        <li><strong>Launch the App:</strong> Fetches current location & weather.</li>
        <li><strong>View Weather Details:</strong> Displays temperature, wind speed, and humidity.</li>
        <li><strong>Check Forecast:</strong> Hourly & 5-day updates.</li>
        <li><strong>Search Locations:</strong> Enter a city name for weather info.</li>
        <li><strong>Toggle Units:</strong> Switch between °C & °F.</li>
    </ol>
    
  <h2>📂 API Integration (Retrofit Example)</h2>
    <pre>
<code>
interface WeatherService {
    @GET("/v4/weather/forecast")
    suspend fun getWeather(@Query("location") location: String, @Query("apikey") apiKey: String): Response<WeatherResponse>
}
    </code>
    </pre>
    
  <h2>🚀 Installation & Setup</h2>
    <ol>
        <li>Clone this repository: <code>git clone https://github.com/your-repo.git</code></li>
        <li>Open in Android Studio.</li>
        <li>Add your <strong>Tomorrow.io API Key</strong> in <code>gradle.properties</code>.</li>
        <li>Run the project on an emulator or physical device.</li>
    </ol>

## Key Features

* Basic Arithmetic Operations: The calculator supports basic arithmetic operations such as addition, subtraction, multiplication, and division.
* Numeric Keypad: It includes a standard numeric keypad with digits from 0 to 9.
* Simple Interface: The interface is straightforward, displaying the current input and result clearly.
* Real-time Calculation: As seen in the second screenshot, the calculator updates the display in real-time as numbers and operations are entered.


## How To Use

unzip it and run main file ❤️


* Entering Numbers: Tap the numeric buttons (0-9) to input numbers.
* Performing Operations: Use the appropriate operation buttons (+, -, *, /) to perform calculations.
* Viewing Results: The result of the calculation is displayed at the top of the screen. For example, in the second screenshot, "91+7" is shown, indicating that the user has entered 91 and is adding 7 to it.
* Clearing Input: Although not visible in the screenshots, typically, there would be a clear (C) button to reset the input or clear the current calculation.
* This calculator is designed for basic calculations, making it easy to use for everyday arithmetic tasks.


## Support

<a href="https://buymeacoffee.com/mohamedmkaj" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>


---
