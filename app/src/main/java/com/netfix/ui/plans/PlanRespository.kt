package com.netfix.ui.plans

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.plan.PlanAllRequestModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.models.network.response.service.ServiceAllResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanRespository :BaseRepository() {
    fun getAllPlans(allPlanLiveData:MutableLiveData<PlanAllResponseModel>) {

        var planAllRequestModel:PlanAllRequestModel = PlanAllRequestModel()

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

    fun updatePlan() {
        TODO("Not yet implemented")
    }

    fun addPlan() {
        TODO("Not yet implemented")
    }

}