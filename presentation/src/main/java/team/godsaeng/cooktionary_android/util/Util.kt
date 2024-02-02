package team.godsaeng.cooktionary_android.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.UnknownHostException

inline fun ViewModel.getExceptionHandler(
    crossinline onUnknownHostException: suspend () -> Unit,
    noinline onElse: (suspend (Throwable) -> Unit)? = null
): CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch {
        when (throwable) {
            is UnknownHostException -> {
                onUnknownHostException()
            }

            else -> {
                onElse?.let { it(throwable) }
            }
        }
    }
}