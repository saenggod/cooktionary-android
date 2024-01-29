package team.godsaeng.data.repository_impl

import team.godsaeng.data.model.request.VerificationRequest
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val cooktionaryApi: CooktionaryApi) : UserRepository {
    override suspend fun sendVerification(
        platform: String,
        token: String
    ) {
        cooktionaryApi.postVerification(
            VerificationRequest(
                platform = platform,
                token = token
            )
        )
    }
}