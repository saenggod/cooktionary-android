package team.godsaeng.cooktionary_android.ui.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE, EVENT, EFFECT> {
    val uiState: StateFlow<STATE>
    val uiEffect: Flow<EFFECT>
    fun uiEvent(event: EVENT)
}