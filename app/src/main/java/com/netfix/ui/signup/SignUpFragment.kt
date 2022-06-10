package com.netfix.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ServiceBinding
import com.netfix.databinding.SignUpBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.models.network.request.connection.ConnectionRequestModel
import com.netfix.models.network.request.signup.SignUpRequestModel
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.dashboard.GPlanAdapter
import com.netfix.ui.dashboard.PlanServiceDialogFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.ui.service.AddServiceDialogFragment
import com.netfix.ui.service.ServiceViewModel
import com.netfix.util.AppConst

class SignUpFragment : BaseFragment() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: SignUpBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)
        viewModel.signUpRequestModelLiveData.value = SignUpRequestModel()
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.sign_up_fragment, container, false)

        binding.model = viewModel
        binding.handler = EventHandler()
        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarService)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("RESULT", this, { key, bundle ->
            var id = bundle.getInt("id", 0)
            var name = bundle.getString("name")
            viewModel.connectionRequestModel.ID_PlanDetail = id
            binding.planName.text = name
            binding.chipGroup.visibility = View.VISIBLE

        })

        viewModel.allPlanLiveData.observe(viewLifecycleOwner, {
            Log.d("", "")
            binding.arePlanLoaded = true
        })

        val items = listOf("Customer", "Agent")
        binding.userType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                items
            )
        )


        binding.userType.setOnItemClickListener { parent, view, position, id ->
            viewModel.signUpRequestModelLiveData.value?.iD_UserType =
                if (position == 0) AppConst.USER_TYPE_CUSTOMER else AppConst.USER_TYPE_AGENT

            if (viewModel.signUpRequestModelLiveData.value?.iD_UserType == AppConst.USER_TYPE_CUSTOMER) {
                binding.planSelectionView.visibility = View.VISIBLE
            } else {
                binding.planSelectionView.visibility = View.GONE
                viewModel.connectionRequestModel = ConnectionRequestModel()
                binding.planName.text = ""
            }


        }

        viewModel.signUpResultLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.locationResponseModel.observe(viewLifecycleOwner, {
            it?.let {
                val locationList = mutableListOf<String>()

                for (location in it.response) {
                    locationList.add(location.name)
                }
                binding.area.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        locationList
                    )
                )
                binding.submitSignUp.setEnabled(true)

                binding.area.setOnItemClickListener { parent, view, position, id ->
                    viewModel.signUpRequestModelLiveData.value?.iD_AddressArea =
                        it.response.get(position).iD_AddressArea
                }

            }
        })



        viewModel.getLocations()

        binding.submitSignUp.setOnClickListener {
            viewModel.signupUser()
        }

        viewModel.getAllPlans()
    }

    inner class EventHandler {
        fun onClick(view: View) {
            if (view.id == R.id.submitSignUp) {
                val msg = viewModel.signUpRequestModelLiveData.value?.isValid()
                if (msg != null) {
                    if(!msg.isBlank()){
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                }
                if (viewModel.signUpRequestModelLiveData.value?.iD_UserType == 0) {
                    Toast.makeText(requireContext(), "Please select user type.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (viewModel.signUpRequestModelLiveData.value?.iD_UserType == AppConst.USER_TYPE_CUSTOMER) {
                    if (viewModel.connectionRequestModel.ID_PlanDetail == 0) {
                        Toast.makeText(
                            requireContext(),
                            "Please select plan for customer",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
                if (viewModel.signUpRequestModelLiveData.value?.iD_AddressArea == 0) {
                    Toast.makeText(requireContext(), "Please select location", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                viewModel.signupUser()
            } else {
                Navigation.findNavController(view).navigateUp()
            }
        }

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
    }


}