package team.godsaeng.cooktionary_android.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
inline fun <reified STATE, EVENT, EFFECT> use(
    viewModel: BaseContract<STATE, EVENT, EFFECT>
): StateDispatchEffect<STATE, EVENT, EFFECT> {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.uiEvent(event)
    }

    return StateDispatchEffect(
        state = uiState,
        effectFlow = viewModel.uiEffect,
        dispatch = dispatch,
    )
}

data class StateDispatchEffect<STATE, EVENT, EFFECT>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
    val effectFlow: Flow<EFFECT>,
)