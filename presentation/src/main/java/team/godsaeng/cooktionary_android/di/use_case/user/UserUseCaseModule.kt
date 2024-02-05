package team.godsaeng.cooktionary_android.di.use_case.user

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import team.godsaeng.domain.model.repository.user.UserRepository
import team.godsaeng.domain.model.use_case.user.GetGoogleAccessTokenUseCase
import team.godsaeng.domain.model.use_case.user.LoadStoredOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.user.StoreOAuthPlatformUseCase
import team.godsaeng.domain.model.use_case.user.VerifyUserUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UserUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetGoogleAccountAccessTokenUseCase(userRepository: UserRepository): GetGoogleAccessTokenUseCase = GetGoogleAccessTokenUseCase(userRepository)

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