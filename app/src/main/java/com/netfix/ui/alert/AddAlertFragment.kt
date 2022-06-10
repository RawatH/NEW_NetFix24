package com.netfix.ui.alert


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.AddAlertBinding
import com.netfix.models.network.dataModel.AlertDataVO
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddAlertFragment : BaseFragment() {

    private lateinit var binding: AddAlertBinding
    private lateinit var viewModel: AddAlertViewModel
    private val REQ_KEY = "SELECT_ALERT"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddAlertViewModel::class.java)

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.add_alert_layout,
            container,
            false
        )

        binding.alertOption = 0;
        binding.lifecycleOwner = viewLifecycleOwner
        binding.alertVO = viewModel.alert

        var tabA = binding.tabLayout.newTab();
        tabA.text = "User"

        var tabB = binding.tabLayout.newTab();
        tabB.text = "Location"
        binding.tabLayout.addTab(tabA)
        binding.tabLayout.addTab(tabB)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarAddAlert)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.actionButton.setOnClickListener {
            showUserLocationSelectionPopup();
        }

        binding.submitButton.setOnClickListener {

            if(!viewModel.alert.isValid()){
                Toast.makeText(requireContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.isLoading = true
            viewModel.sendAlertToServer()
        }

        binding.startTime.setEndIconOnClickListener {
            pickTime("Start Time")
        }
        binding.endTime.setEndIconOnClickListener {
            pickTime("End Time")
        }

        viewModel.alertResponseLiveData.observe(viewLifecycleOwner, {
            binding.isLoading = false
            it?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

    private fun showUserLocationSelectionPopup() {
        val popupDataList = mutableListOf<AlertDataVO>()

        if (binding.tabLayout.selectedTabPosition == 0){
            viewModel.userResponseModel.value?.response?.let {
                for (userVo in it) {
                    var alertDataVO = AlertDataVO(userVo.iD_User, userVo.loginID, false)
                    popupDataList.add(alertDataVO)
                }
            }

        }else {
            viewModel.locationResponseModel.value?.response?.let {
                for (location in it) {
                    var alertDataVO = AlertDataVO(location.iD_AddressArea, location.name, false)
                    popupDataList.add(alertDataVO)
                }
            }
        }


        val alertSelectionDialogFragment = AlertSelectionDialogFragment()
        val bundle = Bundle()
        bundle.putSerializable("dataList", popupDataList as Serializable)

        bundle.putInt(
            "mode",
            if (binding.tabLayout.selectedTabPosition == 0)
                AlertSelectionDialogFragment.ALERT_SELECTION_MODE.USER_SELECTION.ordinal
            else
                AlertSelectionDialogFragment.ALERT_SELECTION_MODE.LOCATION_SELECTION.ordinal
        )

        alertSelectionDialogFragment.arguments = bundle
        alertSelectionDialogFragment.show(parentFragmentManager, REQ_KEY)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.isLoading = true

        loadUserOrLocations();

        parentFragmentManager.setFragmentResultListener(REQ_KEY, this, { key, bundle ->

            val selectedList = bundle.getSerializable("selectedList") as List<AlertDataVO>

            if(selectedList.isEmpty()){
                Toast.makeText(requireContext(),"Please select user/location.",Toast.LENGTH_SHORT).show()
                return@setFragmentResultListener
            }

//            viewModel.selectedList.clear();
            viewModel.selectedList.addAll(selectedList)

            selectedList.forEach {
                addUserLocationSelection(it)
            }

        })

        viewModel.userResponseModel.observe(viewLifecycleOwner, {
            binding.isLoading = false
        })

        viewModel.locationResponseModel.observe(viewLifecycleOwner, {
            binding.isLoading = false
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.alertOption = tab.position

//                    var fabText = if(tab.position == 0 ) "Add User" else "Add Location"
//                    binding.actionButton.setText(fabText)
                    viewModel.alert.forUser = tab.position == 0
                    loadUserOrLocations()

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun loadUserOrLocations() {
        viewModel.alert.AppliedIDList = ""
        viewModel.selectedList.clear()
        binding.chipGroup.removeAllViews()
        binding.isLoading = true
        if (binding.alertOption == 0) {
            viewModel.fetchAllUsers();
        } else {
            viewModel.fetchAllLocations();
        }
    }

    private fun addUserLocationSelection(dataVO: AlertDataVO) {
        val chip = Chip(requireContext())
        chip.setText(dataVO.name)
        chip.setChipBackgroundColorResource(R.color.purple_500)
        chip.setCloseIconVisible(true)
        chip.setTextColor(resources.getColor(R.color.white))
        chip.setOnCloseIconClickListener {
            // Responds to chip's close icon click if one is present
            binding.chipGroup.removeView(chip)
            viewModel.selectedList.remove(dataVO)
        }

        binding.chipGroup.addView(chip)
    }


    private fun pickTime(title: String) {
        SingleDateAndTimePickerDialog.Builder(context)
            //.bottomSheet()
            //.curved()
            //.stepSizeMinutes(15)
            //.displayHours(false)
            //.displayMinutes(false)
            //.todayText("aujourd'hui")
            .mainColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
            .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                override fun onDisplayed(picker: SingleDateAndTimePicker) {
                    // Retrieve the SingleDateAndTimePicker
                }

                fun onClosed(picker: SingleDateAndTimePicker?) {
                    // On dialog closed
                }
            })
            .title(title)
            .listener {
                val simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm")
                val result = Date(it.time)
                if (title.equals("Start Time")) {
                    viewModel.alert.startDateTime = it.time.toString()
                } else {
                    viewModel.alert.endDateTime = it.time.toString()
                }
                binding.alertVO = viewModel.alert
//                Toast.makeText(requireContext(), "Time - " + it.time, Toast.LENGTH_SHORT).show()
            }.display()


    }

    companion object {

        @BindingAdapter("time")
        @JvmStatic
        fun setTime(view: EditText, value: String?) {
            if (!TextUtils.isEmpty(value)) {
                val simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm")
                val result = value?.let { Date(it.toLong()) }
                view.setText(simple.format(result))
            }
        }
    }


}