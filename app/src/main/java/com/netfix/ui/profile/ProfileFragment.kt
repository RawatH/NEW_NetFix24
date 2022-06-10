package com.netfix.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.databinding.ProfileBinding
import com.netfix.databinding.ServiceBinding
import com.netfix.db.CWDatabase
import com.netfix.ui.base.BaseFragment
import com.netfix.ui.login.LoginViewModelFactory
import com.netfix.ui.service.ServiceViewModel
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.profile_layout, container, false)
        binding.inEditMode = false
        binding.lifecycleOwner = this

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.toolbarProfile)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelProfileEdit.setOnClickListener(){
            binding.inEditMode = false;
            requireActivity().invalidateOptionsMenu()
        }

        binding.updateProfile.setOnClickListener(){
            binding.inEditMode = false;
            requireActivity().invalidateOptionsMenu()
            viewModel.updateProfile()
        }

        viewModel.profileLiveData.observe(viewLifecycleOwner, {
            binding.model = it
            binding.executePendingBindings()
        })

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            viewModel.getProfile(userList.get(0).iD_User)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu,menu);
        val menuEdit = menu.findItem(R.id.menu_editProfile)

        val showEdit = binding.inEditMode as Boolean
        menuEdit.setVisible(!showEdit)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_editProfile){
            binding.inEditMode = true
            activity?.invalidateOptionsMenu()
        }
        return super.onOptionsItemSelected(item)
    }


}