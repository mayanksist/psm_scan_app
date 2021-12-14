package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

abstract class ReceivePOAdapter(private var ReceiveModelList: List<ReceiveModel>) :
    RecyclerView.Adapter<com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ProducyListPO: CardView = view.findViewById(R.id.ProducyListPO)
        var PID: TextView = view.findViewById(R.id.txtProductId)
        var PEODUCTNAME: TextView = view.findViewById(R.id.txtproductname)
        var UNITYPW: TextView = view.findViewById(R.id.txtunittype)
        var POQTY: TextView = view.findViewById(R.id.txtpoqty)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.receivepolist, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var productList=ReceiveModelList[position]
        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.POQTY.text = productList.getPOQTY().toString()
    }


    override fun getItemCount(): Int {
        return ReceiveModelList.size
    }

}

