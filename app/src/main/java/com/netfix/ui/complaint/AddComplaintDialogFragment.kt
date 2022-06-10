package com.netfix.ui.complaint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import com.netfix.R
import com.netfix.databinding.AddComplaintBinding
import com.netfix.models.network.request.UserReqModel
import com.netfix.models.network.response.ResponseModel
import com.netfix.network.NFServiceClient
import com.netfix.ui.customer.CustomerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddComplaintDialogFragment(val isAdmin:Boolean = false) : DialogFragment() {

    lateinit var binding: AddComplaintBinding
    var userResponseModel : MutableLiveData<ResponseModel<List<UserReqModel>>> = MutableLiveData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddComplaintBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(isAdmin){
            val call: Call<ResponseModel<List<UserReqModel>>> = NFServiceClient.getClient().getAllUsers(Any())

            call.enqueue(object : Callback<ResponseModel<List<UserReqModel>>> {
                override fun onResponse(call: Call<ResponseModel<List<UserReqModel>>>, response: Response<ResponseModel<List<UserReqModel>>>) {
                    userResponseModel.postValue(response.body())

                    val userList = mutableListOf<String>()

                    response.body()?.response?.forEach {
                        userList.add( "UserID : ${it.iD_User}  \nLoginId : ${it.loginID} \nUsername : ${it.userName}\n")
                    }

                    binding.selectUser.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            userList
                        )
                    )
                }

                override fun onFailure(call: Call<ResponseModel<List<UserReqModel>>>, t: Throwable) {
                    userResponseModel.postValue(null)
                }

            })
        }else{
            binding.ilUserList.visibility = View.GONE
        }


        val items = listOf("No internet connection",
            "Internet speed is slow",
            "WiFi signal disconnects frequently",
            "Other")
        binding.complaintSubject.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                items
            )
        )

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.submitComplaint.setOnClickListener {

            Log.d("","----->${binding.selectUser.text}")


            if(binding.complaintSubject.text.isBlank()){
                Toast.makeText(requireContext(),"Please select subject",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(binding.complaintDescription.text?.isBlank() == true){
                Toast.makeText(requireContext(),"Please write description",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = bundleOf(
                Pair("subject", binding.complaintSubject.text),
                Pair("desc", binding.complaintDescription.text),

            )

            if(isAdmin){
                val userStr = binding.selectUser.text

                val matchIndex = userStr.indexOf(":")
                val matchIndex1 = userStr.indexOf("LoginId")

                val userId = userStr.subSequence(matchIndex+1, matchIndex1).toString().trim().toInt()

                bundle.putInt("userId", userId)
            }



            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("REQ_NEW_COMPLAINT", bundle)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.getWindow()?.setLayout(width, height)
        }
    }

}
