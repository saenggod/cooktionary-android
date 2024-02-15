package team.godsaeng.cooktionary_android.di.use_case.ingredient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import team.godsaeng.domain.model.repository.ingredient.IngredientRepository
import team.godsaeng.domain.model.use_case.ingredient.GetIngredientUseCase
import team.godsaeng.domain.model.use_case.ingredient.GetMyIngredientListUseCase

@Module
@InstallIn(ViewModelComponent::class)
object IngredientUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetIngredientUseCase(ingredientRepository: IngredientRepository): GetIngredientUseCase = GetIngredientUseCase(ingredientRepository)

    @Provides
    @ViewModelScoped
    fun provideGetMyIngredientListUseCase(ingredientRepository: IngredientRepository): GetMyIngredientListUseCase = GetMyIngredientListUseCase(ingredientRepository)
}