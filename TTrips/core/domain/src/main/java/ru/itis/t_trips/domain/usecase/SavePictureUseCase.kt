package ru.itis.t_trips.domain.usecase

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.t_trips.domain.di.qualifiers.IoDispatchers
import ru.itis.t_trips.domain.repository.PictureRepository
import ru.itis.t_trips.utils.OtherProperties
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SavePictureUseCase @Inject constructor(
    @IoDispatchers private val dispatcher: CoroutineDispatcher,
    private val pictureRepository: PictureRepository,
) {
    suspend operator fun invoke(keyPrefix: String, context: Context, sourceUri: Uri, imageId: Int) {
        withContext(dispatcher) {
            val inputStream = context.contentResolver.openInputStream(sourceUri)
            val filename = keyPrefix + imageId + OtherProperties.FILE_EXTENSION_JPG
            val file = File(context.filesDir, filename)
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            val localUrl = Uri.fromFile(file).toString() //сохраняем именно путь к картинке из нашего внутреннего хранилища, а не галерии пользователя
            when (keyPrefix) {
                OtherProperties.FILE_TRIP_PIC_PREFIX -> pictureRepository.saveTripPictureUrl(url = localUrl, tripId = imageId)
                OtherProperties.FILE_EXPENSE_PIC_PREFIX -> pictureRepository.saveExpenseReceiptPictureUrl(url = localUrl, expenseId = imageId)
                OtherProperties.FILE_PROFILE_PIC_PREFIX -> pictureRepository.saveUserPhotoUrl(localUrl)
            }
        }
    }
}