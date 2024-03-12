package team.godsaeng.cooktionary_android.di.use_case.recipe

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import team.godsaeng.domain.model.use_case.recipe.DeleteSavedRecipeUseCase
import team.godsaeng.domain.model.use_case.recipe.GetRecipeListUseCase
import team.godsaeng.domain.model.use_case.recipe.SaveRecipeUseCase

@Module
@InstallIn(ViewModelComponent::class)
object RecipeUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetRecipeListUseCase(recipeRepository: RecipeRepository): GetRecipeListUseCase = GetRecipeListUseCase(recipeRepository)

    @Provides
    @ViewModelScoped
    fun provideSaveRecipeUseCase(recipeRepository: RecipeRepository): SaveRecipeUseCase = SaveRecipeUseCase(recipeRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteRecipeUseCase(recipeRepository: RecipeRepository): DeleteSavedRecipeUseCase = DeleteSavedRecipeUseCase(recipeRepository)
}