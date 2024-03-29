package team.godsaeng.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import team.godsaeng.data.model.CTResponse
import team.godsaeng.data.model.request.google_account.GoogleAccessTokenRequest
import team.godsaeng.data.model.request.verification.VerificationRequest
import team.godsaeng.data.model.response.google_account.GoogleAccessTokenResponse
import team.godsaeng.data.model.response.ingredient.IngredientResponse
import team.godsaeng.data.model.response.recipe.RecipeResponse
import team.godsaeng.data.model.response.verification.VerificationResponse

interface CooktionaryApi {
    @POST
    suspend fun postGoogleAccountInfo(
        @Url url: String,
        @Body googleAccessTokenRequest: GoogleAccessTokenRequest
    ): GoogleAccessTokenResponse

    @POST("user/verification")
    suspend fun postVerification(
        @Body verificationRequest: VerificationRequest
    ): CTResponse<VerificationResponse>

    @GET("ingredient/{name}")
    suspend fun getIngredient(
        @Path("name") name: String
    ): CTResponse<IngredientResponse>

    @GET("user/ingredient")
    suspend fun getMyIngredientList(): CTResponse<List<IngredientResponse>>

    @GET("recipe")
    suspend fun getRecipeList(
        @Query("q") ingredientNames: String
    ): CTResponse<List<RecipeResponse>>

    @POST("user/recipe/save/{recipeId}")
    suspend fun postRecipeSaving(
        @Path("recipeId") recipeId: Int
    )

    @DELETE("user/recipe/delete/{recipeId}")
    suspend fun deleteSavedRecipe(
        @Path("recipeId") recipeId: Int
    )

    @GET("user/recipe")
    suspend fun getSavedRecipeList(): CTResponse<List<RecipeResponse>>
}