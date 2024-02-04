package team.godsaeng.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import team.godsaeng.data.model.CTResponse
import team.godsaeng.data.model.request.google_account.GoogleAccessTokenRequest
import team.godsaeng.data.model.request.verification.VerificationRequest
import team.godsaeng.data.model.response.google_account.GoogleAccessTokenResponse
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
}