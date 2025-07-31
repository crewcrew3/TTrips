package ru.itis.t_trips.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.cancellation.CancellationException

inline fun <T, R> T.runCatchingNonCancellable(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun String.toUiDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateFormatterUi = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val date = LocalDate.parse(this, dateFormatter)
    return dateFormatterUi.format(date)
}
