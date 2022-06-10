package com.netfix.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputEditText
import com.netfix.R
import com.netfix.app.NFApplication
import com.netfix.models.network.request.login.LoginRequestModel
import com.netfix.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = LoginViewModelFactory(activity?.application as NFApplication)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        val view = inflater.inflate(R.layout.login_layout, container, false);

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val overlay = view.findViewById<ConstraintLayout>(R.id.progressOverlay)

        var newConnBtn = view.findViewById<Button>(R.id.newConnection)

        newConnBtn.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_newConnectionFragment)
        }

        var signUpBtn = view.findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        var loginBtn = view.findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val userName = view.findViewById<TextInputEditText>(R.id.userName).text.toString()
            val password = view.findViewById<TextInputEditText>(R.id.password).text.toString()
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    requireContext(),
                    "Username/Password field is empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                overlay.visibility = View.VISIBLE
                viewModel.validateUser(LoginRequestModel(userName, password))

            }
        }

        viewModel.loginReqLiveData.observe(viewLifecycleOwner, {
            overlay.visibility = View.GONE
            if (it != null) {
                if (it.code.equals("200")) {
                    viewModel.persistLoggedUser()
                    activity?.finish()
                    var dashbaordIntent = Intent(requireContext(),DashboardActivity::class.java)
                    startActivity(dashbaordIntent)
//                    NavHostFragment.findNavController(this)
//                        .navigate(R.id.action_loginFragment_to_dashboardFragment)
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Failed to log in.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}