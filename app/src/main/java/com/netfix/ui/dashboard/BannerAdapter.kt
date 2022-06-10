package com.netfix.ui.dashboard

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.netfix.models.network.dataModel.BannerVO
import com.netfix.ui.main.RootActivity
import com.squareup.picasso.Picasso

class BannerAdapter(fa:Fragment,var datalist:List<BannerVO>?) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return datalist?.size?:0
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = BannerFragment()
        val bundle = Bundle()
        bundle.putSerializable("bannerVO", datalist?.get(position))
        fragment.arguments = bundle
        return fragment
    }
}