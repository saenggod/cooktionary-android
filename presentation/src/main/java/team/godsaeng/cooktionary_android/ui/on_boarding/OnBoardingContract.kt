package team.godsaeng.cooktionary_android.ui.on_boarding

import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface OnBoardingContract : BaseContract<OnBoardingContract.UiState, OnBoardingContract.UiEvent, OnBoardingContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false
    )

    sealed interface UiEvent {
        data object OnClickKakaoLogin : UiEvent

        data object OnClickGoogleLogin : UiEvent

        data object OnClickSkip : UiEvent
    }

    sealed interface UiEffect {

    }
}