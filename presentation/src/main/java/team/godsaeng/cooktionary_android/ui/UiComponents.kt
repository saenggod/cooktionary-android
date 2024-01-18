package team.godsaeng.cooktionary_android.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.godsaeng.cooktionary_android.ui.theme.TextColor

@Composable
fun StyledText(
    modifier: Modifier = Modifier,
    text: String,
    style: FontStyle,
    fontSize: Int,
    lineHeight: Float = 1f,
    color: Color = TextColor,
) {
    Text(
        modifier = modifier,
        text = text,
        fontStyle = style,
        fontSize = dpToSp(fontSize.dp),
        letterSpacing = 0.sp,
        lineHeight = dpToSp((fontSize * lineHeight).dp),
        color = color
    )
}