package ru.itis.t_trips.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import ru.itis.t_trips.ui.theme.DimensionsCustom

@Composable
fun ImageCustom(
    url: String = "https://avatars.mds.yandex.net/i?id=ea0a1c823bbecc99697a118d3959b166e11bbc39-5173517-images-thumbs&n=13", //временная заглушка
    contentScale: ContentScale = ContentScale.Crop,
    imageShape: Shape = RoundedCornerShape(DimensionsCustom.roundedCornersSmall),
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = url,
        contentDescription = "",
        modifier = modifier
            .clip(imageShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentScale = contentScale,
//        placeholder = painterResource(R.drawable.placeholder),
//        error = painterResource(R.drawable.error_image)
    )
}