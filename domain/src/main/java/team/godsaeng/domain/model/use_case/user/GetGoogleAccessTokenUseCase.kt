package team.godsaeng.domain.model.use_case.user

import dagger.Reusable
import team.godsaeng.domain.model.repository.user.UserRepository
import javax.inject.Inject

@Reusable
class GetGoogleAccessTokenUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(
        clientId: String,
        serverAuthCode: String
    ): String = repository.fetchGoogleAccessToken(
        clientId = clientId,
        serverAuthCode
    )
}