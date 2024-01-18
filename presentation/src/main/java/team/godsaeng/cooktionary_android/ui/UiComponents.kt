package team.godsaeng.cooktionary_android.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.godsaeng.cooktionary_android.ui.theme.TextColor

@Composable
fun StyledText(
    modifier: Modifier = Modifier,
    stringId: Int,
    style: TextStyle,
    fontSize: Int,
    lineHeight: Float = 1f,
    color: Color = TextColor,
) {
    Text(
        modifier = modifier,
        text = stringResource(id = stringId),
        style = style,
        fontSize = dpToSp(fontSize.dp),
        letterSpacing = 0.sp,
        lineHeight = dpToSp((fontSize * lineHeight).dp),
        color = color
    )
}