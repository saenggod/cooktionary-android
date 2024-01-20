package team.godsaeng.data.model.request

import com.google.gson.annotations.SerializedName

data class VerificationRequest(
    @SerializedName("type") val platform: String,
    @SerializedName("value") val token: String
)
