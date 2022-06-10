package com.netfix.models.network.response.banners

import com.netfix.models.network.dataModel.BannerVO

data class BannerResponseModel(var status:Boolean, var message:String,var code:Int,var response:List<BannerVO>?)