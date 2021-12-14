package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.whm.ui.inventoryreceive.ReceivePO

class ReceivePOAdapter1(var ReceiveModelList: List<ReceiveModel>, receivePO: ReceivePO):
    RecyclerView.Adapter<ReceivePOAdapter1.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        var ProducyListPO: CardView = view.findViewById(R.id.ProducyListPO)
        var PID: TextView = view.findViewById(R.id.txtProductId)
        var PEODUCTNAME: TextView = view.findViewById(R.id.txtproductname)
        var UNITYPW: TextView = view.findViewById(R.id.txtunittype)
        var POQTY: TextView = view.findViewById(R.id.txtpoqty)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceivePOAdapter1.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.receivepolist, parent, false)
        return ReceivePOAdapter1.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceivePOAdapter1.MyViewHolder, position: Int) {
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



