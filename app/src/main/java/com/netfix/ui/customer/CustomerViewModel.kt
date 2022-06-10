package com.netfix.ui.customer

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.ui.base.BaseViewModel

class CustomerViewModel(application: NFApplication) : BaseViewModel(application) {
    var repository: CustomerRepository = CustomerRepository()
    var userResponseModel: MutableLiveData<ResponseModel<List<UserReqModel>>> = MutableLiveData()

    fun fetchAllUsers() {
        repository.getAllUsers(userResponseModel)
    }
}