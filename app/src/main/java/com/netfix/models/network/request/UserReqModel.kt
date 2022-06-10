package com.netfix.models.network.request


data class UserReqModel(
    var iD_User: Int,
    var loginID: String,
    var password: String,
    var userName: String,
    var iD_UserType: Int,
    var iD_AddressArea: Int,
    var createdBy: Int,
    var isActive: Boolean

)