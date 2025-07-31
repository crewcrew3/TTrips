package ru.itis.t_trips.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.t_trips.ui.model.enums.ExpenseCategory
import ru.itis.t_trips.ui.theme.StylesCustom

@Composable
fun ExpenseCategoryRadioGroup(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ExpenseCategory.entries.forEach { category ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (category.name == selectedCategory),
                        onClick = { onCategorySelected(category.name) }
                    )
                    .padding(bottom = 8.dp, start = 8.dp)
            ) {
                RadioButton(
                    selected = (category.name == selectedCategory),
                    onClick = { onCategorySelected(category.name) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.secondary,
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(id = category.stringResourceId),
                    style = StylesCustom.body2,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}