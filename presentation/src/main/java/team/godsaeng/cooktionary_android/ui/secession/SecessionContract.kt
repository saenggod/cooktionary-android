package team.godsaeng.cooktionary_android.ui.secession

import androidx.compose.runtime.Immutable
import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface SecessionContract : BaseContract<SecessionContract.UiState, SecessionContract.UiEvent, SecessionContract.UiEffect> {
    @Immutable
    data class UiState(
        val isAgreed: Boolean = false
    )

    sealed interface UiEvent {
        data object OnClickAgree : UiEvent

        data object OnClickSecession : UiEvent
    }

    sealed interface UiEffect {

    }
}