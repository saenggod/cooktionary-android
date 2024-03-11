package team.godsaeng.cooktionary_android.di.use_case.recipe

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import team.godsaeng.domain.model.use_case.recipe.GetLoadedRecipeListUseCase
import team.godsaeng.domain.model.use_case.recipe.GetRecipeListUseCase

@Module
@InstallIn(ViewModelComponent::class)
object RecipeUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetRecipeListUseCase(recipeRepository: RecipeRepository): GetRecipeListUseCase = GetRecipeListUseCase(recipeRepository)

    @Provides
    @ViewModelScoped
    fun provideGetLoadedRecipeListUseCase(recipeRepository: RecipeRepository): GetLoadedRecipeListUseCase = GetLoadedRecipeListUseCase(recipeRepository)
}