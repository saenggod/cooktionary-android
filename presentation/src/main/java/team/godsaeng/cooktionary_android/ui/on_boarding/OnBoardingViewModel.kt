package team.godsaeng.cooktionary_android.ui.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import team.godsaeng.cooktionary_android.di.coroutines.IoDispatcher
import team.godsaeng.cooktionary_android.di.network.NetworkModule
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.GoToMain
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithGoogle
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakao
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickGoogleLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickKakaoLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickSkip
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnFailureSocialLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnSuccessSocialLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiState
import team.godsaeng.cooktionary_android.util.UserInfo
import team.godsaeng.domain.model.model.verification.Verification
import team.godsaeng.domain.model.use_case.LoadStoredOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.StoreOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.VerifyUserUseCase
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val verifyUserUseCase: VerifyUserUseCase,
    private val storeOAuthPlatformUseCase: StoreOAuthPlatformUseCase,
    private val loadStoredOAuthPlatformUseCase: LoadStoredOAuthPlatformUseCase
) : ViewModel(), OnBoardingContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: OnBoardingContract.UiEvent) = when (event) {
        is OnStarted -> onStarted()

        is OnClickKakaoLogin -> onClickKakaoLogin()

        is OnClickGoogleLogin -> onClickGoogleLogin()

        is OnClickSkip -> onClickSkip()

        is OnSuccessSocialLogin -> onSuccessSocialLogin(
            platform = event.platform,
            token = event.token
        )

        is OnFailureSocialLogin -> onFailureSocialLogin()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {

        }
    }

    private fun onStarted() {
        viewModelScope.launch {
            val platform = loadStoredOAuthPlatformUseCase().first()

            if (platform == SocialLoginManager.PLATFORM_KAKAO) {
                _uiEffect.emit(LoginWithKakao)
            } else if (platform == SocialLoginManager.PLATFORM_GOOGLE) {
                _uiEffect.emit(LoginWithGoogle)
            } else {
                _uiState.update {
                    it.copy(autoLoginFailed = true)
                }
            }
        }
    }

    private fun onClickKakaoLogin() {
        viewModelScope.launch {
            _uiEffect.emit(LoginWithKakao)
        }
    }

    private fun onClickGoogleLogin() {
        viewModelScope.launch {
            _uiEffect.emit(LoginWithGoogle)
        }
    }

    private fun onClickSkip() {
        viewModelScope.launch {
            _uiEffect.emit(GoToMain)
        }
    }

    private fun onSuccessSocialLogin(
        platform: String,
        token: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            verifyUserUseCase(
                platform = platform,
                token = token
            ).handle(
                onSuccess = { verification ->
                    onSuccessServiceLogin(
                        verification = verification,
                        platform = platform
                    )
                },
                onFailure = { error ->
                    when (error.code) {
                        // todo: handle error with code
                    }
                }
            )
        }
    }

    private fun onFailureSocialLogin() {

    }

    private fun onSuccessServiceLogin(
        verification: Verification,
        platform: String
    ) {
        UserInfo.run {
            isLoggedIn = true
            name = verification.name
            email = verification.email
        }

        NetworkModule.accessToken = verification.accessToken

        viewModelScope.launch {
            if (uiState.value.autoLoginFailed) {
                withContext(ioDispatcher) {
                    storeOAuthPlatformUseCase(platform)
                }
            }

            _uiEffect.emit(GoToMain)
        }
    }

    private fun onFailureServiceLogin() {

    }
}