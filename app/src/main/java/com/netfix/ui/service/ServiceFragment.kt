package com.netfix.ui.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ServiceBinding
import com.netfix.db.CWDatabase
import com.netfix.models.network.request.service.AddServiceRequestModel
import com.netfix.models.network.request.service.UpdateServiceRequestModel
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import kotlinx.coroutines.launch

class ServiceFragment : BaseFragment(), ItemClickListener {

    private val TAG = "ServiceFragment"


    private lateinit var viewModel: ServiceViewModel
    private lateinit var binding: ServiceBinding

    private val REQ_KEY = "REQ_ADD_SERVICE"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ServiceViewModel::class.java)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.service_layout, container, false)
        binding.model = viewModel
        binding.handler = EventHandler()
        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarService)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            binding.userResponseModel = userList.get(0)
        }

        viewModel.allServiceLiveData.observe(viewLifecycleOwner, Observer {
            binding.isLoading = false
            binding.adapter = ServiceAdapter(it.getAllServices(), this);
            Log.d(TAG, it.getAllServices().toString())
        })

        viewModel.addServiceLiveData.observe(viewLifecycleOwner, {
            binding.isLoading = false
            if (it == null) {
                Toast.makeText(requireContext(), "Couldn't add the service", Toast.LENGTH_SHORT).show()
            } else {
                binding.isLoading = true
                binding.executePendingBindings()
                viewModel.getAllServices()

                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.isLoading = true
        viewModel.getAllServices()

        parentFragmentManager.setFragmentResultListener(REQ_KEY, this, { key, bundle ->


            when (bundle.getInt("serviceMode")) {
                AddServiceDialogFragment.ALERTMODE.VIEW.ordinal -> view.visibility = View.GONE
                AddServiceDialogFragment.ALERTMODE.ADD.ordinal -> {
                    val serviceRequestModel = AddServiceRequestModel(
                        bundle.get("serviceCode").toString(),
                        bundle.get("serviceName").toString(),
                        bundle.get("serviceDesc").toString(),
                        bundle.get("isActive") as Boolean,
                        1
                    )
                    viewModel.addService(serviceRequestModel)
                }
                AddServiceDialogFragment.ALERTMODE.UPDATE.ordinal -> {
                    val updateServiceRequestModel = UpdateServiceRequestModel(
                        bundle.getInt("serviceId"),
                        bundle.get("serviceCode").toString(),
                        bundle.get("serviceName").toString(),
                        bundle.get("serviceDesc").toString(),
                        bundle.get("isActive") as Boolean,
                        1
                    )
                    viewModel.updateService(updateServiceRequestModel)
                }
                else -> { }

            }

        })
    }


    inner class EventHandler {
        fun addService(view: View) {
            val addServiceFrag = AddServiceDialogFragment()
            val bundle = Bundle()
            bundle.putString("title", "Add Service")
            bundle.putInt("mode", AddServiceDialogFragment.ALERTMODE.ADD.ordinal)
            addServiceFrag.arguments = bundle
            addServiceFrag.show(parentFragmentManager, "addService_tag")
        }
    }

    override fun onItemClick(view: View, position: Int) {
        val serviceVO = binding.adapter?.dataList?.get(position)
        if(binding.userResponseModel?.iD_UserType == 1) {
            val addServiceFrag = AddServiceDialogFragment()
            val bundle = Bundle()
            bundle.putString("title", "Service Detail")
            bundle.putInt("mode", AddServiceDialogFragment.ALERTMODE.VIEW.ordinal)
            bundle.putSerializable("serviceVO", serviceVO)
            addServiceFrag.arguments = bundle
            addServiceFrag.show(parentFragmentManager, "addService_tag")
        }
    }

    override fun onCancelComplaintClicked(position: Int, ticketId: String,view:Button) {
        TODO("Not yet implemented")
    }
}