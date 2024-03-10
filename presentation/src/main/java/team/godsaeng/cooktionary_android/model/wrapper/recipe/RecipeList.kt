package team.godsaeng.cooktionary_android.model.wrapper.recipe

import androidx.compose.runtime.Immutable
import team.godsaeng.domain.model.model.recipe.Recipe

@Immutable
data class RecipeList(val values: List<Recipe>)
