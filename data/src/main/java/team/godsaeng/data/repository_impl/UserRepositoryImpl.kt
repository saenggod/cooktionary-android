package team.godsaeng.data.repository_impl

import team.godsaeng.data.model.request.VerificationRequest
import team.godsaeng.data.model.response.verification.VerificationResponse.Companion.toDomainModel
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.model.CTError
import team.godsaeng.domain.model.model.CTException
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.verification.Verification
import team.godsaeng.domain.model.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val cooktionaryApi: CooktionaryApi) : UserRepository {
    override suspend fun sendVerification(
        platform: String,
        token: String
    ): ResponseState<Verification> {
        return try {
            val response = cooktionaryApi.postVerification(
                VerificationRequest(
                    platform = platform,
                    token = token
                )
            )

            ResponseState.OnSuccess(response.data.toDomainModel())
        } catch (exception: CTException) {
            ResponseState.OnFailure(
                CTError(
                    code = exception.ctError.code,
                    message = exception.ctError.message
                )
            )
        }
    }
}