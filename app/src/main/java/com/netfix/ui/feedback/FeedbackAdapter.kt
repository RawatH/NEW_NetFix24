package com.netfix.ui.feedback

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.ComplaintCellBinding
import com.netfix.databinding.FeedbackCellBinding
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.models.network.request.feedback.FeedbackRequestModel
import com.netfix.ui.complaint.AllComplaintsAdapter
import com.netfix.ui.dashboard.GenericAdapter

class FeedbackAdapter() : GenericAdapter<FeedbackRequestModel>() {

    fun setData(dataList: List<FeedbackRequestModel>) {
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: FeedbackRequestModel): Int {
        return R.layout.feedback_cell_layout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as FeedbackViewHolder
        viewHolder.binding.feedbackVO = dataList.get(position)
        viewHolder.binding.feedbackRating.numStars =5

    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val feedbackCellBinding: FeedbackCellBinding = FeedbackCellBinding.bind(view)
        return FeedbackViewHolder(feedbackCellBinding)
    }

    class FeedbackViewHolder(val binding: FeedbackCellBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<FeedbackRequestModel> {
        override fun bind(data: FeedbackRequestModel) {
            binding.feedbackVO = data
        }
    }

}