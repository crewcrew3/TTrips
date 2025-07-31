package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun PrimaryButtonCustom(
    onBtnText: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    btnHeight: Dp = DimensionsCustom.btnHeight,
    textStyle: TextStyle = StylesCustom.body3,
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(btnHeight)
    ) {
        Text(
            text = onBtnText,
            style = textStyle,
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun PrimaryButtonCustomPreview() {
    TTripsTheme {
        PrimaryButtonCustom(
            onBtnText = "Нажми",
            onClick = {}
        )
    }
}