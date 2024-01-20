package team.godsaeng.domain.model.use_case

import dagger.Reusable
import team.godsaeng.domain.model.repository.UserRepository
import javax.inject.Inject

@Reusable
class VerifyUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(
        platform: String,
        token: String
    ) {
        repository.sendVerification(
            platform = platform,
            token = token
        )
    }
}