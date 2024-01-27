package team.godsaeng.cooktionary_android.ui.search_result

import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface SearchResultContract : BaseContract<SearchResultContract.UiState, SearchResultContract.UiEvent, SearchResultContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val pullFraction: Float = 0f,
        val isRefreshing: Boolean = false,
    )

    sealed interface UiEvent {

    }

    sealed interface UiEffect {

    }
}