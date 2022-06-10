package com.netfix.models.network.response.complaint

import com.netfix.models.BaseModel
import com.netfix.models.network.dataModel.ComplaintVO

data class ComplaintCancelResponseModel (
    var status: Boolean,
    var code: Int,
    var message: String?,
    var response: String,
    ) : BaseModel()