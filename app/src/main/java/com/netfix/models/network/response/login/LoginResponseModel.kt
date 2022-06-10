package com.netfix.models.network.response.login

import com.google.gson.JsonObject
import org.json.JSONObject

data class LoginResponseModel(
    var status:Boolean,
    var code:String,
    var message:String,
    var responseValue:String,
    var response : UserLoginResponseModel)
