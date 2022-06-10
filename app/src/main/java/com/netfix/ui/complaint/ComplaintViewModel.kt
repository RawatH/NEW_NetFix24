package com.netfix.ui.complaint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.netfix.app.NFApplication
import com.netfix.db.CWDatabase
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.request.complaint.AssignComplaintModel
import com.netfix.models.network.request.complaint.NewComplaintReqModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.complaint.NewComplaintResponseModel
import com.netfix.models.network.response.login.UserLoginResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintViewModel(app: NFApplication) : BaseViewModel(app) {

    var selectedAgentId: Int = 0
    var cancelTicketResponse = MutableLiveData<ResponseModel<String>>()
    var assignComplaintResponse = MutableLiveData<ResponseModel<Any>>()
    val isComplaintSelectionOn = MutableLiveData<Boolean>()
    var agentResponse = MutableLiveData<ResponseModel<List<UserReqModel>>>()
    var allComplaintsLiveData = MutableLiveData<ComplaintResponseModel>()
    var newComplaintResponseLiveData = MutableLiveData<NewComplaintResponseModel>()
    var repository = ComplaintRepository()


    fun getAllComplaints() {
        val db = CWDatabase.getDatabase(getApplication())
        viewModelScope.launch {
            var userModel: List<UserLoginResponseModel> = db.userLoginResponseDao().getLoggedUser()
            repository.fetchAllComplaints(userModel.get(0).iD_User, allComplaintsLiveData)

        }

    }

    fun raiseComplaint(complaintVO: ComplaintVO) {
        repository.raiseComplaint(complaintVO, newComplaintResponseLiveData)
    }

    fun getAgents() {
        repository.getAgents(agentResponse)
    }

    fun assignToAgent(loginReqModel: AssignComplaintModel) {
        repository.assignToAgent(loginReqModel, assignComplaintResponse)
    }

    fun cancelTicket(position: Int, ticketId: String) {
        repository.cancelTicket(position, ticketId, cancelTicketResponse);
    }



}