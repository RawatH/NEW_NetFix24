package com.netfix.models.network.dataModel

data class ConnectionVO(var iD_NewConnection:Int,
                        var customerName:String,
                        var mobileNo:String,
                        var emailID:String,
                        var iD_PlanDetail:Int,
                        var planCode:String,
                        var description:String,
                        var createdOn:String
                        )
