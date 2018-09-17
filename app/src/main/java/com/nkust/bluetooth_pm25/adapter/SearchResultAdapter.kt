package com.nkust.bluetooth_pm25.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inuker.bluetooth.library.search.SearchResult
import com.nkust.bluetooth_pm25.R
import kotlinx.android.synthetic.main.list_device_data.view.*

class SearchResultAdapter(
        private val contactList: List<SearchResult>,
        private val listener: SearchResultAdapter.OnItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class TYPE {
        ADJUSTABLE,
        SWITCHES
    }

    interface OnItemListener {
        fun onItemClick(item: SearchResult, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_device_data, parent, false), listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (holder is SearchResultAdapter.ViewHolder) {
            holder.bindView(contactList[position])
            holder.itemView.setOnClickListener {
                listener.onItemClick(contactList[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ViewHolder(itemView: View, private val listener: OnItemListener) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(roomData: SearchResult) {
            itemView.textTitle.text = roomData.name
            itemView.textData.text = roomData.address
        }
    }
}
