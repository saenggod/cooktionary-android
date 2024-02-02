package team.godsaeng.cooktionary_android.ui.on_boarding

import team.godsaeng.cooktionary_android.ui.base.BaseContract

sealed interface OnBoardingContract : BaseContract<OnBoardingContract.UiState, OnBoardingContract.UiEvent, OnBoardingContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val autoLoginFailed: Boolean = false
    )

    sealed interface UiEvent {
        data object OnStarted : UiEvent

        data object OnClickKakaoLogin : UiEvent

        data object OnClickGoogleLogin : UiEvent

        data object OnClickSkip : UiEvent

        data class OnSuccessSocialLogin(
            val platform: String,
            val token: String
        ) : UiEvent

        data object OnFailureSocialLogin : UiEvent
    }

    sealed interface UiEffect {
        data object LoginWithKakao : UiEffect

        data object LoginWithGoogle : UiEffect

        data object GoToMain : UiEffect
    }
}