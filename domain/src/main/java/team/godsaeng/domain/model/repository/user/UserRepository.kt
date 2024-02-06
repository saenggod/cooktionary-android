package team.godsaeng.domain.model.repository.user

import kotlinx.coroutines.flow.Flow
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.verification.Verification

interface UserRepository {
    suspend fun fetchGoogleAccessToken(
        clientId: String,
        serverAuthCode: String
    ): String

    suspend fun sendVerification(
        platform: String,
        token: String
    ): ResponseState<Verification>

    suspend fun storeOAuthPlatform(data: String)

    fun loadStoredOAuthPlatform(): Flow<String?>
}