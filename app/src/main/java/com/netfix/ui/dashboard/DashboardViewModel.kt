package com.netfix.ui.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netfix.app.NFApplication
import com.netfix.db.CWDatabase
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.models.network.response.banners.BannerResponseModel
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.login.UserLoginResponseModel
import com.netfix.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DashboardViewModel(application: NFApplication): BaseViewModel(application) {
    val allBannerLiveData : MutableLiveData<BannerResponseModel> = MutableLiveData();
    val feedbackLiveData : MutableLiveData<ResponseModel<FeedbackRequestModel>> = MutableLiveData()
    val alertLiveData : MutableLiveData<ResponseModel<List<AlertMsgVO>>> = MutableLiveData()
    val complaintLiveData: MutableLiveData<ComplaintResponseModel> = MutableLiveData()


    val alertMsgVO:MutableLiveData<AlertMsgVO> = MutableLiveData()
    val currentComplaintVO:MutableLiveData<ComplaintVO> = MutableLiveData()

    val repository:DashboardRepository = DashboardRepository()

    fun getBanners(){
        repository.getBanners(allBannerLiveData)
    }

    fun sendFeedback(model:FeedbackRequestModel) {
        repository.sendFeedbackReqmodel(model,feedbackLiveData)
    }

    fun getUserAlert(){
        val db = CWDatabase.getDatabase(getApplication())
        viewModelScope.launch {

            var userModel:List<UserLoginResponseModel> = db.userLoginResponseDao().getLoggedUser()

            repository.getUserAlert(userModel.get(0).iD_User,alertLiveData)

        }

    }

    fun getAllComplaints(){
        val db = CWDatabase.getDatabase(getApplication())
        viewModelScope.launch {

            var userModel:List<UserLoginResponseModel> = db.userLoginResponseDao().getLoggedUser()

            repository.getComplaints(userModel.get(0).iD_User,complaintLiveData)

        }

    }


}