package com.netfix.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.PaymentBinding
import com.netfix.db.CWDatabase
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.util.AppConst
import kotlinx.coroutines.launch


class PaymentFragment : BaseFragment() {
    lateinit var viewModel: PaymentViewModel
    lateinit var binding: PaymentBinding
    val args: PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.payment_fragment, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarPayment)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        try {
            var planVO = args.userPlanVO
            binding.planValidity.text = "Validity till : "+planVO.planDuration
            binding.planName.text = "Plan : "+ planVO.planName
            binding.planAmount.text = "\u20B9 " +planVO.planAmount

        } catch (e: Exception) {
            viewLifecycleOwner.lifecycleScope.launch {
                val db = CWDatabase.getDatabase(requireContext())
                var userList = db.userLoginResponseDao().getLoggedUser()
                var planVO = userList.get(0).userPlanPaymentDetails.get(0)
                binding.planValidity.visibility = View.GONE
                binding.planName.text = "Plan : "+ planVO.planName
                binding.planAmount.text = "\u20B9 " +planVO.paymentAmount
            }
        }

        binding.pay.setOnClickListener {
            val url = AppConst.INSTANT_RECHARGE
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        return binding.root
    }
}