package com.netfix.ui.alert

import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.AlertCellBinding
import com.netfix.models.network.dataModel.AlertDataVO
import com.netfix.ui.dashboard.GenericAdapter

class UserLocationAdapter : GenericAdapter<AlertDataVO>() {

    var orignalList = mutableListOf<AlertDataVO>()
    var filteredList = mutableListOf<AlertDataVO>()
    val selectedList = mutableListOf<AlertDataVO>()

    fun setData(dataList: List<AlertDataVO>) {
        if (orignalList.size == 0) {
            orignalList.addAll(dataList)
        }
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: AlertDataVO): Int {
        return R.layout.alert_cell_layout;
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val alertCellBinding: AlertCellBinding = AlertCellBinding.bind(view)
        return AlertCellViewHolder(alertCellBinding)
    }

    inner class AlertCellViewHolder(val binding: AlertCellBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<AlertDataVO> ,CompoundButton.OnCheckedChangeListener{
        override fun bind(data: AlertDataVO) {
            binding.model = data
            binding.checkBox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(compoundButton:CompoundButton, isChecked: Boolean) {
            binding.model?.let {
                if(isChecked){
                    selectedList.add(it)
                }else{
                    selectedList.remove(it)
                }
            }

        }
    }

    fun filter(filter: String) {
        val newFilteredList = dataList.filter { it.name.contains(filter, true) }
        filteredList.clear()
        filteredList.addAll(newFilteredList)
        this.dataList = filteredList
        notifyDataSetChanged()
    }


    fun selectAll(selectAll:Boolean){
        for(item in dataList){
            item.isSelected = selectAll
        }

        selectedList.clear()
        if(selectAll){
            selectedList.addAll(dataList)
        }
        notifyDataSetChanged()
    }



}