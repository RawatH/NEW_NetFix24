package com.netfix.ui.customer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.netfix.R
import com.netfix.databinding.CustomerRowBinding
import com.netfix.models.network.request.UserReqModel
import com.netfix.ui.dashboard.GenericAdapter

class CustomerAdapter : GenericAdapter<UserReqModel>() {

    var isSearchOn:Boolean =false
    init{
        setItems(mutableListOf<UserReqModel>())
    }

    var orignalList = mutableListOf<UserReqModel>()
    var filteredList = mutableListOf<UserReqModel>()
    val selectedList = mutableListOf<UserReqModel>()

    fun setData(dataList: List<UserReqModel>) {
        filteredList.clear()
        orignalList.clear()
        orignalList.addAll(dataList)
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: UserReqModel): Int {
        return R.layout.customer_row_layout;
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val customerRowBinding: CustomerRowBinding = CustomerRowBinding.bind(view)
        return CustomerViewHolder(customerRowBinding)
    }

    inner class CustomerViewHolder(val binding: CustomerRowBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<UserReqModel> {
        override fun bind(data: UserReqModel) {
            binding.userModel = data
        }

    }

    fun filter(filter: String) {
        val newFilteredList = orignalList.filter { it.loginID.contains(filter, true) }
        filteredList.clear()
        filteredList.addAll(newFilteredList)
        this.dataList = filteredList
        notifyDataSetChanged()
    }

    fun resetSearch(){
        setData(orignalList)
        notifyDataSetChanged()
    }
}