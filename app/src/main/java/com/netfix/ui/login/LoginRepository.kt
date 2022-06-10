package com.netfix.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.login.LoginRequestModel
import com.netfix.models.network.response.login.LoginResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository :BaseRepository() {

    private  val TAG = "LoginRepository"

    fun validateUser(loginRequestModel: LoginRequestModel, loginReqLiveData: MutableLiveData<LoginResponseModel>){

        val call:Call<LoginResponseModel>  = NFServiceClient.getClient().validateUser(loginRequestModel)

        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {

                Log.d(TAG, "onResponse: ")
                loginReqLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                loginReqLiveData.postValue(null)
            }

        })


    }
}