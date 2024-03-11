package team.godsaeng.cooktionary_android.di.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.godsaeng.data.repository_impl.ingredient.IngredientRepositoryImpl
import team.godsaeng.data.repository_impl.recipe.RecipeRepositoryImpl
import team.godsaeng.data.repository_impl.user.UserRepositoryImpl
import team.godsaeng.domain.model.repository.ingredient.IngredientRepository
import team.godsaeng.domain.model.repository.recipe.RecipeRepository
import team.godsaeng.domain.model.repository.user.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindIngredientRepository(ingredientRepositoryImpl: IngredientRepositoryImpl): IngredientRepository

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeRepository
}