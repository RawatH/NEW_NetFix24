package com.netfix.ui.feedback

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.service.ServiceAllResponseModel
import com.netfix.ui.base.BaseViewModel

class FeedbackViewModel(application: NFApplication) : BaseViewModel(application) {
    var repository = FeedbackRepository()
    var feedbackList: MutableLiveData<ResponseModel<List<FeedbackRequestModel>>> =
        MutableLiveData<ResponseModel<List<FeedbackRequestModel>>>()

    fun getFeedbackList() {
        repository.getFeedback(feedbackList)
    }



}