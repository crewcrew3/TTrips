package ru.itis.t_trips.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ru.itis.t_trips.utils.OtherProperties

@Composable
fun ShimmerCustom(
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier
    ) {
        ShimmerEffect()
    }
}

@Composable
fun ShimmerEffect() {
    //создаем бесконечную анимацию, которая будет использоваться для шиммера
    val transition = rememberInfiniteTransition(label = OtherProperties.INF_ANIM_LABEL)
    //Создает анимированное значение от 0 до 1, которое будет использоваться для определения положения градиента.
    //0f - начальное значение анимации. 1f - конечное значение анимации.
    //animationSpec = infiniteRepeatable(...): Задает параметры анимации, которая будет повторяться бесконечно.
    //tween(durationMillis = 1500, easing = LinearEasing): тип анимации - линейная и длительность - 1500 мс.
    //repeatMode = RepeatMode.Reverse: анимация будет воспроизводиться в обратном порядке после достижения конечного значения
    val shimmerAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = OtherProperties.SHIMMER_ANIM_LABEL
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color.LightGray.copy(alpha = 0.3f), Color.LightGray.copy(alpha = 0.6f)),
                    start = Offset.Zero, //начальная точка градиента (левый верхний угол)
                    //Значение shimmerAnimation варьируется от 0f до 1f, что означает, что оно принимает значения в диапазоне от 0 до 1.
                    //Умножая его на 1000, мы масштабируем это значение до диапазона от 0 до 1000. Это позволяет градиенту перемещаться по оси X и Y на достаточное расстояние, чтобы создать заметный эффект.
                    end = Offset(shimmerAnimation * 1000, shimmerAnimation * 1000) //конечная точка градиента
                )
            )
    )
}