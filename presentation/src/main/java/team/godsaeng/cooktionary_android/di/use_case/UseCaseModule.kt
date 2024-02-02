package team.godsaeng.cooktionary_android.di.use_case

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import team.godsaeng.domain.model.repository.UserRepository
import team.godsaeng.domain.model.use_case.LoadStoredOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.StoreOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.VerifyUserUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideVerifyUserUseCase(userRepository: UserRepository): VerifyUserUseCase = VerifyUserUseCase(userRepository)

    @Provides
    @ViewModelScoped
    fun provideStoreOAuthPlatformUseCase(userRepository: UserRepository): StoreOAuthPlatformUseCase = StoreOAuthPlatformUseCase(userRepository)

    @Provides
    @ViewModelScoped
    fun provideLoadStoredOAuthPlatformUseCase(userRepository: UserRepository): LoadStoredOAuthPlatformUseCase = LoadStoredOAuthPlatformUseCase(userRepository)
}