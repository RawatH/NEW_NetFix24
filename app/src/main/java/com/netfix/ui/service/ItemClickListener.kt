package com.netfix.ui.service

import android.view.View
import android.widget.Button

interface ItemClickListener {
    fun onItemClick(view: View, position : Int)
    fun onCancelComplaintClicked(position: Int,ticketId:String , view: Button)
}