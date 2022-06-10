package com.netfix.ui.dashboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.PlanCellBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.ui.service.ItemClickListener

class GPlanAdapter<PlanVO>() : GenericAdapter<PlanVO>() {
    lateinit var itemClickListener: ItemClickListener

    fun setData(dataList: List<PlanVO>) {
        setItems(dataList)
    }


    override fun getLayoutId(position: Int, obj: PlanVO): Int {
        return R.layout.plan_cell_layout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val gPlanViewHolder = holder as GPlanViewHolder
        gPlanViewHolder.binding.root.setOnClickListener {
            itemClickListener.onItemClick(
                gPlanViewHolder.binding.root,
                position
            )
        }
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val planCellBinding: PlanCellBinding = PlanCellBinding.bind(view)
        return GPlanViewHolder(planCellBinding)
    }

    class GPlanViewHolder(val binding: PlanCellBinding) : RecyclerView.ViewHolder(binding.root), Binder<PlanVO> {
        override fun bind(data: PlanVO) {
            binding.planVO = data
        }
    }


}