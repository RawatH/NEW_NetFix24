package com.netfix.ui.complaint

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.AllComplaintsBinding
import com.netfix.db.CWDatabase
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.complaint.AssignComplaintModel
import com.netfix.models.network.request.complaint.NewComplaintReqModel
import com.netfix.models.network.response.login.UserLoginResponseModel
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.ui.service.ItemClickListener
import com.netfix.util.AppConst
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ComplaintFragment : BaseFragment(), ItemClickListener {

    lateinit var viewModel: ComplaintViewModel
    lateinit var binding: AllComplaintsBinding
    private val REQ_KEY = "REQ_NEW_COMPLAINT"
    var isCustomer: Boolean = false;
    var isAgent: Boolean = false;
    var isAdmin: Boolean = false;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComplaintViewModel::class.java)

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.compliant_dashboard_layout,
            container,
            false
        )

        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarComplaints)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            loggedUser = userList.get(0)
            isAdmin = loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_ADMIN

            isAgent = loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_AGENT
            binding.isAdmin =
                loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_ADMIN
            binding.executePendingBindings()
            Log.d("COMPLAITNFRAG", "LOGGED USER = " + binding.isAdmin)
            binding.raiseComplaintActionButton.visibility =
                if (loggedUser.iD_UserType == AppConst.USER_TYPE_CUSTOMER ||
                    loggedUser.iD_UserType == AppConst.USER_TYPE_ADMIN)
                    View.VISIBLE else View.GONE

            isCustomer = loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_CUSTOMER
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.getAgents()

        viewModel.cancelTicketResponse.observe(viewLifecycleOwner, {
            Log.d("asd", "asdad")

            Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            loadAllComplaints()
        })

        viewModel.agentResponse.observe(viewLifecycleOwner, {
            if (it != null) {

                val agentNameList = mutableListOf<String>()
                val adapter = viewModel.agentResponse.value?.let { it1 ->
                    for (item in it1.response) {
                        agentNameList.add("" + item.loginID + " - " + item.userName)
                    }
                    ArrayAdapter(
                        requireContext(),
                        R.layout.agent_row_layout,
                        agentNameList
                    )
                }
                binding.agentDropDown.setAdapter(adapter)
            }
        })

        viewModel.newComplaintResponseLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                loadAllComplaints()
            }
        })

        viewModel.allComplaintsLiveData.observe(viewLifecycleOwner, {
            binding.isLoading = false
            if (it.code == 200) {
                val adapter = AllComplaintsAdapter(this)
                adapter.setIsAgent(isAgent)
                adapter.setIsCustomer(isCustomer)
                adapter.setData(it.response)
                binding.complaintList.adapter = adapter
            }
        })

        binding.raiseComplaintActionButton.setOnClickListener {
            val addComplaintDialogFragment = AddComplaintDialogFragment(isAdmin)
            addComplaintDialogFragment.show(parentFragmentManager, "raiseComplaint_tag")
        }

        parentFragmentManager.setFragmentResultListener(REQ_KEY, this, { key, bundle ->


            Log.d("","======${bundle.get("userId").toString()}")

            val db = CWDatabase.getDatabase(requireContext())
            GlobalScope.launch {
                var chatHistory:ComplaintVO.ChatHistory

                if(isAdmin){
                    var userId = bundle.getInt("userId")
                    chatHistory = ComplaintVO.ChatHistory(
                        null, userId, bundle.get("desc").toString(),
                        3, null, null, null
                    )
                }else {
                    var userModel: List<UserLoginResponseModel> =
                        db.userLoginResponseDao().getLoggedUser()
                    var user = userModel.get(0)
                    chatHistory = ComplaintVO.ChatHistory(
                        null, user.iD_User, bundle.get("desc").toString(),
                        3, null, null, null
                    )
                }
                var chatList: MutableList<ComplaintVO.ChatHistory> = mutableListOf(chatHistory)
                var complaintVO = ComplaintVO(
                    null,
                    null,
                    null,
                    null,
                    true,
                    bundle.get("subject").toString(),
                    chatList,
                    "",
                    "",
                    "",
                    "",
                    ""
                )


                viewModel.raiseComplaint(complaintVO)

            }

        })

        loadAllComplaints()

        viewModel.isComplaintSelectionOn.observe(viewLifecycleOwner, {
            (binding.complaintList.adapter as AllComplaintsAdapter).onAgentSelectionToggled(it)
        })

        binding.assignComplaint.setOnClickListener {
            var agentName = binding.agentDropDown.text
            var complaintIdList =
                (binding.complaintList.adapter as AllComplaintsAdapter).getSelectedAgent()

            if (viewModel.selectedAgentId == 0) {
                Toast.makeText(requireContext(), "Please select agent", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (complaintIdList.size == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please select complaints to assign",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            var complaintId = ""

            for ((index, compaintId) in complaintIdList.withIndex()) {
                if (index == complaintIdList.size - 1) {
                    complaintId += compaintId
                } else {
                    complaintId += compaintId + ","
                }

            }
            Log.d("", "--" + complaintId)
            var loginReqModel = AssignComplaintModel(viewModel.selectedAgentId, complaintId)
            viewModel.assignToAgent(loginReqModel)

            binding.progressOverlay.visibility = View.VISIBLE
        }

        viewModel.assignComplaintResponse.observe(viewLifecycleOwner, {
            it.let {
                binding.progressOverlay.visibility = View.GONE
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                binding.executePendingBindings()
                binding.adapter?.clearComplaintSelection()
            }

            onOptionsItemSelected(assignMenuItem)
        })

        binding.agentDropDown.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.selectedAgentId =
                    viewModel.agentResponse.value?.response?.get(p2)?.iD_User!!
            }
        })

    }

    fun loadAllComplaints() {
        binding.isLoading = true
        viewModel.getAllComplaints()
    }


    override fun onItemClick(view: View, position: Int) {
        val adapter: AllComplaintsAdapter = binding.complaintList.adapter as AllComplaintsAdapter
        val ticketId = adapter.dataList.get(position).ticketID
        val action = ticketId?.let {
            ComplaintFragmentDirections.actionComplaintFragmentToComplaintDetailFragment()
                .setTicketId(it)
        }
        action?.let { NavHostFragment.findNavController(this).navigate(action) }
    }

    override fun onCancelComplaintClicked(position: Int, ticketId: String ,view: Button) {
//        view.isEnabled = false
        if (isAgent) {
           val view =  LayoutInflater.from(requireContext())
                .inflate(R.layout.ticketid_input_layout, null, false)

            MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .setTitle("Cancel Ticket")
                .setMessage("Please enter ticket id.")
                .setPositiveButton("Proceed") { dialog, _ ->
                    val ticketIdView = view.findViewById<TextInputEditText>(R.id.ticketId)
                    val inputTicketId = ticketIdView.text.toString()
                    if(inputTicketId.isBlank()){
                        view.isEnabled = true

                        Toast.makeText(requireContext(),"Invalid ticket id",Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    viewModel.cancelTicket(position, inputTicketId)
                    dialog.dismiss()
                }
                .setNegativeButton("Dismiss") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            viewModel.cancelTicket(position, ticketId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            loggedUser = userList.get(0)
            var isAdmin = loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_ADMIN
            if (isAdmin) {
                inflater.inflate(R.menu.complaint_menu, menu)
            }
        }
    }

    lateinit var assignMenuItem: MenuItem

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        assignMenuItem = item
        if (item.itemId == R.id.menu_select_complaint) {
            viewModel.isComplaintSelectionOn.value =
                if (viewModel.isComplaintSelectionOn.value == true)
                    false
                else
                    true

            item.icon = if (viewModel.isComplaintSelectionOn.value == true)
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_close_24)
            else
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_select_complaint)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}