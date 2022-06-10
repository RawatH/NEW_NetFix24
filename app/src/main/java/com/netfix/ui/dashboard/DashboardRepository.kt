package com.netfix.ui.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.models.network.response.banners.BannerResponseModel
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import com.netfix.ui.complaint.ComplaintDetailRepository
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository : BaseRepository() {
    private  val TAG = "BannerRepository"

    fun getBanners(allBannerLiveData: MutableLiveData<BannerResponseModel>){
        val call: Call<BannerResponseModel> = NFServiceClient.getClient().getBanners()

        call.enqueue(object : Callback<BannerResponseModel> {
            override fun onResponse(call: Call<BannerResponseModel>, response: Response<BannerResponseModel>) {

                Log.d(TAG, "onResponse: ")
                allBannerLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<BannerResponseModel>, t: Throwable) {

                Log.d(TAG, "onFailure: "+t.message)
            }

        })
    }

    fun sendFeedbackReqmodel(model: FeedbackRequestModel,feedbackLiveData : MutableLiveData<ResponseModel<FeedbackRequestModel>>) {
        val call: Call<ResponseModel<FeedbackRequestModel>> = NFServiceClient.getClient().submitFeedback(model)

        call.enqueue(object : Callback<ResponseModel<FeedbackRequestModel>> {

            override fun onResponse(
                call: Call<ResponseModel<FeedbackRequestModel>>,
                response: Response<ResponseModel<FeedbackRequestModel>>
            ) {
                Log.d(TAG, "onResponse: ")
                feedbackLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseModel<FeedbackRequestModel>>, t: Throwable) {
                Log.d(TAG, "onFailure: "+t.message)
            }

        })
    }

    fun getUserAlert(userId: Int, alertLiveData: MutableLiveData<ResponseModel<List<AlertMsgVO>>>) {
        val call: Call<ResponseModel<List<AlertMsgVO>>> = NFServiceClient.getClient().getUserAlert(userId)

        call.enqueue(object : Callback<ResponseModel<List<AlertMsgVO>>> {

            override fun onResponse(
                call: Call<ResponseModel<List<AlertMsgVO>>>,
                response: Response<ResponseModel<List<AlertMsgVO>>>
            ) {
                Log.d(TAG, "onResponse: ")
                alertLiveData.value = response.body()
            }

            override fun onFailure(call: Call<ResponseModel<List<AlertMsgVO>>>, t: Throwable) {
                Log.d(TAG, "onFailure: "+t.message)
            }

        })

    }

    class Data(var iD_User:Int)
    class ComplaintReq(var chatHistory:List<Data>)
    fun getComplaints(userId: Int, ticketLiveData: MutableLiveData<ComplaintResponseModel>) {


        var data = Data(userId)
        var chatHistory = mutableListOf<Data>()
        chatHistory.add(data)


        var comReq = ComplaintReq(chatHistory)


        val call: Call<ComplaintResponseModel> = NFServiceClient.getClient().getComplaintForUser(
            comReq)
        call.enqueue(object : Callback<ComplaintResponseModel> {
            override fun onResponse(call: Call<ComplaintResponseModel>, response: Response<ComplaintResponseModel>) {
                Log.d(TAG, "onResponse: ")
                ticketLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ComplaintResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                ticketLiveData.postValue(null)
            }

        })
    }
}