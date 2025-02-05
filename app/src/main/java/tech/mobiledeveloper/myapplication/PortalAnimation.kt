package tech.mobiledeveloper.myapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.time.Duration

@Composable
fun PortalAnimation(easing: Easing, animationDuration: Int) {
    val coroutineScope = rememberCoroutineScope()

    val cubeSize = 40.dp.value
    val blueYStart = 400.dp.value
    val blueHeight = 100.dp.value

    val redYStart = 1200.dp.value
    val redHeight = 100.dp.value

    val yOffset = remember(animationDuration, easing) { Animatable(0f) }
    val xOffset = remember(animationDuration, easing) { Animatable(0f) }
    val alpha = remember(animationDuration, easing) { Animatable(1f) }

    val bottomAnimationEnd = 2000.dp.value // Конечная точка после выхода из портала

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(900.dp)
    ) {
        val centerX = size.width / 2

        // Синий портал (вход) - овал
        drawOval(
            brush = Brush.radialGradient(listOf(Color.Blue, Color.Blue.copy(alpha = 0.2f))),
            topLeft = Offset(centerX - 200.dp.value, 400.dp.value),
            size = androidx.compose.ui.geometry.Size(400.dp.value, 100.dp.value)
        )

        // Желтый портал (выход) - овал
        drawOval(
            brush = Brush.radialGradient(listOf(Color.Red.copy(alpha = 0.6f) , Color.Red.copy(alpha = 0.2f))),
            topLeft = Offset(centerX - 200.dp.value, redYStart),
            size = androidx.compose.ui.geometry.Size(400.dp.value, redHeight)
        )

        // Куб
        translate(left = centerX - 25f + xOffset.value, top = yOffset.value) {
            drawRect(
                color = Color.DarkGray.copy(alpha = alpha.value),
                size = androidx.compose.ui.geometry.Size(cubeSize, cubeSize)
            )
        }
    }

    LaunchedEffect(animationDuration, easing) {
        val blueYCenter = blueYStart + (blueHeight / 2) - (cubeSize / 2)
        val redYCenter = redYStart + (redHeight / 2) - (cubeSize / 2)

        coroutineScope.launch {
            while (true) {
                // Куб падает в синий портал
                yOffset.animateTo(
                    targetValue = blueYCenter,
                    animationSpec = tween(animationDuration, easing = easing)
                )

                yOffset.snapTo(blueYCenter) // Фиксируем в портале

                alpha.animateTo(0f, animationSpec = tween(animationDuration / 2)) // Остаётся невидимым

                yOffset.snapTo(redYCenter) // Телепортируем куб

                alpha.animateTo(1f, animationSpec = tween(animationDuration / 2))

                // Куб выходит из желтого портала и продолжает падение вниз
                yOffset.animateTo(
                    targetValue = bottomAnimationEnd,
                    animationSpec = tween(animationDuration, easing = easing)
                )

                yOffset.snapTo(0f)
                xOffset.snapTo(0f)
            }
        }
    }
}