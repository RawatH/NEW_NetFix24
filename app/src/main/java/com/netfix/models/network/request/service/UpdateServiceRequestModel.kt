package com.netfix.models.network.request.service

import com.netfix.models.BaseModel


data class UpdateServiceRequestModel(
    var iD_Services :Int,
    var serviceCode: String,
    var serviceName: String,
    var serviceDescription: String,
    var isActive: Boolean,
    var createdBy:Int
) : BaseModel()