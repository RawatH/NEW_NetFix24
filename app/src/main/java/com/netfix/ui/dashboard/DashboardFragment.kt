package com.netfix.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.DashboardBinding
import com.netfix.db.CWDatabase
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.models.network.response.alert.AlertMsgVO
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.ui.notification.NotificationAlertDialogFragment
import com.netfix.util.AppConst
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : BaseFragment() {
    lateinit var binding: DashboardBinding
    lateinit var viewModel: DashboardViewModel

    private val TAG = "DashboardFragment"
    private val USER_FEEDBACK_REQ = "FEEDBACK_USER_REQ";

    companion object {
        val CURR_TS = System.currentTimeMillis()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dashboard_layout, container, false)
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        binding.signupUser.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navHome_to_customerFragment)
        }

        binding.planStaus.setOnClickListener {
            val url = AppConst.PLAN_STATUS
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.instantRecharge.setOnClickListener {
            val url = AppConst.INSTANT_RECHARGE
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.notificationBell.setOnClickListener {
            showAlertNotifications()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            binding.userResponseModel = userList.get(0)
            binding.userPlanVO = userList.get(0).userPlanPaymentDetails.get(0)
            Log.d(TAG, "onViewStateRestored: " + binding.userResponseModel)
        }


        viewModel.feedbackLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

        })

        viewModel.complaintLiveData.observe(viewLifecycleOwner, {
            it.message?.let {
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            if (it.code == 200) {
                var complaintVO = it.response.filter {
                    it.ticketStatus.equals("Open")
                }
                if (!complaintVO.isEmpty()) {
                    viewModel.currentComplaintVO.value = complaintVO.get(0)
                    refreshTimer()
                }

            }
        })

        viewModel.alertLiveData.observe(viewLifecycleOwner, {
            it.response.let {
                if (it != null && it.isNotEmpty()) {
                    viewModel.alertMsgVO.value = it.get(0)
                    binding.viewModel = viewModel
                    binding.count = it.size
                    binding.notificationBell.visibility = View.VISIBLE

                }
            }
        })

        viewModel.allBannerLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, "onViewStateRestored: ")
            val bannerAdapter = BannerAdapter(this, it.response)
            binding.bannerPager.adapter = bannerAdapter

            binding.dotsIndicator.setViewPager2(binding.bannerPager)

        })

        binding.actionProfile.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.navProfile);
        }

        binding.custFeedback.dashboardAction.setOnClickListener {
            openFeedback()
        }

        binding.actionServices.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_dashboardFragment_to_serviceFragment)
        }

        binding.betterDeal.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_dashboardFragment_to_planFragment)
        }


        binding.actionQuery.dashboardAction.setOnClickListener {
            val dashboardActivity = activity as DashboardActivity
            dashboardActivity.performBottomNavigationAction(R.id.navHelp)

        }

        binding.actionPlan.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_dashboardFragment_to_planFragment)
        }

        binding.actionPayment.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.paymentFragment)
        }

        binding.actionAddAlert.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_dashboardFragment_to_addAlertFragment)
        }

        binding.actionConnections.dashboardAction.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navHome_to_connectionFragment)
        }

//        viewModel.getBanners()
        viewModel.getUserAlert()
        viewModel.getAllComplaints()

        parentFragmentManager.setFragmentResultListener("FEEDBACK_USER", this, { key, bundle ->

            val feedbackReqModel = bundle.get("model") as FeedbackRequestModel
            viewModel.sendFeedback(feedbackReqModel)

        })

    }

    private fun showAlertNotifications() {
        val notificationAlertDialogFragment = NotificationAlertDialogFragment()

        val bundle = Bundle()
        bundle.putSerializable("dataList", viewModel.alertLiveData.value?.response as Serializable)
        bundle.putBoolean("isAdmin", loggedUser.iD_UserType == AppConst.USER_TYPE_ADMIN);

        notificationAlertDialogFragment.arguments = bundle
        notificationAlertDialogFragment.show(parentFragmentManager, null)
    }


    fun openFeedback() {
        if (binding.userResponseModel?.iD_UserType == AppConst.USER_TYPE_CUSTOMER) {
            val feedbackDialog = FeedbackDialog()
            feedbackDialog.show(parentFragmentManager, "feedback")

        } else {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navHome_to_feedbackFragment)
        }


    }

    fun refreshTimer() {
        var refreshRunnable = Runnable() {

            viewModel.currentComplaintVO.value?.let {
                viewModel.currentComplaintVO.value = it
            }

            refreshTimer()
        }
        Handler(Looper.getMainLooper()).postDelayed(refreshRunnable, 1000)
    }

}

@BindingAdapter("timer")
fun setTimer(view: TextView, complaintVO: ComplaintVO?) {

    complaintVO?.ticketTimeStemp?.let {
        val startTs = it
        val fourHour = 4 * 60 * 60 * 1000
        var endTs = startTs + fourHour
        while (endTs < DashboardFragment.CURR_TS) {
            endTs = endTs + fourHour
        }
        val currTs = System.currentTimeMillis()

        if (startTs <= currTs && currTs <= endTs) {
            view.visibility = View.VISIBLE
            var diff = endTs - currTs
            var hour = 0L
            val oneHourMS = 60 * 60 * 1000
            if (diff >= oneHourMS) {
                hour = diff / oneHourMS
                diff = diff - (hour * oneHourMS)
            }

            var minute = 0L

            if (diff > 59999) {
                minute = diff / 60000
                diff = diff - (minute * 60 * 1000)
            }

            var sec = 0L
            if (diff > 1000) {
                sec = diff / 1000

            }

            var timer =
                hour.toString() + "h  : " + minute.toString() + "m : " + sec.toString() + "s"

            view.text = timer

        } else {
            view.visibility = View.GONE
            view.text = "0h : 0m : 0s"
        }
    }


}


