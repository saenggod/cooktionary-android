package team.godsaeng.cooktionary_android.ui.on_boarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.GoToMain
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithGoogle
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakao
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickGoogleLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickKakaoLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickSkip
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnFailureLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnSuccessLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiState
import team.godsaeng.domain.model.use_case.VerifyUserUseCase
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val verifyUserUseCase: VerifyUserUseCase
) : ViewModel(), OnBoardingContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: OnBoardingContract.UiEvent) = when (event) {
        is OnClickKakaoLogin -> onClickKakaoLogin()

        is OnClickGoogleLogin -> onClickGoogleLogin()

        is OnClickSkip -> onClickSkip()

        is OnSuccessLogin -> onSuccessLogin(
            platform = event.platform,
            token = event.token
        )

        is OnFailureLogin -> onFailureLogin()
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

    private fun onSuccessLogin(
        platform: String,
        token: String
    ) {
        viewModelScope.launch {
            verifyUserUseCase(
                platform = platform,
                token = token
            )
        }
    }

    private fun onFailureLogin() {

    }
}