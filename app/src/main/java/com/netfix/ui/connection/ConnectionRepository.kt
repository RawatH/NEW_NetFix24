package com.netfix.ui.connection

import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.dataModel.ConnectionVO
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.request.plan.PlanAllRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.connection.ConnectionResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnectionRepository : BaseRepository() {

    fun requestNewConnection(connectionRequestModel: ConnectionRequestModel,connectionResponseLiveData:MutableLiveData<ConnectionResponseModel>){

        val call: Call<ConnectionResponseModel> = NFServiceClient.getClient().requestNewConnection(connectionRequestModel)

        call.enqueue(object : Callback<ConnectionResponseModel> {
            override fun onResponse(call: Call<ConnectionResponseModel>, response: Response<ConnectionResponseModel>) {

                connectionResponseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ConnectionResponseModel>, t: Throwable) {

                connectionResponseLiveData.postValue(null)
            }

        })

    }

    fun getAllPlans(allPlanLiveData: MutableLiveData<PlanAllResponseModel>) {

        var planAllRequestModel: PlanAllRequestModel = PlanAllRequestModel()

        val call: Call<PlanAllResponseModel> = NFServiceClient.getClient().getAllPlans(planAllRequestModel)

        call.enqueue(object : Callback<PlanAllResponseModel> {
            override fun onResponse(call: Call<PlanAllResponseModel>, response: Response<PlanAllResponseModel>) {

                allPlanLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<PlanAllResponseModel>, t: Throwable) {

                allPlanLiveData.postValue(null)
            }

        })
    }

    fun getAllConnections(connectionReqLiveData : MutableLiveData<ResponseModel<List<ConnectionVO>>>) {

        val call: Call<ResponseModel<List<ConnectionVO>>> = NFServiceClient.getClient().getAllConnections()

        call.enqueue(object : Callback<ResponseModel<List<ConnectionVO>>> {
            override fun onResponse(call: Call<ResponseModel<List<ConnectionVO>>>, response: Response<ResponseModel<List<ConnectionVO>>>) {

                connectionReqLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseModel<List<ConnectionVO>>>, t: Throwable) {

                connectionReqLiveData.postValue(null)
            }

        })

    }
}