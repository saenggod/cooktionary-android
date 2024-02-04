package team.godsaeng.data.model.request.google_account

import com.google.gson.annotations.SerializedName

data class GoogleAccessTokenRequest(
    @SerializedName("grant_type") val grantType: String = "authorization_code",
    @SerializedName("client_id") val clientId: String,
    @SerializedName("client_secret") val clientSecret: String,
    @SerializedName("redirect_url") val redirectUrl: String = "",
    @SerializedName("code") val serverAuthCode: String
)
