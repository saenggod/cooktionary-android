package team.godsaeng.domain.model.repository.ingredient

import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.ingredient.Ingredient

interface IngredientRepository {
    suspend fun fetchIngredient(name: String): ResponseState<Ingredient>

    suspend fun fetchMyIngredientList(): ResponseState<List<Ingredient>>
}