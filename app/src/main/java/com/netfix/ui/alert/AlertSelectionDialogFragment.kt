package com.netfix.ui.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.netfix.databinding.AlertSelectionBinding
import com.netfix.models.network.dataModel.AlertDataVO
import java.io.Serializable


class AlertSelectionDialogFragment : DialogFragment() {

    enum class ALERT_SELECTION_MODE {
        USER_SELECTION , LOCATION_SELECTION
    }

    lateinit var binding: AlertSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlertSelectionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode: Int? = arguments?.getInt("mode")
        val dataList:List<AlertDataVO> = arguments?.getSerializable("dataList") as List<AlertDataVO>

        val adapter = UserLocationAdapter()
        adapter.setData(dataList)

        binding.adapter =  adapter

        binding.mode = mode

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.doneButton.setOnClickListener {

            val bundle = Bundle()
            bundle.putSerializable("selectedList",binding.adapter?.selectedList as Serializable)

//            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("SELECT_ALERT", bundle)
            dismiss()
        }

        binding.selectAll.setOnClickListener {
            var label = binding.selectAll.text
            val adapter = binding.adapter as UserLocationAdapter
            adapter.selectAll(if (label == "Select All")  true else false)
            binding.selectAll.text = if (label == "Select All")  "Deselect All" else "Select All"
        }
        attachSearchListener()
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.getWindow()?.setLayout(width, height)
        }
    }

    fun attachSearchListener(){
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    binding.adapter?.filter(newText)
                }else{
                    Toast.makeText(requireContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
               return true
            }

        })
    }
}