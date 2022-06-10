package com.netfix.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.netfix.R
import com.netfix.db.CWDatabase
import com.netfix.ui.main.RootActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity() , DashboardFragmentsListener{

    lateinit var bottomNavigationView:BottomNavigationView
    lateinit var navHostFragment:NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity_layout)
        bottomNavigationView =  findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.dash_nav_host_fragment)
                as NavHostFragment

        NavigationUI.setupWithNavController(
            bottomNavigationView,
            navHostFragment.navController
        )

        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId)
            {
                R.id.navLogout -> showLogoutAlert()
                else -> NavigationUI.onNavDestinationSelected(it, navHostFragment.navController)
            }

            true
        }
    }


    private fun showLogoutAlert(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Do you want to logout?")

            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, which ->
                performLogout()
            }
            .show()
    }
    private fun performLogout(){
        val db = CWDatabase.getDatabase(getApplication())
        GlobalScope.launch {
            db.userLoginResponseDao().deleteAll()
            var dashbaordIntent = Intent(this@DashboardActivity, RootActivity::class.java)
            startActivity(dashbaordIntent)
            finish()
        }

    }

    override fun performBottomNavigationAction(id: Int) {
        val view: View = bottomNavigationView.findViewById(id)
        view.performClick()
    }


}