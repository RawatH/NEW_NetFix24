package com.netfix.models.network.request.alert

data class AlertVO(
    var iD_MessageAlert: Int = 0,
    var messageSubject: String,
    var messageBody: String,
    var startDateTime: String,
    var endDateTime: String,
    var forUser: Boolean = false,
    var AppliedIDList: String,
    var ipAddress: String = "::1",
    var createdBy: Int = 1,
    var isActive: Boolean = true
){

    fun isValid():Boolean{

        if(messageBody.isBlank() ||messageSubject.isBlank() || startDateTime.isBlank()|| endDateTime.isBlank() ){
            return false
        }

        return true
    }
}
