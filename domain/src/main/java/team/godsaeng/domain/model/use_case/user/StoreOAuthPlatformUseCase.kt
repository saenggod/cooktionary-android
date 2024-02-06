package team.godsaeng.domain.model.use_case.user

import dagger.Reusable
import team.godsaeng.domain.model.repository.user.UserRepository
import javax.inject.Inject

@Reusable
class StoreOAuthPlatformUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(data: String) = repository.storeOAuthPlatform(data)
}