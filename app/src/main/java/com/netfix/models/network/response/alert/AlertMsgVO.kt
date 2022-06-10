package com.netfix.models.network.response.alert

class AlertMsgVO (
    var iD_MessageAlert: Int,
    var iD_UserID: Int,
    var messageSubject: String,
    var messageBody: String,
    var startDateTime: String,
    var endDateTime: String,
)
