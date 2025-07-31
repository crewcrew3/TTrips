package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.t_trips.ui.theme.DimensionsCustom
import ru.itis.t_trips.ui.theme.IconsCustom
import ru.itis.t_trips.ui.theme.StylesCustom
import ru.itis.t_trips.ui.theme.TTripsTheme

@Composable
fun InputFieldCustom(
    labelText: String? = null,
    placeholderText: String? = null,
    isError: Boolean? = null,
    errorText: String = "",
    startValue: String = "",
    onValueChange: (String) -> Unit = {},
    isNumberKeyboard: Boolean = false,
    isPasswordField: Boolean = false,
    modifier: Modifier = Modifier
) {
    var placeholder: @Composable (() -> Unit)? = null
    placeholderText?.let {
        placeholder = {
            Text(
                text = placeholderText,
                style = StylesCustom.body3,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    var textState by remember { mutableStateOf(TextFieldValue(startValue)) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        labelText?.let {
            Label(labelText)
        }
        TextField(
            value = textState,
            onValueChange = { input ->
                if (isNumberKeyboard) { //на всякий случай, вдруг нам сюда заходят вставить текст из буфера
                    val filteredInput = input.text.filter { it.isDigit() }
                    textState = input.copy(text = filteredInput)
                    onValueChange(filteredInput)
                } else {
                    textState = input
                    onValueChange(textState.text)
                }
            },
            placeholder = placeholder,
            textStyle = StylesCustom.body3,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                errorTextColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            isError = isError ?: false,
            shape = RoundedCornerShape(DimensionsCustom.roundedCorners),
            keyboardOptions = if (isNumberKeyboard)
                KeyboardOptions(keyboardType = KeyboardType.Number) else
                KeyboardOptions.Default,
            visualTransformation = if (isPasswordField) {
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (isPasswordField) {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        if (passwordVisible) {
                            Icon(
                                imageVector = IconsCustom.visibilityIcon(),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = ""
                            )
                        } else {
                            Icon(
                                imageVector = IconsCustom.visibilityOffIcon(),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = ""
                            )
                        }
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
        )
        if (isError == true) {
            Text(
                text = errorText,
                style = StylesCustom.body2,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
            )
        }
    }
}

@Composable
fun Label(text: String) {
    Text(
        text = text,
        style = StylesCustom.body5,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(bottom = 8.dp, start = 8.dp)
    )
}


@Preview(showBackground = true, apiLevel = 34)
@Composable
fun InputFieldCustomPreview() {
    TTripsTheme {
        InputFieldCustom(
            labelText = "Лейбл",
            placeholderText = "Введите текст"
        )
    }
}
