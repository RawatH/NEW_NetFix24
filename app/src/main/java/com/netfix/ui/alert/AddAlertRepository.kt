package com.netfix.ui.alert

import androidx.lifecycle.MutableLiveData
import com.netfix.models.UserVO
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.request.alert.AlertVO
import com.netfix.models.network.response.ResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAlertRepository : BaseRepository() {

    private val TAG = "AddAlertRepository"

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

    fun submitAlert(alert: AlertVO, alertResponseLiveData: MutableLiveData<ResponseModel<AlertVO>>) {
        val call: Call<ResponseModel<AlertVO>> = NFServiceClient.getClient().submitAlert(alert)

        call.enqueue(object : Callback<ResponseModel<AlertVO>> {
            override fun onResponse(call: Call<ResponseModel<AlertVO>>, response: Response<ResponseModel<AlertVO>>) {

                alertResponseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseModel<AlertVO>>, t: Throwable) {
                alertResponseLiveData.postValue(null)
            }

        })
    }

    fun getAllUsers(userResponseModel: MutableLiveData<ResponseModel<List<UserReqModel>>>) {

        val call: Call<ResponseModel<List<UserReqModel>>> = NFServiceClient.getClient().getAllUsers(Any())

        call.enqueue(object : Callback<ResponseModel<List<UserReqModel>>> {
            override fun onResponse(call: Call<ResponseModel<List<UserReqModel>>>, response: Response<ResponseModel<List<UserReqModel>>>) {

                userResponseModel.postValue(response.body())
            }

            override fun onFailure(call: Call<ResponseModel<List<UserReqModel>>>, t: Throwable) {
                userResponseModel.postValue(null)
            }

        })
    }
}