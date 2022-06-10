package com.netfix.ui.dashboard

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
import com.netfix.databinding.FeedbackBinding
import com.netfix.models.network.dataModel.ServiceVO
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.ui.service.AddServiceDialogFragment

class FeedbackDialog: DialogFragment() {

    lateinit var binding: FeedbackBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FeedbackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.model = FeedbackRequestModel(0,6,3.5f,"",true,6,"")


        binding.submitFeedback.setOnClickListener {
            val bundle = bundleOf(
                Pair("model",binding.model)
            )

            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("FEEDBACK_USER", bundle)
            dismiss()
        }

        binding.cancelFeedback.setOnClickListener {
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
