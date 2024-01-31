package team.godsaeng.data.model

data class CTResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)