package com.netfix.ui.connection

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.dataModel.ConnectionVO
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.connection.ConnectionResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.ui.base.BaseViewModel

class ConnectionViewModel(app:NFApplication):BaseViewModel(app) {

    val connectionRequestModel = ConnectionRequestModel()
    val repository:ConnectionRepository = ConnectionRepository()
    var allPlanLiveData: MutableLiveData<PlanAllResponseModel> = MutableLiveData()
    var connectionResponseLiveData: MutableLiveData<ConnectionResponseModel> = MutableLiveData()

    var connectionReqLiveData: MutableLiveData<ResponseModel<List<ConnectionVO>>> =
        MutableLiveData<ResponseModel<List<ConnectionVO>>>()

    fun requestNewConnection(){
        if(connectionRequestModel.CustomerName.isNotEmpty() &&
            connectionRequestModel.Description.isNotEmpty() &&
            connectionRequestModel.EmailID.isNotEmpty()) {
            repository.requestNewConnection(connectionRequestModel,connectionResponseLiveData)
        }
    }

    fun getAllPlans() {
        repository.getAllPlans(allPlanLiveData)
    }

    fun getAllConnectionRequests(){
        repository.getAllConnections(connectionReqLiveData)
    }



}