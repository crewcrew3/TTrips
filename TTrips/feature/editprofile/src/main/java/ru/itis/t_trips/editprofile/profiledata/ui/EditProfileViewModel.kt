package ru.itis.t_trips.editprofile.profiledata.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.domain.usecase.SavePictureUseCase
import ru.itis.t_trips.domain.usecase.user.UpdateUserProfileUseCase
import ru.itis.t_trips.editprofile.ProfileFormState
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEffect
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenEvent
import ru.itis.t_trips.editprofile.profiledata.EditProfileScreenState
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.OtherProperties
import ru.itis.t_trips.utils.ValidatorHelper
import javax.inject.Inject

@HiltViewModel
internal class EditProfileViewModel @Inject constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val savePictureUseCase: SavePictureUseCase,
    private val navigator: Navigator,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<EditProfileScreenState>(value = EditProfileScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _formState = MutableStateFlow(value = ProfileFormState())
    val formState = _formState.asStateFlow()

    private val _pageEffect = MutableSharedFlow<EditProfileScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: EditProfileScreenEvent, context: Context? = null) {
        when (event) {
            is EditProfileScreenEvent.OnBackBtnClick -> navigator.popBackStack()
            is EditProfileScreenEvent.OnSaveBtnClick -> saveNewProfileData(
                firstName = event.firstName,
                lastName = event.lastName,
                uri = event.uri,
                context = context
            )
            is EditProfileScreenEvent.OnFormFieldChanged -> {
                _formState.update { state ->
                    state.copy(
                        firstName = event.firstName ?: state.firstName,
                        lastName = event.lastName ?: state.lastName,
                        uri = event.uri ?: state.uri,
                    )
                }
            }
        }
    }

    private fun saveNewProfileData(firstName: String, lastName: String, uri: Uri?, context: Context?) {
        viewModelScope.launch {
            if (validateProfileForm(firstName = firstName, lastName = lastName)) {
                runCatching {
                    updateUserProfileUseCase(firstName = firstName, lastName = lastName)
                }.onSuccess { profileModel ->
                    _pageEffect.emit(EditProfileScreenEffect.Message(R.string.msg_update_data_success))
                    if (uri != null && context != null) {
                        savePicture(context, uri, profileModel.id)
                    }
                    navigator.toProfileScreen()
                }.onFailure { exception ->
                    val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                    _pageEffect.emit(
                        EditProfileScreenEffect.Message(
                            message = messageResId
                        )
                    )
                }
            }
        }
    }

    private suspend fun validateProfileForm(
        firstName: String,
        lastName: String,
    ): Boolean {
        var errorCounts = 0
        if (!ValidatorHelper.validateFirstName(firstName)) {
            _pageEffect.emit(EditProfileScreenEffect.ErrorFirstNameInput)
            errorCounts++
        }
        if (!ValidatorHelper.validateLastName(lastName)) {
            _pageEffect.emit(EditProfileScreenEffect.ErrorLastNameInput)
            errorCounts++
        }
        return errorCounts == 0
    }

    private fun savePicture(context: Context, sourceUri: Uri, imageId: Int) {
        viewModelScope.launch {
            runCatching {
                savePictureUseCase(
                    keyPrefix = OtherProperties.FILE_PROFILE_PIC_PREFIX,
                    context = context,
                    sourceUri = sourceUri,
                    imageId = imageId,
                )
            }.onFailure {
                _pageEffect.emit(
                    EditProfileScreenEffect.Message(
                        R.string.exception_msg_picture
                    )
                )
            }
        }
    }
}