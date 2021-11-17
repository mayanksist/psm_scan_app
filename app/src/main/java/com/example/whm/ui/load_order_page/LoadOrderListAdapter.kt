package com.example.myapplication.com.example.whm.ui.load_order_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

internal class LoadOrderListAdapter(private var loadorderList: List<LoadOrderModel>) :
    RecyclerView.Adapter<LoadOrderListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var OrderNo: TextView = view.findViewById(R.id.txtOrderNoL)
        var PackedBoxes: TextView = view.findViewById(R.id.txtboxes)
        var StopNo: TextView = view.findViewById(R.id.txtstopno)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_load_order_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = loadorderList[position]
        holder.OrderNo.text = order.getOno()
        holder.PackedBoxes.text = order.getPackedBoxes().toString()
        holder.StopNo.text = order.getStoppage()

    }
    override fun getItemCount(): Int {
        return loadorderList.size
    }
}