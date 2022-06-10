package com.netfix.ui.complaint

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ComplaintDetailBinding
import com.netfix.db.CWDatabase
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.util.AppConst
import kotlinx.coroutines.launch

class ComplaintDetailFragment : BaseFragment() {
    lateinit var binding: ComplaintDetailBinding
    lateinit var viewModel: ComplaintDetailViewModel
    var isAgent = false
    val args: ComplaintDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.complaint_detail_layout,
            container,
            false
        )
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ComplaintDetailViewModel::class.java)

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarComplaintDetail)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            if (!userList.isEmpty()) {
                loggedUser = userList.get(0)
                isAgent = loggedUser != null && loggedUser.iD_UserType == AppConst.USER_TYPE_AGENT
                binding.isAgent = isAgent
                binding.executePendingBindings()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatInputLayout.setEndIconOnClickListener {
            var complaintVO = binding.complaintVO
            complaintVO?.let {
                var chatHistory = it.chatHistory.get(it.chatHistory.size - 1)

                var newChatHistory = ComplaintVO.ChatHistory(
                    null, loggedUser.iD_User, binding.msg.text.toString(),
                    1, null, null, null
                )
                var chatList: MutableList<ComplaintVO.ChatHistory> = mutableListOf(newChatHistory)
                var newComplaintVO = ComplaintVO(
                    null,
                    complaintVO.ticketStatus,
                    complaintVO.complaintOn,
                    complaintVO.ticketID,
                    complaintVO.isActive,
                    complaintVO.subject,
                    chatList,
                    complaintVO.customerName,
                    "",
                    "",
                    "",
                    ""
                )

                binding.msg.isEnabled = false
                binding.msg.text?.clear()
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                binding.isLoading = true
                viewModel.sendMessage(newComplaintVO)
            }

        }

        viewModel.complaintResponseLiveData.observe(viewLifecycleOwner, {
            binding.isLoading = false
            binding.msg.isEnabled = true
            it?.let {
                binding.complaintVO?.chatHistory?.add(it.response.chatHistory.get(0))
                binding.complaintVO?.chatHistory?.size?.let { it1 ->
                    binding.recyclerView.adapter?.notifyItemInserted(it1 - 1)
                    binding.recyclerView.scrollToPosition(it1 - 1)
                }
            }
        })

        viewModel.ticketLiveData.observe(viewLifecycleOwner, {
            binding.isLoading = false

            if (it.code == 200) {
                var complaintVO = it.response.get(0)
                binding.complaintVO = complaintVO
                if (isAgent) {
                    binding.toolbarComplaintDetail.title = with(complaintVO.ticketID) {
                        this?.substring(0, length - 3) + "XXX"
                    }
                } else {
                    binding.toolbarComplaintDetail.title = complaintVO.ticketID
                }
                var adapter = ChatAdapter()
                adapter.setData(complaintVO.chatHistory)
                binding.recyclerView.adapter = adapter
            }
        })
        binding.isLoading = true
        viewModel.getTicketDetails(args.ticketId)
    }
}