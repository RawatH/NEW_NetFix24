package com.netfix.models.network.request.feedback

import java.io.Serializable

data class FeedbackRequestModel(
    var iD_Feedback: Int,
    var iD_User: Int,
    var starRating: Float,
    var userView: String,
    var isActive: Boolean,
    var createdBy: Int,
    var userName:String
): Serializable
