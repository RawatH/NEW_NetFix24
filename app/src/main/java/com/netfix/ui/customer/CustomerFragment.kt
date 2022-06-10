package com.netfix.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.CustomerBinding
import com.netfix.models.network.request.UserReqModel
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory

class CustomerFragment : BaseFragment(){
    private lateinit var viewModel: CustomerViewModel
    private lateinit var binding: CustomerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CustomerViewModel::class.java)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.customer_layout, container, false)

        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarCustomer)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.adapter = CustomerAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addCustomer.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_customerFragment_to_signUpFragment2)
        }

        viewModel.userResponseModel.observe(viewLifecycleOwner, {
            binding.isLoading = false
            it?.let {
                binding.adapter?.setData(it.response)
            }

        })

        binding.isLoading = true
        viewModel.fetchAllUsers();


        binding.searchView.setOnCloseListener (object:SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.adapter?.resetSearch()
                Toast.makeText(requireContext(),"close",Toast.LENGTH_SHORT).show()
                return true
            }

        })


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    binding.adapter?.filter(newText)
                }else{
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show();
                }
                return true
            }

        })

    }
}