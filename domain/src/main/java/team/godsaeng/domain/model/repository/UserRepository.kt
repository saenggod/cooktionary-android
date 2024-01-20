package team.godsaeng.domain.model.repository

interface UserRepository {
    suspend fun sendVerification(
        platform: String,
        token: String
    )
}