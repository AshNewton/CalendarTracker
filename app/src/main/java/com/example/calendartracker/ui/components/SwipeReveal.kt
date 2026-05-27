package com.example.calendartracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeReveal(
    modifier: Modifier = Modifier,
    actionsWidth: Float = 160f,
    swipeThreshold: Float = 0.5f,
    actions: @Composable RowScope.(progress: Float) -> Unit,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(180),
        label = "offset"
    )

    val maxOffset = -actionsWidth
    val progress = (abs(animatedOffset) / actionsWidth).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            actions(progress)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .pointerInput(actionsWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount)
                                .coerceIn(maxOffset, 0f)
                        },
                        onDragEnd = {
                            offsetX =
                                if (abs(offsetX) > actionsWidth * swipeThreshold)
                                    maxOffset
                                else
                                    0f
                        },
                        onDragCancel = {
                            offsetX = 0f
                        }
                    )
                }
        ) {
            content()
        }
    }
}