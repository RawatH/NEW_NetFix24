package com.netfix.ui.base

import com.netfix.network.NFAPIServices
import javax.inject.Inject

open class BaseRepository @Inject constructor(){
    @Inject  lateinit var apiServices: NFAPIServices
}