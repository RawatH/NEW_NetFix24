package com.netfix.models.network.dataModel


data class UserPlanVO(var iD_PlanDetail:Int,
                      var planName:String,
                      var registrationDate:String,
                      var planRenewalDate:String,
                      var planExpiryDate:String,
                      var paymentDate:String,
                      var paymentMode:String,
                      var paymentAmount:Int,
                      var isActive:Boolean
                      )
