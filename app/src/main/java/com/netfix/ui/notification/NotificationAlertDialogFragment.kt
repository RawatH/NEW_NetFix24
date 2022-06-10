package com.netfix.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.netfix.databinding.NotificationAlertBinding
import com.netfix.models.network.request.alert.AlertVO
import com.netfix.models.network.response.ResponseModel
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.models.network.response.complaint.ComplaintResponseModel
import com.netfix.network.NFServiceClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationAlertDialogFragment : DialogFragment(), AlertCallback {

    lateinit var binding: NotificationAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationAlertBinding.inflate(inflater)
        binding.isLoading = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList: List<AlertMsgVO> =
            arguments?.getSerializable("dataList") as List<AlertMsgVO>

        val adapter = NotificationAdapter(arguments?.getBoolean("isAdmin") as Boolean, this)
        adapter.setData(dataList)

        binding.adapter = adapter

        binding.closeNotificationAlert.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.getWindow()?.setLayout(width, height)
        }
    }

    override fun cancelAlert(alerMsgVO: AlertMsgVO) {

        binding.isLoading = true
        val alertCancelReq = AlertCancelReq(alerMsgVO.iD_MessageAlert)

        val call: Call<ResponseModel<AlertVO>> =
            NFServiceClient.getClient().cancelAlert(alertCancelReq)

        call.enqueue(object : Callback<ResponseModel<AlertVO>> {
            override fun onResponse(
                call: Call<ResponseModel<AlertVO>>,
                response: Response<ResponseModel<AlertVO>>
            ) {
                binding.isLoading = false
                if(response.code() == 200){
                    Toast.makeText(requireContext(),response.raw().message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel<AlertVO>>, t: Throwable) {
                binding.isLoading = false
            }

        })

    }
}

class AlertCancelReq(var iD_MessageAlert: Int, var isActive: Boolean = false)

interface AlertCallback {
    fun cancelAlert(alerMsgVO: AlertMsgVO)
}