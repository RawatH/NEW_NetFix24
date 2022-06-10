package com.netfix.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.netfix.databinding.PlanServiceBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.models.network.dataModel.ServiceVO
import com.netfix.ui.service.ItemClickListener

class PlanServiceDialogFragment<T>(val adapter: GenericAdapter<T>) : DialogFragment(),
    ItemClickListener {

    lateinit var binding: PlanServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlanServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = arguments?.getString("title")
        binding.list.adapter = adapter
        if(adapter is GPlanAdapter) {
            (adapter as GPlanAdapter).itemClickListener = this
        }
        binding.close.setOnClickListener {
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

    override fun onItemClick(view: View, position: Int) {
        var o = adapter.dataList.get(position)
        var name:String = ""
        var id: Int = if (o is PlanVO) {
            name = (o as PlanVO).planName
            (o as PlanVO).planId
        } else {
            (o as ServiceVO).serviceId
        }
        var bundle: Bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("name", name)
        setFragmentResult("RESULT",bundle)
        dismiss()
    }

    override fun onCancelComplaintClicked(position: Int, ticketId: String,view:Button) {
        TODO("Not yet implemented")
    }

}