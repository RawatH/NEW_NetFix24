package com.netfix.models.network.request.service

import com.netfix.models.BaseModel

data class ServiceRequestModel(
    var id_services: Int = 0,
    var serviceCode: String? = null,
    var serviceName: String? = null
) : BaseModel()