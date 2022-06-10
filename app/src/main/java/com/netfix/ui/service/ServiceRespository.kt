package com.netfix.ui.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.netfix.models.network.request.service.AddServiceRequestModel
import com.netfix.models.network.request.service.ServiceRequestModel
import com.netfix.models.network.request.service.UpdateServiceRequestModel
import com.netfix.models.network.response.service.AddServiceResponseModel
import com.netfix.models.network.response.service.ServiceAllResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceRespository : BaseRepository() {
    private  val TAG = "DashboardRespository"

    fun getAllServices(serviceRequestModel: ServiceRequestModel, allServiceLiveData: MutableLiveData<ServiceAllResponseModel>){
        val call: Call<ServiceAllResponseModel> = NFServiceClient.getClient().getAllServices(serviceRequestModel)

        call.enqueue(object : Callback<ServiceAllResponseModel> {
            override fun onResponse(call: Call<ServiceAllResponseModel>, response: Response<ServiceAllResponseModel>) {

                Log.d(TAG, "onResponse: ")
                allServiceLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ServiceAllResponseModel>, t: Throwable) {

                Log.d(TAG, "onFailure: "+t.message)
            }

        })
    }

    fun addService(addServiceRequestModel: AddServiceRequestModel, addServiceLiveData:MutableLiveData<AddServiceResponseModel>){

        val call: Call<AddServiceResponseModel> = NFServiceClient.getClient().addService(addServiceRequestModel)

        call.enqueue(object : Callback<AddServiceResponseModel> {
            override fun onResponse(call: Call<AddServiceResponseModel>, response: Response<AddServiceResponseModel>) {

                Log.d(TAG, "onResponse: ")
                addServiceLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<AddServiceResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: "+t.message)
                addServiceLiveData.postValue(null)
            }

        })

    }

    fun updateService(updateServiceRequestModel: UpdateServiceRequestModel, addServiceLiveData:MutableLiveData<AddServiceResponseModel>){

        val call: Call<AddServiceResponseModel> = NFServiceClient.getClient().updateService(updateServiceRequestModel)

        call.enqueue(object : Callback<AddServiceResponseModel> {
            override fun onResponse(call: Call<AddServiceResponseModel>, response: Response<AddServiceResponseModel>) {

                Log.d(TAG, "onResponse: ")
                addServiceLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<AddServiceResponseModel>, t: Throwable) {

                Log.d(TAG, "onFailure: "+t.message)
                addServiceLiveData.postValue(null)
            }

        })

    }
}