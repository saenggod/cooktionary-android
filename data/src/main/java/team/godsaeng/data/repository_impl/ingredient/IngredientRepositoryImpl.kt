package team.godsaeng.data.repository_impl.ingredient

import team.godsaeng.data.model.response.ingredient.IngredientResponse.Companion.toDomainModel
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.model.CTException
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.ingredient.Ingredient
import team.godsaeng.domain.model.repository.ingredient.IngredientRepository
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(private val cooktionaryApi: CooktionaryApi) : IngredientRepository {
    override suspend fun fetchIngredient(name: String): ResponseState<Ingredient> {
        return try {
            val response = cooktionaryApi.getIngredient(name)
            ResponseState.OnSuccess(response.data.toDomainModel())
        } catch (exception: CTException) {
            ResponseState.OnFailure(exception.ctError)
        }
    }

    override suspend fun fetchMyIngredientList(): ResponseState<List<Ingredient>> {
        return try {
            val response = cooktionaryApi.getMyIngredientList()
            ResponseState.OnSuccess(response.data.map { it.toDomainModel() })
        } catch (exception: CTException) {
            ResponseState.OnFailure(exception.ctError)
        }
    }
}