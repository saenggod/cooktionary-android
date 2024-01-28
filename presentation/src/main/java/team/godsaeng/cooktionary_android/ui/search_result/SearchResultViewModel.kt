package team.godsaeng.cooktionary_android.ui.search_result

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEffect
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiState
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor() : ViewModel(), SearchResultContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: SearchResultContract.UiEvent) = when (event) {
        else -> Unit
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {

        }
    }


}