package ru.itis.t_trips.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import javax.inject.Inject

class GetCurrentLocaleUseCase @Inject constructor(
    private val basicUserDataStorage: BasicUserDataStorage,
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): String {
        return withContext(dispatcher) {
            basicUserDataStorage.getAppLocale() ?: "ru"
        }
    }
}