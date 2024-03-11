package team.godsaeng.data.model.response.recipe

import team.godsaeng.data.base.BaseResponse
import team.godsaeng.data.model.Mapper
import team.godsaeng.domain.model.model.recipe.Recipe

data class RecipeResponse(
    val id: Int,
    val title: String,
    val ingredients: List<String>,
    val time: String,
    val score: String,
    val content: String,
    val imageUrl: String
) : BaseResponse {
    companion object : Mapper<RecipeResponse, Recipe> {
        override fun RecipeResponse.toDomainModel(): Recipe {
            return Recipe(
                id = id,
                title = title,
                ingredientNameList = ingredients,
                time = time,
                difficulty = score,
                content = content,
                imageUrl = imageUrl,
                neededIngredientCount = 0
            )
        }
    }
}