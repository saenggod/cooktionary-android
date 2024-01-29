package team.godsaeng.domain.model.model.verification

import team.godsaeng.domain.model.base.BaseModel

data class Verification(
    val name: String,
    val email: String,
    val accessToken: String
) : BaseModel
