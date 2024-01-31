package team.godsaeng.domain.model.repository

import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.verification.Verification

interface UserRepository {
    suspend fun sendVerification(
        platform: String,
        token: String
    ): ResponseState<Verification>
}