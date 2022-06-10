package com.netfix.ui.complaint

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.models.network.response.complaint.NewComplaintResponseModel
import com.netfix.ui.base.BaseViewModel

class ComplaintDetailViewModel(app:NFApplication) : BaseViewModel(app) {
    var repository = ComplaintDetailRepository()
    var ticketLiveData:MutableLiveData<ComplaintResponseModel> = MutableLiveData()
    var complaintResponseLiveData = MutableLiveData<NewComplaintResponseModel>()

    fun getTicketDetails(ticketId: String) {
      repository.getTicketDetail(ticketId,ticketLiveData)
    }

    fun sendMessage(complaintVO: ComplaintVO) {
       repository.sendComplaintMsg(complaintVO,complaintResponseLiveData);
    }

}