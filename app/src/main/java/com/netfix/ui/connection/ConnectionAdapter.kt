package com.netfix.ui.connection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.ConnectionCellBinding
import com.netfix.models.network.dataModel.ConnectionVO
import com.netfix.ui.dashboard.GenericAdapter

class ConnectionAdapter: GenericAdapter<ConnectionVO>() {

    fun setData(dataList: List<ConnectionVO>) {
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: ConnectionVO): Int {
        return R.layout.connection_cell_layout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as ConnectionViewHolder
        viewHolder.binding.connectionVO = dataList.get(position)

    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val connectionCellBinding: ConnectionCellBinding = ConnectionCellBinding.bind(view)
        return ConnectionViewHolder(connectionCellBinding)
    }

    class ConnectionViewHolder(val binding: ConnectionCellBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<ConnectionVO> {
        override fun bind(data: ConnectionVO) {
            binding.connectionVO = data
        }
    }

}