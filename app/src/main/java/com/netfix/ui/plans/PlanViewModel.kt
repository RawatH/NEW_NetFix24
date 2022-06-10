package com.netfix.ui.plans

import androidx.lifecycle.MutableLiveData
import com.netfix.app.NFApplication
import com.netfix.models.network.response.plan.PlanAllResponseModel
import com.netfix.ui.base.BaseViewModel

class PlanViewModel(application:NFApplication):BaseViewModel(application) {

    var allPlanLiveData:MutableLiveData<PlanAllResponseModel> = MutableLiveData()
    var repository:PlanRespository = PlanRespository()

    fun getAllPlans() {
        repository.getAllPlans(allPlanLiveData)
    }

    fun updatePlan(){
        repository.updatePlan()
    }
    fun addPlan(){
        repository.addPlan()
    }
}