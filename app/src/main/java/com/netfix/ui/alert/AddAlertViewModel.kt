package com.netfix.ui.alert

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.UserVO
import com.netfix.models.network.dataModel.AlertDataVO
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.request.alert.AlertVO
import com.netfix.models.network.response.ResponseModel
import com.netfix.ui.base.BaseViewModel

class AddAlertViewModel(app:NFApplication) : BaseViewModel(app) {
    var repository : AddAlertRepository = AddAlertRepository()
    var locationResponseModel : MutableLiveData<ResponseModel<List<LocationVO>>> = MutableLiveData()
    var userResponseModel : MutableLiveData<ResponseModel<List<UserReqModel>>> = MutableLiveData()

    var alertResponseLiveData:MutableLiveData<ResponseModel<AlertVO>> = MutableLiveData()

    var alert = AlertVO(0,"","","","",true,"","::!",1,true)


    var selectedList = mutableListOf<AlertDataVO>()


    fun fetchAllUsers() {
        repository.getAllUsers(userResponseModel)
    }

    fun fetchAllLocations() {
        repository.getLocations(locationResponseModel)
    }

    fun sendAlertToServer() {
        var idList = ""

        for ((index, value) in selectedList.withIndex()) {
            if(index == selectedList.size-1){
                idList = idList.plus(value.id)
            }else{
                idList = idList.plus(value.id).plus(",")
            }

        }

        alert.AppliedIDList = idList

        repository.submitAlert(alert,alertResponseLiveData)
    }

}