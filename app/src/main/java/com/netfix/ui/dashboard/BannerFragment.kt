package com.netfix.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.netfix.R
import com.netfix.databinding.BannerBinding
import com.netfix.models.network.dataModel.BannerVO
import com.netfix.ui.base.BaseFragment
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class BannerFragment : BaseFragment() {
    lateinit var binding: BannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bannerVO:BannerVO = arguments?.getSerializable("bannerVO") as BannerVO
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.banner_layout,container,false)

        binding.bannerVO = bannerVO
        return binding.root
    }

    companion object {
        @BindingAdapter("loadBanner")
        @JvmStatic
        fun loadImage(view: ImageView, bannerVO: BannerVO) {
            Picasso.get().load(bannerVO.filePath)
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(view);
        }
    }


}