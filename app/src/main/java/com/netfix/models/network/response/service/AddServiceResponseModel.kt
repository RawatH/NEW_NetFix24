package com.netfix.models.network.response.service

import org.json.JSONObject

data class AddServiceResponseModel(var status: Boolean,
                                   var code: String,
                                   var message: String,
                                   var responseValue: String,
                                   var transactionID: String,
                                   var response: JSONObject)
