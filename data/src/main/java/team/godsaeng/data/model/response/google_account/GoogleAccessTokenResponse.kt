package team.godsaeng.data.model.response.google_account

import com.google.gson.annotations.SerializedName

data class GoogleAccessTokenResponse(
    @SerializedName("access_token") val accessToken: String
)
