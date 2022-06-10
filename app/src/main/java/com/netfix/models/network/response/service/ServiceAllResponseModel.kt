package com.netfix.models.network.response.service

import android.util.Log
import com.netfix.models.network.dataModel.ServiceVO

data class ServiceAllResponseModel(
    var status: Boolean,
    var code: String,
    var message: String,
    var responseValue: String,
    var transactionID: String,
    var response: List<ServiceVO>
){

    private  val TAG = "ServiceAllResponseModel"

    fun getAllServices():List<ServiceVO>{
        val serviceList:MutableList<ServiceVO> = mutableListOf()
        response?.let {
            for (i in response) {
                Log.d(TAG," --"+i.toString())
                serviceList.add(i)
            }

        }
        serviceList.reverse()
        return serviceList
    }
}