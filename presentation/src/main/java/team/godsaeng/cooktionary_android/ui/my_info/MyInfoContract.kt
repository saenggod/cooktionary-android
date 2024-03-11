package team.godsaeng.cooktionary_android.ui.my_info

import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface MyInfoContract : BaseContract<MyInfoContract.UiState, MyInfoContract.UiEvent, MyInfoContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false
    )

    sealed interface UiEvent {
        data object OnClickLogout : UiEvent

        data object OnClickSecession : UiEvent
    }

    sealed interface UiEffect {
        data object GoToOnBoarding : UiEffect

        data object GoToSecession : UiEffect
    }
}