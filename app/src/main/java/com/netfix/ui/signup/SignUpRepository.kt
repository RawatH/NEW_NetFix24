package com.netfix.ui.signup

import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.request.plan.PlanAllRequestModel
import com.netfix.models.network.request.signup.SignUpRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository  :BaseRepository() {

    fun signUp(signUpVO: SignUpRequestModel,responseLiveData: MutableLiveData<ResponseModel<SignUpRequestModel>>){
        val call: Call<ResponseModel<SignUpRequestModel>> = NFServiceClient.getClient().signUp(signUpVO)

        call.enqueue(object : Callback<ResponseModel<SignUpRequestModel>> {
            override fun onResponse(call: Call<ResponseModel<SignUpRequestModel>>, response: Response<ResponseModel<SignUpRequestModel>>) {

                responseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseModel<SignUpRequestModel>>, t: Throwable) {
                responseLiveData.postValue(null)
            }

        })
    }



    fun getLocations(locationResponseLiveData: MutableLiveData<ResponseModel<List<LocationVO>>>) {
        val call: Call<ResponseModel<List<LocationVO>>> = NFServiceClient.getClient().getLocations()

        call.enqueue(object : Callback<ResponseModel<List<LocationVO>>> {
            override fun onResponse(call: Call<ResponseModel<List<LocationVO>>>, response: Response<ResponseModel<List<LocationVO>>>) {

                locationResponseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseModel<List<LocationVO>>>, t: Throwable) {
                locationResponseLiveData.postValue(null)
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
}