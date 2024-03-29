package team.godsaeng.data.model.request.verification

import com.google.gson.annotations.SerializedName

data class VerificationRequest(
    @SerializedName("type") val platform: String,
    @SerializedName("value") val token: String
)
