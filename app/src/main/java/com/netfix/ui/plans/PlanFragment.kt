package com.netfix.ui.plans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.PlanBinding
import com.netfix.db.CWDatabase
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.complaint.ComplaintFragmentDirections
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.ui.service.AddServiceDialogFragment
import com.netfix.ui.service.ItemClickListener
import com.netfix.util.AppConst
import kotlinx.coroutines.launch

class PlanFragment : BaseFragment() , ItemClickListener {

    lateinit var viewModel:PlanViewModel
    lateinit var binding:PlanBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlanViewModel::class.java)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.plan_layout, container, false)

        var tabA = binding.tabLayout.newTab();
        tabA.text = "Multiple Months"

        var tabB = binding.tabLayout.newTab();
        tabB.text = "One Month"
        binding.tabLayout.addTab(tabA)
        binding.tabLayout.addTab(tabB)
        binding.planOption = 0

        binding.model = viewModel
        binding.handler = EventHandler()
        binding.lifecycleOwner = this


        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarPlan)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.isLoading = true

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            binding.userResponseModel = userList.get(0)
        }
        viewModel.allPlanLiveData.observe(viewLifecycleOwner,{
            binding.isLoading = false
            binding.adapter = PlanAdapter(it.response,this)
            Log.d("","")
        })

        binding.isLoading = true
        viewModel.getAllPlans();

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("", "onTabSelected: ")
                if (tab != null) {
                    binding.planOption = tab.position
                    binding.adapter?.filter(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    inner class EventHandler {
        fun addPlan(view: View) {
            val addPlanDialogFragment = AddPlanDialogFragment()
            val bundle = Bundle()
            bundle.putString("title", "Add Plan")
            bundle.putInt("mode", AddPlanDialogFragment.ALERTMODE.ADD.ordinal)
            addPlanDialogFragment.arguments = bundle
            addPlanDialogFragment.show(parentFragmentManager, "addPlan_tag")
        }
    }

    override fun onItemClick(view: View, position: Int) {
        var planVO = binding.adapter?.dataList?.get(position)

        if(binding.planOption == 0 ){
            planVO = binding.adapter?.optionAList?.get(position)
        }else{
            planVO = binding.adapter?.optionBList?.get(position)
        }
        if(binding.userResponseModel?.iD_UserType == AppConst.USER_TYPE_ADMIN) {
            val addPlanDialogFragment = AddPlanDialogFragment()
            val bundle = Bundle()
            bundle.putString("title", "Plan Details")
            bundle.putInt("mode", AddPlanDialogFragment.ALERTMODE.VIEW.ordinal)
            bundle.putSerializable("planVO", planVO)
            addPlanDialogFragment.arguments = bundle
            addPlanDialogFragment.show(parentFragmentManager, "addService_tag")
        }
        else if(binding.userResponseModel?.iD_UserType == AppConst.USER_TYPE_CUSTOMER){
            val action = planVO?.let {
                PlanFragmentDirections.actionPlanFragmentToPaymentFragment(planVO)
            }
            action?.let { NavHostFragment.findNavController(this).navigate(it) }
        }
    }

    override fun onCancelComplaintClicked(position: Int, ticketId: String,view:Button) {
        TODO("Not yet implemented")
    }
}