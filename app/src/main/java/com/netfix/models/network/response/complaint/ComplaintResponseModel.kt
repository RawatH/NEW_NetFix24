package com.netfix.models.network.response.complaint

import com.netfix.models.BaseModel
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.dataModel.PlanVO

data class ComplaintResponseModel(
    var status: Boolean,
    var code: Int,
    var message: String?,
    var responseValue: String,
    var response: List<ComplaintVO>
) : BaseModel()
