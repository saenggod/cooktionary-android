package team.godsaeng.cooktionary_android.ui.my_page

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
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToMyInfo
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToPrivacyTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToSavedRecipeList
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToServiceTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickAccount
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickPrivacyTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickSavedRecipeList
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickServiceTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiState
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor() : ViewModel(), MyPageContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: MyPageContract.UiEvent) = when (event) {
        is OnClickAccount -> onClickAccount()

        is OnClickSavedRecipeList -> onClickSavedRecipeList()

        is OnClickServiceTerms -> onClickServiceTerms()

        is OnClickPrivacyTerms -> onClickPrivacyTerms()
    }

    private fun onClickAccount() {
        viewModelScope.launch {
            _uiEffect.send(GoToMyInfo)
        }
    }

    private fun onClickSavedRecipeList() {
        viewModelScope.launch {
            _uiEffect.send(GoToSavedRecipeList)
        }
    }

    private fun onClickServiceTerms() {
        viewModelScope.launch {
            _uiEffect.send(GoToServiceTerms)
        }
    }

    private fun onClickPrivacyTerms() {
        viewModelScope.launch {
            _uiEffect.send(GoToPrivacyTerms)
        }
    }
}