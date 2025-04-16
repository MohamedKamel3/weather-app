package com.example.weather_app.Composable

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather_app.R


@Composable
fun BackButton(
    context: Context
) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .size(55.dp)
            .border(
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp),
                width = 3.dp
            )
            .clickable {
                (context as Activity).finish()
            }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Back",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}