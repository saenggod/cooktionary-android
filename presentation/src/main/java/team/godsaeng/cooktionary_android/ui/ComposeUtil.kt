package team.godsaeng.cooktionary_android.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun dpToSp(dp: Dp): TextUnit = with(LocalDensity.current) { dp.toSp() }

@Composable
fun pxToDp(pixel: Float) = with(LocalDensity.current) { pixel.toDp() }

@Composable
fun getContext(): Context = LocalContext.current

fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null
) {
    onClick()
}

fun Color.alpha(alpha: Int) = this.copy(alpha = alpha / 100f)

@Composable
inline fun branchedModifier(
    condition: Boolean,
    crossinline onDefault: @Composable () -> Modifier,
    noinline onTrue: (@Composable (Modifier) -> Modifier)? = null,
    noinline onFalse: (@Composable (Modifier) -> Modifier)? = null,
): Modifier {
    val defaultModifier: Modifier = onDefault()
    var modifier: Modifier = onDefault()
    modifier = if (condition) {
        onTrue?.let {
            onTrue(modifier)
        } ?: defaultModifier
    } else {
        onFalse?.let {
            onFalse(modifier)
        } ?: defaultModifier
    }

    return modifier
}

@Composable
fun <T> CollectUiEffectWithLifecycle(
    uiEffect: SharedFlow<T>,
    onCollect: (T) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(uiEffect) {
        uiEffect.flowWithLifecycle(lifecycle).collect { uiEffect ->
            onCollect(uiEffect)
        }
    }
}
