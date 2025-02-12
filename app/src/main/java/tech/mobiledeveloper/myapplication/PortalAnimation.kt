package tech.mobiledeveloper.myapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PortalAnimation(easing: Easing, animationDuration: Int) {

    val cubeSize = 40.dp.value
    val blueYStart = 400.dp.value
    val blueHeight = 100.dp.value
    val blueWidth = 400.dp.value

    val redYStart = 1200.dp.value
    val redHeight = 100.dp.value
    val redWidth = 400.dp.value

    val yOffset = remember(animationDuration, easing) { Animatable(0f) }
    val xOffset = remember(animationDuration, easing) { Animatable(0f) }
    val alpha = remember(animationDuration, easing) { Animatable(1f) }

    Canvas(
        modifier = Modifier.fillMaxSize()
            .height(900.dp)
    ) {
        val centerX = size.width / 2

        drawOval(
            brush = Brush.radialGradient(listOf(Color.Blue, Color.Blue.copy(alpha = 0.2f))),
            topLeft = Offset(centerX - 200.dp.value, blueYStart),
            size = Size(blueWidth, blueHeight)
        )

        drawOval(
            brush = Brush.radialGradient(listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.2f))),
            topLeft = Offset(centerX - 200.dp.value, redYStart),
            size = Size(redWidth, redHeight)
        )

        translate(left = centerX - (cubeSize / 2) + xOffset.value, top = yOffset.value) {
            drawRect(
                color = Color.DarkGray.copy(alpha = alpha.value),
                size = Size(cubeSize, cubeSize)
            )
        }
    }

    LaunchedEffect(animationDuration, easing) {
        val blueYCenter = blueYStart + (blueHeight / 2) - (cubeSize / 2)
        val redYCenter = redYStart + (redHeight / 2) - (cubeSize / 2)

        launch {
            while (true) {
                yOffset.animateTo(
                    targetValue = blueYCenter,
                    animationSpec = tween(animationDuration, easing = easing)
                )

                yOffset.snapTo(blueYCenter)

                alpha.animateTo(0f, animationSpec = tween(animationDuration, easing = easing))

                yOffset.snapTo(redYCenter)

                alpha.animateTo(1f, animationSpec = tween(animationDuration, easing = easing))

                yOffset.animateTo(
                    targetValue = 2000.dp.value,
                    animationSpec = tween(animationDuration, easing = easing)
                )

                yOffset.snapTo(0f)
                xOffset.snapTo(0f)
            }
        }
    }
}