package com.netfix.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.netfix.R
import com.netfix.databinding.AddPlanBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.models.network.dataModel.ServiceVO
import com.netfix.ui.service.AddServiceDialogFragment

class AddPlanDialogFragment : DialogFragment() {

    enum class ALERTMODE {
        VIEW, ADD, UPDATE
    }

    lateinit var binding: AddPlanBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddPlanBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title: String? = arguments?.getString("title")
        val mode: Int? = arguments?.getInt("mode")
        val planVO = arguments?.getSerializable("planVO") as? PlanVO

        binding.planVO = planVO
        binding.title = title
        binding.mode = mode //0 VIEW 1 ADD 2 EDIT

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.planAction.setOnClickListener {
            val bundle = bundleOf(
                Pair("planCode", binding.planCode.text),
                Pair("planName", binding.planName.text),
                Pair("planDesc", binding.planDesc.text),
                Pair("isActive", binding.isActive.isChecked),
                Pair("serviceMode", binding.mode),
                Pair("planId", binding.planVO?.planId)
            )

            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("REQ_ADD_SERVICE", bundle)
            dismiss()
        }

        binding.editAction.setOnClickListener {
            binding.mode = AddServiceDialogFragment.ALERTMODE.UPDATE.ordinal
            binding.title = "Update Detail"
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