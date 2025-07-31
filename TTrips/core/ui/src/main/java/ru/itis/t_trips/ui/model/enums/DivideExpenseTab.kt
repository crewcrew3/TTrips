package ru.itis.t_trips.ui.model.enums

import androidx.annotation.StringRes
import ru.itis.t_trips.ui.R

enum class DivideExpenseTab(@StringRes val stringResourceId: Int) {
    EQUALLY(R.string.tab_text_divide_expense_equally),
    NOTEQUALLY(R.string.tab_text_divide_expense_not_equally);

    companion object {
        fun getStringResourceId(category: String): Int? {
            return DivideExpenseTab.entries.find { it.name.equals(category, ignoreCase = true) }?.stringResourceId
        }
    }
}