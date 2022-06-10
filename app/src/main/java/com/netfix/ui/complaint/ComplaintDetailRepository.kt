package com.netfix.ui.complaint

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.complaint.NewComplaintResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintDetailRepository :BaseRepository() {

    private val TAG = "ComplaintDetailRepository"

    class Ticket(var ticketID:String)

    fun getTicketDetail(ticketId: String, ticketLiveData: MutableLiveData<ComplaintResponseModel>) {


        val call: Call<ComplaintResponseModel> = NFServiceClient.getClient().getTicketDetail(Ticket(ticketId))
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


    fun sendComplaintMsg(complaintVO: ComplaintVO , responseLiveData: MutableLiveData<NewComplaintResponseModel>){
        val call: Call<NewComplaintResponseModel> = NFServiceClient.getClient().raiseComplaint(complaintVO)

        call.enqueue(object : Callback<NewComplaintResponseModel> {
            override fun onResponse(call: Call<NewComplaintResponseModel>, response: Response<NewComplaintResponseModel>) {
                Log.d(TAG, "onResponse: ")
                responseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<NewComplaintResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                responseLiveData.postValue(null)
            }

        })
    }

}