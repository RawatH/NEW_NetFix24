package com.netfix.ui.complaint

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.ChatCellBinding
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.ui.dashboard.GenericAdapter
import com.netfix.util.AppConst


class ChatAdapter : GenericAdapter<ComplaintVO.ChatHistory>() {

    fun setData(dataList: List<ComplaintVO.ChatHistory>) {
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: ComplaintVO.ChatHistory): Int {
        return R.layout.chat_cell_layout
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val chatCellBinding: ChatCellBinding = ChatCellBinding.bind(view)
        return ChatViewHolder(chatCellBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    class ChatViewHolder(val binding: ChatCellBinding) : RecyclerView.ViewHolder(binding.root), Binder<ComplaintVO.ChatHistory> {
        override fun bind(data: ComplaintVO.ChatHistory) {
            binding.chatHistoryVO = data
            var isAdminMsg = false

            data.messangerName?. let {
                isAdminMsg = it.startsWith(AppConst.CHAT_CLIENT, true)
            }

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.chatCell)

            isAdminMsg?.let {
                var hBias = if(it == true) 0.0f else 1.0f
                val params: ConstraintLayout.LayoutParams = binding.chatCell.getLayoutParams() as ConstraintLayout.LayoutParams
                params.horizontalBias = hBias
                binding.chatCell.setLayoutParams(params)
            }

            var bgDrawable = if(isAdminMsg) R.drawable.rounded_rect_admin else R.drawable.rounded_rect_user

            binding.chatCell.background = ContextCompat.getDrawable(
                binding.root.context,
                bgDrawable
            )

        }
    }
}