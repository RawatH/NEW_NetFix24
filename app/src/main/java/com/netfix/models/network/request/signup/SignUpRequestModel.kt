package com.netfix.models.network.request.signup

import com.netfix.util.AppConst

data class SignUpRequestModel(
    var loginID: String,
    var password: String,
    var userName: String,
    var mobileNo: String,
    var emailID: String,
    var iD_UserType: Int,
    var iD_AddressArea:Int,
    var createdBy: Int,
    var ID_PlanDetail:Int
) {


    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        1,
        0
    )

    fun isValid():String{

        if(loginID.isBlank()){
            return "Enter UserID"
        } else if(password.isBlank()){
            return "Enter password"
        }else if(userName.isBlank()){
            return "Enter username"
        }else if(mobileNo.isBlank()){
            return "Enter mobile number"
        }else if(emailID.isBlank()){
            return "Enter email"
        }
        return ""
    }

}