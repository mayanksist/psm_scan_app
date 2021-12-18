package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.content.Context


class ReceivePOAdapter1(var ReceiveModelList: List<ReceiveModel>,var activity: Context?):
    RecyclerView.Adapter<ReceivePOAdapter1.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        var ProducyListPO: CardView = view.findViewById(com.example.myapplication.R.id.ProducyListPO)
        var PID: TextView = view.findViewById(com.example.myapplication.R.id.txtproductidl)
        var PEODUCTNAME: TextView = view.findViewById(com.example.myapplication.R.id.txtproductnamePO)
        var UNITYPW: TextView = view.findViewById(com.example.myapplication.R.id.txtunittype)
        var txttotalpieceqty: TextView = view.findViewById(com.example.myapplication.R.id.txttotalpieceqty)
        var POQTY: TextView = view.findViewById(com.example.myapplication.R.id.txtpo_qty)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceivePOAdapter1.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.myapplication.R.layout.receivepolist, parent, false)
        return ReceivePOAdapter1.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceivePOAdapter1.MyViewHolder, position: Int) {
        var productList=ReceiveModelList[position]
            holder.PID.text = productList.getPID().toString()
            holder.PEODUCTNAME.text = productList.getPNAME()
            holder.UNITYPW.text = productList.getUnitType().toString()
            holder.txttotalpieceqty.text =productList.getTotalPieces().toString()
            holder.POQTY.text=productList.getPOQTY().toString()

    }


    override fun getItemCount(): Int {
        return ReceiveModelList.size
    }
}



