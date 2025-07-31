package ru.itis.t_trips.ui.model.enums

import androidx.annotation.StringRes
import ru.itis.t_trips.ui.R

enum class ExpenseCategory(@StringRes val stringResourceId: Int) {
    FLIGHT(R.string.tab_text_expense_category_tickets),
    HOTEL(R.string.tab_text_expense_category_hotels),
    FOOD(R.string.tab_text_expense_category_food),
    FUN(R.string.tab_text_expense_category_fun),
    INSURANCE(R.string.tab_text_expense_category_insurance),
    OTHER(R.string.tab_text_expense_category_other);

    companion object {
        fun getStringResourceId(category: String): Int? {
            return ExpenseCategory.entries.find { it.name.equals(category, ignoreCase = true) }?.stringResourceId
        }
    }
}