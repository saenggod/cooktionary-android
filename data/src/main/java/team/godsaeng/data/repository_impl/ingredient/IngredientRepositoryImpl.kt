package team.godsaeng.data.repository_impl.ingredient

import team.godsaeng.data.model.response.ingredient.IngredientResponse.Companion.toDomainModel
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.model.CTException
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.ingredient.Ingredient
import team.godsaeng.domain.model.repository.ingredient.IngredientRepository
import java.io.IOException
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(private val cooktionaryApi: CooktionaryApi) : IngredientRepository {
    override suspend fun fetchIngredient(name: String): ResponseState<Ingredient> {
        return try {
            val response = cooktionaryApi.getIngredient(name)
            IOException("asd")
            ResponseState.OnSuccess(response.data.toDomainModel())
        } catch (exception: CTException) {
            ResponseState.OnFailure(exception.ctError)
        }
    }
}