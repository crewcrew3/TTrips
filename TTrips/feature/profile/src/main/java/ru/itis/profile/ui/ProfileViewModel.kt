package ru.itis.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.profile.ProfileScreenEffect
import ru.itis.profile.ProfileScreenEvent
import ru.itis.profile.ProfileScreenState
import ru.itis.t_trips.common.ExceptionHandler
import ru.itis.t_trips.ui.R
import ru.itis.t_trips.domain.exception.AuthenticationException
import ru.itis.t_trips.domain.usecase.ChangeLocaleUseCase
import ru.itis.t_trips.domain.usecase.GetCurrentLocaleUseCase
import ru.itis.t_trips.domain.usecase.GetPictureUseCase
import ru.itis.t_trips.domain.usecase.user.GetUserProfileUseCase
import ru.itis.t_trips.domain.usecase.auth.LogOutUserUseCase
import ru.itis.t_trips.navigation_api.navigator.ProfileNavigator
import ru.itis.t_trips.utils.ExceptionCode
import ru.itis.t_trips.utils.OtherProperties
import javax.inject.Inject

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logOutUserUseCase: LogOutUserUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    private val getCurrentLocaleUseCase: GetCurrentLocaleUseCase,
    private val profileNavigator: ProfileNavigator,
    private val getPictureUseCase: GetPictureUseCase,
    private val exceptionHandler: ExceptionHandler,
) : ViewModel() {

    private val _pageState = MutableStateFlow<ProfileScreenState>(value = ProfileScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _currentLocale = MutableStateFlow("")
    val currentLocale = _currentLocale.asStateFlow()

    private val _pageEffect = MutableSharedFlow<ProfileScreenEffect>()
    val pageEffect = _pageEffect.asSharedFlow()

    fun processEvent(event: ProfileScreenEvent) {
        when (event) {
            is ProfileScreenEvent.OnInitProfile -> {
                setCurrentLocale()
                getUserProfile()
            }
            is ProfileScreenEvent.OnEditProfileBtnClick -> profileNavigator.toEditProfileScreen(firstName = event.firstName, lastName = event.lastName, userPhotoUrl = event.userPhotoUrl)
            is ProfileScreenEvent.OnPrivacyTabClick -> profileNavigator.toPrivacyScreen(event.phoneNumber)
            is ProfileScreenEvent.OnTripArchiveTabClick -> profileNavigator.toTripArchiveScreen()
            is ProfileScreenEvent.OnLogOutTabClick -> logOutUser()
            is ProfileScreenEvent.OnChangeLocale -> changeLocale(event.locale)
        }
    }

    private fun getUserProfile() {
        //для теста
        //_pageState.value = ProfileScreenState.UserProfileResult(result = UserProfileModel(1, "Вася Пупкин", "88005555535"))
        viewModelScope.launch {
            runCatching {
                _pageState.value = ProfileScreenState.Loading
                //delay(2000)
                getUserProfileUseCase()
            }.onSuccess { result ->
                val profilePhotoUrl = getPictureUseCase(OtherProperties.FILE_PROFILE_PIC_PREFIX)
                _pageState.value = ProfileScreenState.UserProfileResult(result = result, photoUrl = profilePhotoUrl)
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ProfileScreenEffect.Error(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun logOutUser() {
        viewModelScope.launch {
            runCatching {
                logOutUserUseCase()
            }.onSuccess { success ->
                if (success) {
                    profileNavigator.toAuthenticationPhoneNumberScreen()
                } else {
                    _pageEffect.emit(ProfileScreenEffect.Error(R.string.exception_msg_log_out))
                }
            }.onFailure { exception ->
                val messageResId = exceptionHandler.handleExceptionMessage(exception.message)
                _pageEffect.emit(
                    ProfileScreenEffect.Error(
                        message = messageResId
                    )
                )
            }
        }
    }

    private fun changeLocale(locale: String) {
        viewModelScope.launch {
            changeLocaleUseCase(locale)
            _currentLocale.value = locale
        }
    }

    private fun setCurrentLocale() {
        viewModelScope.launch {
            _currentLocale.value = getCurrentLocaleUseCase()
        }
    }
}