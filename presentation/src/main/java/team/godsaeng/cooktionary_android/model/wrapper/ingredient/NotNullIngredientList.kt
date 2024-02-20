package team.godsaeng.cooktionary_android.model.wrapper.ingredient

import androidx.compose.runtime.Immutable
import team.godsaeng.domain.model.model.ingredient.Ingredient

@Immutable
data class NotNullIngredientList(val values: List<Ingredient>)
