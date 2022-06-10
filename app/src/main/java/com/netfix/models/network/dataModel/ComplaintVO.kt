package com.netfix.models.network.dataModel

import java.io.Serializable

data class ComplaintVO(
    var ticketTimeStemp:Long?,
    var ticketStatus: String?,
    var complaintOn: String?,
    var ticketID: String?,
    var isActive: Boolean,
    var subject: String,
    var chatHistory: MutableList<ChatHistory>,
    var customerName:String,
    var loginID:String,
    var areaName:String,
    var assignToName : String,
    var customerNumber:String
) : Serializable {
    data class ChatHistory(
        var iD_Complaint: Int?,
        var iD_USer: Int,
        var description: String,
        var complaintStatus: Int,
        var ticketStatus: Int?,
        var complaintOn: String?,
        var messangerName: String?
    ) : Serializable
}