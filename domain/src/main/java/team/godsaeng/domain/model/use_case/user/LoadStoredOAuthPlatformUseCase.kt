package team.godsaeng.domain.model.use_case.user

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import team.godsaeng.domain.model.repository.user.UserRepository
import javax.inject.Inject

@Reusable
class LoadStoredOAuthPlatformUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): Flow<String?> = repository.loadStoredOAuthPlatform()
}