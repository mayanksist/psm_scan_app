package com.example.myapplication.com.example.whm.ui.submitpolist

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.whm.ui.submitpolist.SubmitDetailsItemViewModel



class submitdetailsadapter(var ReceiveModelList: ArrayList<SubmitDetailsItemViewModel>, var activity: Context?):

    RecyclerView.Adapter<submitdetailsadapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){


        var ProducyListPO: CardView = view.findViewById(com.example.myapplication.R.id.ProducyListPO)
        var PID: TextView = view.findViewById(com.example.myapplication.R.id.txtproductidl)
        var PEODUCTNAME: TextView = view.findViewById(com.example.myapplication.R.id.txtproductnamePO)
        var UNITYPW: TextView = view.findViewById(com.example.myapplication.R.id.txtunittype)
        var txttotalpieceqty: TextView = view.findViewById(com.example.myapplication.R.id.txttotalpieceqty)
        var POQTY: TextView = view.findViewById(com.example.myapplication.R.id.txtpo_qty)
        val draftAutoIdTV: TextView = view.findViewById(com.example.myapplication.R.id.podraftAutoId)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): submitdetailsadapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.myapplication.R.layout.submit_details_item_fragment, parent, false)
        return submitdetailsadapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: submitdetailsadapter.MyViewHolder, position: Int) {
        var productList=ReceiveModelList[position]
        // var model = ReceiveModelList.get(position)
        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.txttotalpieceqty.text =productList.getTotalPieces().toString()
        holder.POQTY.text=productList.getPOQTY().toString()
        holder.draftAutoIdTV.text=productList.getDraftID().toString()




    }


    override fun getItemCount(): Int {
        return ReceiveModelList.size
        notifyDataSetChanged()
    }

}