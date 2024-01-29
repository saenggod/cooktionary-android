package team.godsaeng.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import team.godsaeng.data.model.request.VerificationRequest
import team.godsaeng.data.model.CTResponse
import team.godsaeng.data.model.response.verification.VerificationResponse

interface CooktionaryApi {
    @POST("user/verification")
    suspend fun postVerification(
        @Body verificationRequest: VerificationRequest
    ): CTResponse<VerificationResponse>
}