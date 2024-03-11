package team.godsaeng.cooktionary_android.ui.secession

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import team.godsaeng.cooktionary_android.ui.secession.SecessionContract.UiEffect
import team.godsaeng.cooktionary_android.ui.secession.SecessionContract.UiEvent.OnClickAgree
import team.godsaeng.cooktionary_android.ui.secession.SecessionContract.UiEvent.OnClickSecession
import team.godsaeng.cooktionary_android.ui.secession.SecessionContract.UiState
import javax.inject.Inject

@HiltViewModel
class SecessionViewModel @Inject constructor() : ViewModel(), SecessionContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: SecessionContract.UiEvent) = when (event) {
        is OnClickAgree -> onClickAgree()

        is OnClickSecession -> onClickSecession()
    }

    private fun onClickAgree() {
        _uiState.update {
            it.copy(isAgreed = !it.isAgreed)
        }
    }

    private fun onClickSecession() {

    }
}