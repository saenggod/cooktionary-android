package team.godsaeng.domain.model.model.recipe

import team.godsaeng.domain.model.base.BaseModel

data class Recipe(
    val id: Int,
    val title: String,
    val ingredientNameList: List<String>,
    val time: String,
    val difficulty: String,
    val content: String,
    val imageUrl: String,
    val neededIngredientCount: Int,
    val isSaved: Boolean
) : BaseModel {
    fun difficultyToLevel(): Int = when (difficulty) {
        DIFFICULTY_HIGH -> 3
        DIFFICULTY_MIDDLE -> 2
        DIFFICULTY_LOW -> 1
        else -> 0
    }

    companion object {
        private const val DIFFICULTY_HIGH = "상"
        private const val DIFFICULTY_MIDDLE = "중"
        private const val DIFFICULTY_LOW = "하"
    }
}
