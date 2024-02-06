package team.godsaeng.data.model.response.ingredient

import team.godsaeng.data.base.BaseResponse
import team.godsaeng.data.model.Mapper
import team.godsaeng.domain.model.model.ingredient.Ingredient

data class IngredientResponse(
    val name: String,
    val imageUrl: String
) : BaseResponse {
    companion object : Mapper<IngredientResponse, Ingredient> {
        override fun IngredientResponse.toDomainModel(): Ingredient {
            return Ingredient(
                name = name,
                imageUrl = imageUrl
            )
        }
    }
}
