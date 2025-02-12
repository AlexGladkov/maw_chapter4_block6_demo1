package tech.mobiledeveloper.myapplication

import android.graphics.Paint.Align
import android.transition.ArcMotion
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PortalAnimationScreen(
    modifier: Modifier = Modifier
) {
    var selectedEasing by remember { mutableStateOf("EaseInOut") }
    var slowMotion by remember { mutableStateOf(false) }
    val easingMap: Map<String, Easing> = mapOf(
        "Linear" to LinearEasing,
        "EaseIn" to EaseIn,
        "EaseOut" to EaseOut,
        "EaseInOut" to EaseInOut
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Выберите интерполяцию", color = Color.Black)

            Spacer(modifier = Modifier.weight(1f))

            DropdownEasingSelector(
                easingOptions = easingMap.keys.toList(),
                selectedEasing = selectedEasing,
                onSelectionChanged = { selectedEasing = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SlowMotionToggle(slowMotion) { slowMotion = it }

        Spacer(modifier = Modifier.height(16.dp))

        PortalAnimation(
            easing = easingMap[selectedEasing] ?: LinearEasing,
            animationDuration = if (slowMotion) 8000 else 800
        )
    }
}

@Composable
fun DropdownEasingSelector(
    easingOptions: List<String>,
    selectedEasing: String,
    onSelectionChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selectedEasing,
            color = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF333333))
                .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            easingOptions.forEach { easing ->
                DropdownMenuItem(
                    text = { Text(easing) },
                    onClick = {
                        onSelectionChanged(easing)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SlowMotionToggle(slowMotion: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Замедленный режим", color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = slowMotion,
            onCheckedChange = { onToggle(it) }
        )
    }
}