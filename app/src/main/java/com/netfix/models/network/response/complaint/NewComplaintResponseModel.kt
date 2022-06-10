package com.netfix.models.network.response.complaint

import com.netfix.models.BaseModel
import com.netfix.models.network.dataModel.ComplaintVO

data class NewComplaintResponseModel (
    var status: Boolean,
    var code: Int,
    var message: String?,
    var responseValue: String,
    var transactionID: String,
    var response: ComplaintVO
) : BaseModel()
