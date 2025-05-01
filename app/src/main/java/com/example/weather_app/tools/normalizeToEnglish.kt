package com.example.weather_app.tools

import java.text.Normalizer

fun normalizeToEnglish(input: String?): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}