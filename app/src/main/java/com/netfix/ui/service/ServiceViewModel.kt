package com.netfix.ui.service

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.request.service.AddServiceRequestModel
import com.netfix.models.network.request.service.ServiceRequestModel
import com.netfix.models.network.request.service.UpdateServiceRequestModel
import com.netfix.models.network.response.service.AddServiceResponseModel
import com.netfix.models.network.response.service.ServiceAllResponseModel
import com.netfix.ui.base.BaseViewModel

class ServiceViewModel(application: NFApplication): BaseViewModel(application) {
    var allServiceLiveData:MutableLiveData<ServiceAllResponseModel> = MutableLiveData<ServiceAllResponseModel>()
    var addServiceLiveData:MutableLiveData<AddServiceResponseModel> = MutableLiveData<AddServiceResponseModel>()

    var respository: ServiceRespository = ServiceRespository()

    fun getAllServices(){
        val requestModel = ServiceRequestModel()
        requestModel.id_services = 0
        respository.getAllServices(requestModel,allServiceLiveData)
    }

    fun addService(addServiceRequestModel: AddServiceRequestModel){
        respository.addService(addServiceRequestModel,addServiceLiveData)
    }

    fun updateService(updateServiceRequestModel: UpdateServiceRequestModel){
        respository.updateService(updateServiceRequestModel,addServiceLiveData)
    }
}