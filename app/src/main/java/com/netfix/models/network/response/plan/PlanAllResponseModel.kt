package com.netfix.models.network.response.plan

import com.netfix.models.BaseModel
import com.netfix.models.network.dataModel.PlanVO

class PlanAllResponseModel(var status:Boolean,
                           var code:Int,
                           var message:String?,
                           var responseValue:String,
                           var transactionID:String,
                           var response:List<PlanVO>):BaseModel()