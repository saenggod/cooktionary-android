package team.godsaeng.domain.model.use_case.recipe

import dagger.Reusable
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import javax.inject.Inject

@Reusable
class DeleteSavedRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipeId: Int): ResponseState<Unit> = repository.deleteRecipe(recipeId)
}