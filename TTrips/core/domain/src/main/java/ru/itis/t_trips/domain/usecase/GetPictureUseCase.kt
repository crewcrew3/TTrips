package ru.itis.t_trips.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.PictureRepository
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

class GetPictureUseCase @Inject constructor(
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
    private val pictureRepository: PictureRepository
) {
    suspend operator fun invoke(keyPrefix: String, imageId: Int = 0): String? {
        return withContext(dispatcher) {
            when (keyPrefix) {
                OtherProperties.FILE_PROFILE_PIC_PREFIX -> pictureRepository.getUserPhotoUrl()
                OtherProperties.FILE_TRIP_PIC_PREFIX -> pictureRepository.getTripPictureUrlById(imageId)
                OtherProperties.FILE_EXPENSE_PIC_PREFIX -> pictureRepository.getExpenseReceiptPictureUrlById(imageId)
                else -> throw IllegalArgumentException(ExceptionCode.UNKNOWN_PICTURE_KEY)
            }
        }
    }
}