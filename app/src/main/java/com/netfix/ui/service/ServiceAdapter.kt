package com.netfix.ui.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.ServiceCellBinding
import com.netfix.models.network.dataModel.ServiceVO

class ServiceAdapter(val dataList:List<ServiceVO>, val itemClickListener: ItemClickListener): RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(val binding: ServiceCellBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val serviceCellBinding:ServiceCellBinding = DataBindingUtil.inflate(layoutInflater, R.layout.service_cell_layout, parent, false)
        return ServiceViewHolder(serviceCellBinding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.binding.serviceVO = dataList.get(position)
        holder.binding.root.setOnClickListener{
         itemClickListener.onItemClick(holder.binding.root,position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }



}