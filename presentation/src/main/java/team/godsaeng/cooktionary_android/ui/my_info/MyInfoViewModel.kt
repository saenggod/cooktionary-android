package team.godsaeng.cooktionary_android.ui.my_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEffect
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEffect.GoToSecession
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEvent.OnClickLogout
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEvent.OnClickSecession
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiState
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor() : ViewModel(), MyInfoContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: MyInfoContract.UiEvent) = when (event) {
        is OnClickLogout -> onClickLogout()

        is OnClickSecession -> onClickSecession()
    }

    private fun onClickLogout() {

    }

    private fun onClickSecession() {
        viewModelScope.launch {
            _uiEffect.send(GoToSecession)
        }
    }
}