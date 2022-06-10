package com.netfix.models.network.dataModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanVO(@SerializedName("iD_Services")var serviceId:Int,
                  @SerializedName("iD_PlanDetail")var planId:Int,
                  var planCode:String,
                  var planName:String,
                  var planDescription:String,
                  var planKeyPoints:String,
                  var planDuration:String,
                  var planAmount:Int,
                  var isIncludingTax:Boolean,
                  var isActive:Boolean):Serializable
