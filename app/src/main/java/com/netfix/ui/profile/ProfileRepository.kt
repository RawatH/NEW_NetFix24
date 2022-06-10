package com.netfix.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.dataModel.ProfileVO
import com.netfix.models.network.request.plan.PlanAllRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository : BaseRepository() {

    fun getProfile(profileLiveData: MutableLiveData<ProfileVO>,userId:Int) {

        val call: Call<ResponseModel<ProfileVO>> = NFServiceClient.getClient().getProfile(userId)

        call.enqueue(object : Callback<ResponseModel<ProfileVO>> {
            override fun onResponse(call: Call<ResponseModel<ProfileVO>>, response: Response<ResponseModel<ProfileVO>>) {
                profileLiveData.value = response.body()?.response
            }

            override fun onFailure(call: Call<ResponseModel<ProfileVO>>, t: Throwable) {
                Log.d("","----")
            }

        })
    }


    fun updateProfile(profileLiveData: MutableLiveData<ProfileVO> , profileVO:ProfileVO) {



        val call: Call<ResponseModel<ProfileVO>> = NFServiceClient.getClient().updateProfile(profileVO)
        call.enqueue(object : Callback<ResponseModel<ProfileVO>> {
            override fun onResponse(call: Call<ResponseModel<ProfileVO>>, response: Response<ResponseModel<ProfileVO>>) {
                profileLiveData.value = response.body()?.response
            }

            override fun onFailure(call: Call<ResponseModel<ProfileVO>>, t: Throwable) {
                Log.d("","----")
            }

        })


    }


}


