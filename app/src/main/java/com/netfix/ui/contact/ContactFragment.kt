package com.netfix.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.netfix.R
import com.netfix.databinding.AboutBinding
import com.netfix.ui.base.BaseFragment

class ContactFragment :BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:AboutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.about,container,false)
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.feedbackToolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }
}