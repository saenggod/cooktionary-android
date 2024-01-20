package team.godsaeng.data.remote

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST
import team.godsaeng.data.model.request.VerificationRequest

interface CooktionaryApi {
    @POST("user/verification")
    suspend fun postVerification(
        @Body verificationRequest: VerificationRequest
    ): JsonObject
}