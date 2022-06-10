package com.netfix.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.netfix.R
import com.netfix.databinding.AddServiceBinding
import com.netfix.databinding.ServiceBinding
import com.netfix.models.network.dataModel.ServiceVO

class AddServiceDialogFragment : DialogFragment() {

    lateinit var binding: AddServiceBinding

    enum class ALERTMODE {
        VIEW, ADD, UPDATE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title: String? = arguments?.getString("title")
        val mode: Int? = arguments?.getInt("mode")
        val serviceVO = arguments?.getSerializable("serviceVO") as? ServiceVO

        binding.serviceVO = serviceVO
        binding.title = title
        binding.mode = mode //0 VIEW 1 ADD 2 EDIT

        val cancelBtn = view.findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.serviceAction.setOnClickListener {
            val bundle = bundleOf(
                Pair("serviceCode", binding.serviceCode.text),
                Pair("serviceName", binding.serviceName.text),
                Pair("serviceDesc", binding.serviceDesc.text),
                Pair("isActive", binding.isActive.isChecked),
                Pair("serviceMode", binding.mode),
                Pair("serviceId", binding.serviceVO?.serviceId)
            )

            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("REQ_ADD_SERVICE", bundle)
            dismiss()
        }

        binding.editAction.setOnClickListener {
            binding.mode = ALERTMODE.UPDATE.ordinal
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

@BindingAdapter("mode")
fun setText(view: Button, mode: Int) {
    when (mode) {
        AddServiceDialogFragment.ALERTMODE.VIEW.ordinal -> view.visibility = View.GONE
        AddServiceDialogFragment.ALERTMODE.ADD.ordinal -> {
            view.text = "Submit"
            view.visibility = View.VISIBLE
        }
        else -> {
            view.text = "Update"
            view.visibility = View.VISIBLE
        }
    }
}


@BindingAdapter("mode")
fun setText(view: ImageView, mode: Int) {
    when (mode) {
        AddServiceDialogFragment.ALERTMODE.VIEW.ordinal -> view.visibility = View.VISIBLE

        else -> view.visibility = View.GONE

    }
}