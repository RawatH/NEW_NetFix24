package com.netfix.models.network.dataModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServiceVO(@SerializedName("iD_Services")var serviceId:Int,
                     var serviceCode:String,
                     var serviceName:String,
                     var serviceDescription:String,
                     var isActive:Boolean):Serializable{
    var createdBy:Int = 1

    constructor(serviceCode:String , serviceName: String,serviceDescription: String,isActive: Boolean):this(0,serviceCode,serviceName,serviceDescription,isActive)
}