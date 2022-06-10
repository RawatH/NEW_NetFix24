package com.netfix.ui.complaint

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.request.complaint.AssignComplaintModel
import com.netfix.models.network.request.complaint.NewComplaintReqModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.complaint.NewComplaintResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintRepository : BaseRepository() {

    private val TAG = "ComplaintRepository"
    fun fetchAllComplaints(userId: Int, responseLiveData: MutableLiveData<ComplaintResponseModel>) {

        val call: Call<ComplaintResponseModel> =
            NFServiceClient.getClient().getAllComplaints(userId)

        call.enqueue(object : Callback<ComplaintResponseModel> {
            override fun onResponse(
                call: Call<ComplaintResponseModel>,
                response: Response<ComplaintResponseModel>
            ) {

                responseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ComplaintResponseModel>, t: Throwable) {

                responseLiveData.postValue(null)
            }

        })
    }

    fun raiseComplaint(
        complaintVO: ComplaintVO,
        responseLiveData: MutableLiveData<NewComplaintResponseModel>
    ) {
        val call: Call<NewComplaintResponseModel> =
            NFServiceClient.getClient().raiseComplaint(complaintVO)

        call.enqueue(object : Callback<NewComplaintResponseModel> {
            override fun onResponse(
                call: Call<NewComplaintResponseModel>,
                response: Response<NewComplaintResponseModel>
            ) {
                Log.d(TAG, "onResponse: ")
                responseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<NewComplaintResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                responseLiveData.postValue(null)
            }

        })
    }

    fun getAgents(agentResponse: MutableLiveData<ResponseModel<List<UserReqModel>>>) {
        val call: Call<ResponseModel<List<UserReqModel>>> = NFServiceClient.getClient().getAgents()
        call.enqueue(object : Callback<ResponseModel<List<UserReqModel>>> {
            override fun onResponse(
                call: Call<ResponseModel<List<UserReqModel>>>,
                response: Response<ResponseModel<List<UserReqModel>>>
            ) {
                agentResponse.postValue(response.body())
                Log.d(TAG, "onResponse: ")
            }

            override fun onFailure(call: Call<ResponseModel<List<UserReqModel>>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                agentResponse.postValue(null)
            }

        })
    }

    fun assignToAgent(loginReqModel: AssignComplaintModel,assignComplaintResponse :MutableLiveData<ResponseModel<Any>>) {
        val call: Call<ResponseModel<Any>> =
            NFServiceClient.getClient().assignComplaint(loginReqModel)
        call.enqueue(object : Callback<ResponseModel<Any>> {
            override fun onResponse(
                call: Call<ResponseModel<Any>>,
                response: Response<ResponseModel<Any>>
            ) {
                assignComplaintResponse.postValue(response.body())
                Log.d(TAG, "onResponse: ")
            }

            override fun onFailure(call: Call<ResponseModel<Any>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                assignComplaintResponse.postValue(null)
            }

        })
    }

    fun cancelTicket(position: Int, ticketId: String, cancelTicketResponse: MutableLiveData<ResponseModel<String>>) {
        var ticket= ComplaintDetailRepository.Ticket(ticketId)
        val call: Call<ResponseModel<String>> =
            NFServiceClient.getClient().cancelTicket(ticket)
        call.enqueue(object : Callback<ResponseModel<String>> {
            override fun onResponse(
                call: Call<ResponseModel<String>>,
                response: Response<ResponseModel<String>>
            ) {
                cancelTicketResponse.postValue(response.body())
                Log.d(TAG, "onResponse: ")
            }

            override fun onFailure(call: Call<ResponseModel<String>>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                cancelTicketResponse.postValue(null)
            }

        })
    }
}