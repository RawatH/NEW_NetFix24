package com.netfix.ui.signup

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.dataModel.LocationVO
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.request.signup.SignUpRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.ui.base.BaseViewModel

class SignUpViewModel(nfApp: NFApplication) :BaseViewModel(nfApp)  {
    var connectionRequestModel = ConnectionRequestModel()
    var signUpRequestModelLiveData : MutableLiveData<SignUpRequestModel> = MutableLiveData()
    var locationResponseModel : MutableLiveData<ResponseModel<List<LocationVO>>> = MutableLiveData()
    var repository : SignUpRepository = SignUpRepository()
    var allPlanLiveData: MutableLiveData<PlanAllResponseModel> = MutableLiveData()

    var signUpResultLiveData : MutableLiveData<ResponseModel<SignUpRequestModel>> = MutableLiveData()

    fun signupUser() {
        signUpRequestModelLiveData.value?.ID_PlanDetail =  connectionRequestModel.ID_PlanDetail
        signUpRequestModelLiveData.value?.let { repository.signUp(it,signUpResultLiveData) }
    }

    fun getLocations(){
        repository.getLocations(locationResponseModel)
    }

    fun getAllPlans() {
        repository.getAllPlans(allPlanLiveData)
    }
}