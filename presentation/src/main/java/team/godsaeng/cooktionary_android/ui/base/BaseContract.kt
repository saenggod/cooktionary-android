package team.godsaeng.cooktionary_android.ui.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE, EVENT, EFFECT> {
    val uiState: StateFlow<STATE>
    val uiEffect: SharedFlow<EFFECT>
    fun uiEvent(event: EVENT)
}