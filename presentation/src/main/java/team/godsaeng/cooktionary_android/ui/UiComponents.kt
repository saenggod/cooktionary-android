package team.godsaeng.cooktionary_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.theme.TextColor

@Composable
fun StyledText(
    modifier: Modifier = Modifier,
    stringId: Int? = null,
    text: String? = null,
    style: TextStyle,
    fontSize: Int,
    lineHeight: Float = 1f,
    color: Color = TextColor,
) {
    Text(
        modifier = modifier,
        text = if (stringId != null) stringResource(id = stringId) else text.orEmpty(),
        style = style,
        fontSize = dpToSp(fontSize.dp),
        letterSpacing = 0.sp,
        lineHeight = dpToSp((fontSize * lineHeight).dp),
        color = color
    )
}

@Composable
fun RoundedCornerButton(
    modifier: Modifier = Modifier,
    stringId: Int,
    textStyle: TextStyle,
    fontSize: Int,
    textColor: Color,
    buttonColor: Color,
    borderColor: Color
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = buttonColor)
    ) {
        StyledText(
            stringId = stringId,
            style = textStyle,
            fontSize = fontSize,
            color = textColor
        )
    }
}

@Composable
inline fun TopBar(
    noinline onClickBackButton: (() -> Unit)? = null,
    noinline onClickProfileIcon: (() -> Unit)? = null,
    crossinline middleContents: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(54.dp)
    ) {
        onClickBackButton?.let {
            BackButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { onClickBackButton() }
            )
        }

        Box(modifier = Modifier.align(Center)) {
            middleContents()
        }

        onClickProfileIcon?.let {
            ProfileIcon(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { onClickProfileIcon() }
            )
        }
    }
}

@Composable
inline fun ProfileIcon(
    modifier: Modifier = Modifier,
    crossinline onClick: () -> Unit
) {
    Image(
        modifier = modifier.clickableWithoutRipple { onClick() },
        painter = painterResource(id = R.drawable.ic_user),
        contentDescription = null
    )
}

@Composable
inline fun BackButton(
    modifier: Modifier = Modifier,
    crossinline onClick: () -> Unit
) {
    Icon(
        modifier = modifier.clickableWithoutRipple { onClick() },
        painter = painterResource(R.drawable.ic_arrow_back),
        contentDescription = null
    )
}