package team.godsaeng.domain.model.model

sealed class ResponseState<out T> {
    data class OnSuccess<out T>(val data: T) : ResponseState<T>()
    data class OnFailure(val error: CTError) : ResponseState<Nothing>()

    inline fun handle(
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (CTError) -> Unit
    ) {
        when (this) {
            is OnSuccess -> {
                onSuccess(data)
            }

            is OnFailure -> {
                onFailure(error)
            }
        }
    }
}