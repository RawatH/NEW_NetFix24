package com.netfix.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.netfix.R
import com.netfix.db.CWDatabase
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private  val TAG = "SplashFragment"
    lateinit var navController: NavController

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        navController = NavHostFragment.findNavController(this)

        viewLifecycleOwner.lifecycleScope.launch {
            val db = CWDatabase.getDatabase(requireContext())
            var userList = db.userLoginResponseDao().getLoggedUser()
            Log.d(TAG, "onStart: Logged User -> "+userList)
            Handler(Looper.getMainLooper()).postDelayed({
                if(userList.isEmpty()) {
                   navController
                        .navigate(R.id.action_splashFragment_to_loginFragment)
                }else{
                    navController
                        .navigate(R.id.action_splashFragment_to_dashboardActivity)
                }
            },1200)

        }

    }

}