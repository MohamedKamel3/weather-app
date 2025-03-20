fun getVisibilityDescription(visibility: Double): String {
    return when {
        visibility >= 10 -> "Perfectly clear view"
        visibility in 5.0..9.99 -> "Great visibility"
        visibility in 2.0..4.99 -> "Fairly clear view"
        visibility in 1.0..1.99 -> "Hazy conditions"
        visibility in 0.5..0.99 -> "Very poor visibility"
        else -> "Extremely foggy"
    }
}