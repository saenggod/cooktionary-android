package team.godsaeng.domain.model.use_case.recipe

import dagger.Reusable
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.recipe.Recipe
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import javax.inject.Inject

@Reusable
class GetRecipeListUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(ingredientNameList: List<String>): ResponseState<List<Recipe>> = repository.fetchRecipeList(ingredientNameList)
}