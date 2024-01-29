package team.godsaeng.data.model

data class CTResponse<T>(
    val success: Boolean?,
    val message: String?,
    val data: T?
)