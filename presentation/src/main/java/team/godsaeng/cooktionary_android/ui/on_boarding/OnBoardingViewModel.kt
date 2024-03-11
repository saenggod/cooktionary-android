package team.godsaeng.cooktionary_android.ui.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
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
import team.godsaeng.cooktionary_android.ui.on_boarding.SocialLoginManager.Companion.PLATFORM_GOOGLE
import team.godsaeng.cooktionary_android.ui.on_boarding.SocialLoginManager.Companion.PLATFORM_KAKAO
import team.godsaeng.cooktionary_android.util.UserInfo
import team.godsaeng.cooktionary_android.util.getExceptionHandler
import team.godsaeng.domain.model.model.verification.Verification
import team.godsaeng.domain.model.use_case.user.GetGoogleAccessTokenUseCase
import team.godsaeng.domain.model.use_case.user.LoadStoredOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.user.StoreOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.user.VerifyUserUseCase
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getGoogleAccessTokenUseCase: GetGoogleAccessTokenUseCase,
    private val verifyUserUseCase: VerifyUserUseCase,
    private val storeOAuthPlatformUseCase: StoreOAuthPlatformUseCase,
    private val loadStoredOAuthPlatformUseCase: LoadStoredOAuthPlatformUseCase
) : ViewModel(), OnBoardingContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: OnBoardingContract.UiEvent) = when (event) {
        is OnStarted -> onStarted()

        is OnClickKakaoLogin -> onClickKakaoLogin()

        is OnClickGoogleLogin -> onClickGoogleLogin()

        is OnClickSkip -> onClickSkip()

        is OnSuccessSocialLogin -> onSuccessSocialLogin(
            platform = event.platform,
            data = event.data
        )

        is OnFailureSocialLogin -> onFailureSocialLogin()
    }

    private val exceptionHandler = getExceptionHandler(
        onUnknownHostException = {}
    )

    private fun onStarted() {
        viewModelScope.launch {
            when (loadStoredOAuthPlatformUseCase().first()) {
                PLATFORM_KAKAO -> _uiEffect.send(LoginWithKakao)

                PLATFORM_GOOGLE -> _uiEffect.send(LoginWithGoogle)

                else -> {
                    _uiState.update {
                        it.copy(autoLoginFailed = true)
                    }
                }
            }
        }
    }

    private fun onClickKakaoLogin() {
        viewModelScope.launch {
            _uiEffect.send(LoginWithKakao)
        }
    }

    private fun onClickGoogleLogin() {
        viewModelScope.launch {
            _uiEffect.send(LoginWithGoogle)
        }
    }

    private fun onClickSkip() {
        viewModelScope.launch {
            _uiEffect.send(GoToMain)
        }
    }

    private fun onSuccessSocialLogin(
        platform: String,
        data: String
    ) {
        viewModelScope.launch(exceptionHandler) {
            when (platform) {
                PLATFORM_KAKAO -> serviceLogin(
                    platform = platform,
                    token = data
                )

                PLATFORM_GOOGLE -> {
                    val googleAccessToken = getGoogleAccessToken(data)
                    serviceLogin(
                        platform = platform,
                        token = googleAccessToken
                    )
                }
            }
        }
    }

    private fun onFailureSocialLogin() {

    }

    private suspend fun getGoogleAccessToken(serverAuthCode: String) = getGoogleAccessTokenUseCase(
        clientId = SocialLoginManager.GOOGLE_CLIENT_ID,
        serverAuthCode = serverAuthCode
    )

    private suspend fun serviceLogin(
        platform: String,
        token: String
    ) {
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

        viewModelScope.launch(exceptionHandler) {
            if (uiState.value.autoLoginFailed) {
                withContext(ioDispatcher) {
                    storeOAuthPlatformUseCase(platform)
                }
            }

            _uiEffect.send(GoToMain)
        }
    }

    private fun onFailureServiceLogin() {

    }
}