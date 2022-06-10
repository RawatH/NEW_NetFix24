package com.netfix.ui.feedback

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackRepository : BaseRepository() {


    fun getFeedback(feedbackLiveData: MutableLiveData<ResponseModel<List<FeedbackRequestModel>>>) {
        val call: Call<ResponseModel<List<FeedbackRequestModel>>> =
            NFServiceClient.getClient().getFeedbacks()

        call.enqueue(object : Callback<ResponseModel<List<FeedbackRequestModel>>> {

            override fun onResponse(
                call: Call<ResponseModel<List<FeedbackRequestModel>>>,
                response: Response<ResponseModel<List<FeedbackRequestModel>>>
            ) {

                feedbackLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseModel<List<FeedbackRequestModel>>>,
                t: Throwable
            ) {

            }

        })
    }
}