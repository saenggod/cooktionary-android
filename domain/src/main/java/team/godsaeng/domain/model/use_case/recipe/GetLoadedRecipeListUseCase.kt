package team.godsaeng.domain.model.use_case.recipe

import dagger.Reusable
import team.godsaeng.domain.model.model.recipe.Recipe
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import javax.inject.Inject

@Reusable
class GetLoadedRecipeListUseCase @Inject constructor(private val repository: RecipeRepository) {
    operator fun invoke(): List<Recipe> = repository.getRecipeList()
}