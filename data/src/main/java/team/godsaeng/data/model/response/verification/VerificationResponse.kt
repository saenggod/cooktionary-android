package team.godsaeng.data.model.response.verification

import com.google.gson.annotations.SerializedName
import team.godsaeng.data.base.BaseResponse
import team.godsaeng.data.model.Mapper
import team.godsaeng.domain.model.model.verification.Verification

data class VerificationResponse(
    val name: String?,
    val email: String?,
    @SerializedName("access_token") val accessToken: String?
) : BaseResponse {
    companion object : Mapper<VerificationResponse, Verification> {
        override fun VerificationResponse.toDomainModel(): Verification {
            return Verification(
                name = name.orEmpty(),
                email = email.orEmpty(),
                accessToken = accessToken.orEmpty()
            )
        }
    }
}
