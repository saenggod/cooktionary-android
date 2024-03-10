package team.godsaeng.domain.model.model.recipe

import team.godsaeng.domain.model.base.BaseModel

data class Recipe(
    val id: Int,
    val title: String,
    val ingredientNameList: List<String>,
    val time: String,
    val score: String,
    val content: String,
    val imageUrl: String,
    val neededIngredientCount: Int
) : BaseModel
