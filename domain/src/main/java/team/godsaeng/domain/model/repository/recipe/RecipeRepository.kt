package team.godsaeng.domain.model.repository.recipe

import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.recipe.Recipe

interface RecipeRepository {
    suspend fun fetchRecipeList(ingredientNameList: List<String>): ResponseState<List<Recipe>>

    fun getRecipeList(): List<Recipe>
}