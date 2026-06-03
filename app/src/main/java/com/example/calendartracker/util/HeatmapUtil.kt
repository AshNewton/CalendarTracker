package com.example.calendartracker.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerType
import com.example.calendartracker.data.TrackerValue

@Composable
fun getHeatColor(tracker: TrackerDefinition?, value: TrackerValue?): Color {
    if (value == null) {
        return MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
    }

    if (tracker == null) {
        return MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
    }

    return when (tracker.type) {
        TrackerType.BOOL -> {
            val bool = value.value.toBoolean()
            if (bool) {
                Color.hsv(
                    hue = 120f,
                    saturation = 0.7f,
                    value = 0.85f
                )
            } else {
                Color.hsv(
                    hue = 0f,
                    saturation = 0.7f,
                    value = 0.85f
                )
            }
        }

        TrackerType.NUMBER -> {
            val num = value.value?.toFloatOrNull()
                ?: return Color.Gray

            val min = tracker.minValue?.toFloat() ?: 0f
            val max = tracker.maxValue?.toFloat() ?: 1f

            val normalized = normalize(num, min, max)

            val t =
                if (tracker.higherIsBetter == false)
                    1f - normalized
                else
                    normalized

            val hue = 120f * t

            Color.hsv(
                hue = hue,
                saturation = 0.7f,
                value = 0.85f
            )
        }

        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

fun normalize(value: Float, min: Float, max: Float): Float {
    return ((value - min) / (max - min)).coerceIn(0f, 1f)
}