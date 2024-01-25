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
val AddedIngredientDescColor = Color(0xFFABABAB)
val RecipeDetailTextColor = Color(0xFF6B6B6B)
val ImagePlaceHolderColor = Color(0xFFD9D9D9)

@Composable
fun textFieldSelectionColors() = TextSelectionColors(
    handleColor = PointColor,
    backgroundColor = TextColor
)