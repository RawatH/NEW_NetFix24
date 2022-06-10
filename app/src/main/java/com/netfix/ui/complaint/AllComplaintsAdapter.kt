package com.netfix.ui.complaint

import android.view.View
import android.widget.CompoundButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.netfix.R
import com.netfix.databinding.ComplaintCellBinding
import com.netfix.models.network.dataModel.ComplaintVO
import com.netfix.ui.dashboard.GenericAdapter
import com.netfix.ui.service.ItemClickListener

class AllComplaintsAdapter(var listener: ItemClickListener) : GenericAdapter<ComplaintVO>() {

    var isCustomer: Boolean = false
    var isAgent: Boolean = false
    var isSelectionOn: Boolean = false
    var complaintIdSet = mutableSetOf<String>()

    fun setIsCustomer(isCustomer: Boolean) {
        this.isCustomer = isCustomer
    }

    fun setIsAgent(isAgent: Boolean) {
        this.isAgent = isAgent
    }

    fun setData(dataList: List<ComplaintVO>) {
        setItems(dataList)
    }

    override fun getLayoutId(position: Int, obj: ComplaintVO): Int {
        return R.layout.complaint_cell_layout
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val complaintViewHolder = holder as AllComplaintViewHolder
        complaintViewHolder.binding.isAgent = isAgent
        complaintViewHolder.binding.isCustomer = isCustomer
        complaintViewHolder.binding.isSelectionOn = isSelectionOn

        if (!isSelectionOn) {
            complaintViewHolder.binding.complaintCheckBox.isChecked = false
        }

        complaintViewHolder.binding.closeTicket.setOnClickListener {
            complaintViewHolder.binding.complaintVO?.ticketID?.let { ticketId ->
                listener.onCancelComplaintClicked(position, ticketId,complaintViewHolder.binding.closeTicket)
            }
        }

        complaintViewHolder.binding.root.setOnClickListener {
            listener.onItemClick(
                complaintViewHolder.binding.root,
                position
            )
        }

        complaintViewHolder.binding.complaintCheckBox.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    complaintViewHolder.binding.complaintVO?.ticketID?.let { complaintIdSet.add(it) }
                } else {
                    complaintViewHolder.binding.complaintVO?.ticketID?.let {
                        complaintIdSet.remove(it)
                    }
                }
            }
        })
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        val planCellBinding: ComplaintCellBinding = ComplaintCellBinding.bind(view)
        return AllComplaintViewHolder(planCellBinding)
    }

    class AllComplaintViewHolder(val binding: ComplaintCellBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<ComplaintVO> {
        override fun bind(data: ComplaintVO) {
            binding.complaintVO = data
        }
    }

    fun onAgentSelectionToggled(selectionOn: Boolean) {
        this.isSelectionOn = selectionOn
        notifyDataSetChanged()
    }

    @JvmName("getSelectedAgent1")
    fun getSelectedAgent(): List<String> {
        return complaintIdSet.toList()
    }

    fun clearComplaintSelection() {
        TODO("Not yet implemented")
        complaintIdSet.clear()
        onAgentSelectionToggled(false)
    }


    companion object {

        @JvmStatic
        @BindingAdapter("ticketId", "isAgent")
        fun setTicketId(view: Chip, ticketId: String, isAgent: Boolean) {
            if (isAgent) {
                view.text = with(ticketId) {
                    this.substring(0, length - 3) + "XXX"
                }
            } else {
                view.text = ticketId
            }
        }
    }


}