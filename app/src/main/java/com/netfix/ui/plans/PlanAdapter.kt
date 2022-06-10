package com.netfix.ui.plans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.PlanCellBinding
import com.netfix.databinding.ServiceCellBinding
import com.netfix.models.network.dataModel.PlanVO
import com.netfix.ui.service.ItemClickListener

class PlanAdapter(val dataList: List<PlanVO>, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    var planOption:Int = 0
    val optionAList: MutableList<PlanVO> = mutableListOf()
    val optionBList: MutableList<PlanVO> = mutableListOf()

    init {
        if(!dataList.isEmpty()){
            optionAList.clear()
            optionBList.clear()
            for( o in  dataList){
                if(o.planDuration.contentEquals("1 Month")){
                    optionBList .add(o)
                }else{
                    optionAList .add(o)
                }
            }
        }
    }


    class PlanViewHolder(val binding: PlanCellBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val planCellBinding: PlanCellBinding = DataBindingUtil.inflate(layoutInflater, R.layout.plan_cell_layout, parent, false)
        return PlanViewHolder(planCellBinding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.binding.planVO = if(planOption == 0) optionAList.get(position) else optionBList.get(position)
        holder.binding.root.setOnClickListener {
            itemClickListener.onItemClick(holder.binding.root, position)
        }
    }

    override fun getItemCount(): Int {
        return if(planOption == 0) optionAList.size else optionBList.size
    }

    fun filter(planOption: Int) {
       this.planOption = planOption
        notifyDataSetChanged()
    }
}
