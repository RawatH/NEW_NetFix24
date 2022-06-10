package com.netfix.ui.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ConnectionListBinding
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory

class ConnectionFragment :  BaseFragment() {

    lateinit var binding: ConnectionListBinding
    lateinit var viewModel: ConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ConnectionViewModel::class.java)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.connection_list_layout,container,false)

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.connectionToolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.connectionReqLiveData.observe(viewLifecycleOwner,{
            binding.isLoading = false
            val  adapter = ConnectionAdapter()
            adapter.setData(it.response)
            binding.adapter = adapter

        })
        binding.isLoading = true
        viewModel.getAllConnectionRequests()

    }
}