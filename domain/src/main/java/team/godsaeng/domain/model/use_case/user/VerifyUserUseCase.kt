package team.godsaeng.domain.model.use_case.user

import dagger.Reusable
import team.godsaeng.domain.model.model.ResponseState
import team.godsaeng.domain.model.model.verification.Verification
import team.godsaeng.domain.model.repository.UserRepository
import javax.inject.Inject

@Reusable
class VerifyUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(
        platform: String,
        token: String
    ): ResponseState<Verification> = repository.sendVerification(
        platform = platform,
        token = token
    )
}