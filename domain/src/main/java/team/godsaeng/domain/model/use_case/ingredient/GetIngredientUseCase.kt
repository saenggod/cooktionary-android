package team.godsaeng.domain.model.use_case.ingredient

import dagger.Reusable
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.ingredient.Ingredient
import team.godsaeng.domain.model.repository.ingredient.IngredientRepository
import javax.inject.Inject

@Reusable
class GetIngredientUseCase @Inject constructor(private val repository: IngredientRepository) {
    suspend operator fun invoke(name: String): ResponseState<Ingredient> = repository.fetchIngredient(name)
}