package com.netfix.ui.customer

import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.network.NFServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerRepository {

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