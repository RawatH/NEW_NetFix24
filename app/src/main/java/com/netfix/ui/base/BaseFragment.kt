package com.netfix.ui.base

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.netfix.R
import com.netfix.db.CWDatabase
import com.netfix.models.network.response.login.UserLoginResponseModel
import com.netfix.util.AppConst
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {
    lateinit var loggedUser:UserLoginResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            if(!userList.isEmpty()) {
                loggedUser = userList.get(0)

                Log.d("BaseFragment", "LOGGED USER = " + loggedUser)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> requireActivity()!!.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}