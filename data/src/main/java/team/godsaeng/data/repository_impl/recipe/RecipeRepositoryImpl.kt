package team.godsaeng.data.repository_impl.recipe

import team.godsaeng.data.model.response.recipe.RecipeResponse.Companion.toDomainModel
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.model.CTException
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.recipe.Recipe
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(private val cooktionaryApi: CooktionaryApi) : RecipeRepository {
    override suspend fun fetchRecipeList(ingredientNameList: List<String>): ResponseState<List<Recipe>> {
        return try {
            val response = cooktionaryApi.getRecipeList(ingredientNameList.joinToString(","))
            ResponseState.OnSuccess(response.data.map { it.toDomainModel() })
        } catch (exception: CTException) {
            ResponseState.OnFailure(exception.ctError)
        }
    }
}