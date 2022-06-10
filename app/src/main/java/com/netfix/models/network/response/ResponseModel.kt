package com.netfix.models.network.response

data class ResponseModel<T>(
    var status: Boolean,
    var code: Int,
    var message: String,
    var responseValue: String,
    var response :T
)