package team.godsaeng.data.mapper

import team.godsaeng.data.base.BaseResponse
import team.godsaeng.domain.model.base.BaseModel

interface Mapper<in R : BaseResponse, out D : BaseModel> {
    fun R.toDomainModel(): D
}