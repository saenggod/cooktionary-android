package team.godsaeng.cooktionary_android.ui.on_boarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakaoAccount
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakaoTalk
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickGoogleLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickKakaoLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickSkip
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnLoginError
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnSuccessKakaoLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiState
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(application: Application) : AndroidViewModel(application), OnBoardingContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: OnBoardingContract.UiEvent) = when (event) {
        is OnClickKakaoLogin -> onClickKakaoLogin()

        is OnClickGoogleLogin -> onClickGoogleLogin()

        is OnClickSkip -> onClickSkip()

        is OnSuccessKakaoLogin -> onSuccessKakaoLogin(event.accessToken)

        is OnLoginError -> onLoginError()
    }

    private fun onClickKakaoLogin() {
        viewModelScope.launch {
            _uiEffect.emit(
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(getApplication())) {
                    LoginWithKakaoTalk
                } else {
                    LoginWithKakaoAccount
                }
            )
        }
    }

    private fun onClickGoogleLogin() {

    }

    private fun onClickSkip() {

    }

    private fun onSuccessKakaoLogin(accessToken: String) {

    }

    private fun onLoginError() {

    }
}