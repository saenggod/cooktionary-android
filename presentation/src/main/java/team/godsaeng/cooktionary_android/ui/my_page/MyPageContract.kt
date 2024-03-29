package team.godsaeng.cooktionary_android.ui.my_page

import androidx.compose.runtime.Immutable
import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface MyPageContract : BaseContract<MyPageContract.UiState, MyPageContract.UiEvent, MyPageContract.UiEffect> {
    @Immutable
    data class UiState(
        val isLoading: Boolean = false
    )

    sealed interface UiEvent {
        data object OnClickAccount : UiEvent

        data object OnClickSavedRecipeList : UiEvent

        data object OnClickServiceTerms : UiEvent

        data object OnClickPrivacyTerms : UiEvent
    }

    sealed interface UiEffect {
        data object GoToMyInfo : UiEffect

        data object GoToSavedRecipeList : UiEffect

        data object GoToServiceTerms : UiEffect

        data object GoToPrivacyTerms : UiEffect
    }
}