package com.netfix.ui.notification


import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.AlertCellBinding
import com.netfix.databinding.AlertNotificationBinding
import com.netfix.models.network.dataModel.AlertDataVO
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.ui.dashboard.DashboardViewModel
import com.netfix.ui.dashboard.GenericAdapter
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(val isAdmin:Boolean , val callback:AlertCallback) : GenericAdapter<AlertMsgVO>() {
    var orignalList = mutableListOf<AlertMsgVO>()


    fun setData(dataList: List<AlertMsgVO>) {
        if (orignalList.size == 0) {
            orignalList.addAll(dataList)
        }
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: AlertMsgVO): Int {
        return R.layout.alert_notification_layout;
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val binding: AlertNotificationBinding = AlertNotificationBinding.bind(view)
        return NotificationCellViewHolder(binding)
    }

    inner class NotificationCellViewHolder(val binding: AlertNotificationBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<AlertMsgVO> {
        override fun bind(data: AlertMsgVO) {
            binding.alertMsgVO = data
            binding.cancelAlert.setOnClickListener{
                callback.cancelAlert(data)
            }
            refreshTimer()
            setCancelVisibility(data)
        }

        fun setCancelVisibility(data: AlertMsgVO){
            val startTs = data.startDateTime.toLong()
            val endTs = data.endDateTime.toLong()
            val currTs = System.currentTimeMillis()

            if (startTs <= currTs && currTs <= endTs){
                binding.isAdmin = isAdmin
            }else{
                binding.isAdmin = false
            }
            binding.executePendingBindings()
        }

        fun refreshTimer(){
            var refreshRunnable = Runnable() {
                binding.alertMsgVO = binding.alertMsgVO
                refreshTimer()
            }
            Handler(Looper.getMainLooper()).postDelayed(refreshRunnable, 1000)
        }

    }
}

@BindingAdapter("dateFormat")
fun formatDate(view: TextView, alertMsgVO: AlertMsgVO?) {

    alertMsgVO?.let {
        val formatter = SimpleDateFormat("dd MMM yyyy , hh:mm aaa");

        var startMs = it.startDateTime.toLong()
        var endMs = it.endDateTime.toLong()


        var calendar = Calendar.getInstance()
        calendar.setTimeInMillis(startMs)
        val startTime = formatter.format(calendar.getTime())
        calendar.setTimeInMillis(endMs)
        val endTime = formatter.format(calendar.getTime())
        view.text = String.format(view.resources.getString(R.string.alertMsg), startTime,  endTime)
    }


}

@BindingAdapter("timer")
fun setTimer(view: TextView, alertMsgVO: AlertMsgVO) {

    val startTs = alertMsgVO.startDateTime.toLong()
    val endTs = alertMsgVO.endDateTime.toLong()
    val currTs = System.currentTimeMillis()

    if (startTs <= currTs && currTs <= endTs) {
        view.visibility = View.VISIBLE
        var diff = endTs - currTs
        var hour = 0L
        val oneHourMS = 3600000
        if (diff >= oneHourMS) {
            hour = diff / oneHourMS
            diff = diff - (hour * oneHourMS)
        }

        var minute = 0L

        if(diff > 59999){
            minute = diff / 60000
            diff = diff - (minute * 60 * 1000)
        }

        var sec = 0L
        if(diff > 1000){
            sec = diff /1000

        }

        var timer = hour.toString() + "h  : " + minute.toString() + "m : " + sec.toString() +"s"

        view.text = timer

    }else{
        view.visibility = View.GONE
        view.text = "0h : 0m : 0s"
    }

}