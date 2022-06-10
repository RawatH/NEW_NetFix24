package com.netfix.models.network.response.locations

import com.netfix.models.BaseModel
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.response.ResponseModel

class LocationResponseModel (
var status: Boolean,
var code: Int,
var message: String?,
var responseValue: String,
var response: List<LocationVO>
) : BaseModel()