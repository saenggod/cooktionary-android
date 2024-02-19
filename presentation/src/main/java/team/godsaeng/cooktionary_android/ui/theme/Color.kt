package team.godsaeng.cooktionary_android.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PointColor = Color(0xFFFF5B29)
val SubColor = Color(0xFF27262E)
val ButtonBorderColor = Color(0xFF000000).copy(alpha = 0.03f)
val Grey0 = Color(0xFFF3F5F9)
val Grey1 = Color(0xFFDFE2E7)
val ProgressBackgroundColor = Color(0xFFF3F5F9)
val DraggingGrey = Color(0xFFAAAAAA)
val TextColor = Color(0xFF27262E)
val TextColorGrey1 = Color(0xFF434343)
val TextColorGrey2 = Color(0xFFAAAAAA)
val TextColorGrey3 = Color(0xFF949494)
val TextColorGrey4 = Color(0xFF767676)
val TextColorGrey5 = Color(0xFFA8B0B9)
val TextColorGrey6 = Color(0xFFA9A9A9)
val AddedIngredientDescColor = Color(0xFFABABAB)
val RecipeDetailTextColor = Color(0xFF6B6B6B)
val ImagePlaceHolderColor = Color(0xFFEEEEEE)
val SectionBackgroundColor = Color(0xFFF7F9FD)
val ArrowTint = Color(0xFF888C96)
val ArrowBackgroundColor = Color(0xFFEDF2F9)
val DividerColor = Color(0xFFF0F0F0)
val CheckBoxDisabledColor = Color(0xFFAEAEB2)
val TrashCanDisabledColor = Color(0xFFBEC3CC)

@Composable
fun textFieldSelectionColors() = TextSelectionColors(
    handleColor = PointColor,
    backgroundColor = TextColor
)