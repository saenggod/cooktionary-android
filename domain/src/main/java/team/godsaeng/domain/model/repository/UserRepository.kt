package team.godsaeng.domain.model.repository

import kotlinx.coroutines.flow.Flow
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.verification.Verification

interface UserRepository {
    suspend fun sendVerification(
        platform: String,
        token: String
    ): ResponseState<Verification>

    suspend fun storeOAuthPlatform(data: String): Boolean

    fun loadStoredOAuthPlatform(): Flow<String?>
}