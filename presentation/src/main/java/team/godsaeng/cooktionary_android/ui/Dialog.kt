package team.godsaeng.cooktionary_android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import team.godsaeng.cooktionary_android.ui.theme.PointColor

@Composable
inline fun DefaultDialog(
    properties: DialogProperties,
    noinline onDismissRequest: (() -> Unit)? = null,
    crossinline content: @Composable () -> Unit
) {
    Dialog(
        properties = properties,
        onDismissRequest = { onDismissRequest?.let { it() } }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun LoadingDialog() {
    DefaultDialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = PointColor
        )
    }
}