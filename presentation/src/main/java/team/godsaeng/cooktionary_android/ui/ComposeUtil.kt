package team.godsaeng.cooktionary_android.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun dpToSp(dp: Dp): TextUnit = with(LocalDensity.current) { dp.toSp() }

@Composable
fun getContext(): Context = LocalContext.current