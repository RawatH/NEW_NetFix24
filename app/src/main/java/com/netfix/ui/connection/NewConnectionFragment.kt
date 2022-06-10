package com.netfix.ui.connection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ConnectionBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.dashboard.GPlanAdapter
import com.netfix.ui.dashboard.PlanServiceDialogFragment
import com.netfix.ui.login.LoginViewModelFactory

class NewConnectionFragment : BaseFragment() {

    lateinit var binding: ConnectionBinding
    lateinit var viewModel: ConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ConnectionViewModel::class.java)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.new_connection_layout,
            container,
            false
        )
        binding.reqModel = viewModel.connectionRequestModel
        binding.handler = EventHandler()
        binding.setLifecycleOwner(viewLifecycleOwner)

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarNc)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.connectionResponseLiveData.observe(viewLifecycleOwner, {
            Log.d("", "")
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        })

        viewModel.allPlanLiveData.observe(viewLifecycleOwner, {
            Log.d("", "")
            binding.arePlanLoaded = true
        })
        viewModel.getAllPlans()

        parentFragmentManager.setFragmentResultListener("RESULT", this, { key, bundle ->
            var id = bundle.getInt("id", 0)
            var name = bundle.getString("name")
            viewModel.connectionRequestModel.ID_PlanDetail = id
            binding.planName.text = name
            binding.chipGroup.visibility = View.VISIBLE

        })
    }

    inner class EventHandler {
        fun choosePlan(view: View) {
            var adapter = GPlanAdapter<PlanVO>()

            viewModel.allPlanLiveData.value?.let { adapter.setData(it.response) }
            val planDialogFragment = PlanServiceDialogFragment(adapter)
            adapter.itemClickListener = planDialogFragment
            val bundle = Bundle()
            bundle.putString("title", "Choose Plan")
            planDialogFragment.arguments = bundle
            planDialogFragment.show(parentFragmentManager, "viewPlan_tag")

        }

        fun submitConnectionRequest(view: View) {
            viewModel.requestNewConnection()
        }
    }
}