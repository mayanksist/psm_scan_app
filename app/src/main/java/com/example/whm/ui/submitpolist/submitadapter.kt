package com.example.myapplication.com.example.whm.ui.submitpolist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.whm.ui.inventoryreceive.ReceivePO


internal class submitadapter(private var draftModel: List<submitmodel>, var activity: Context?) :
    RecyclerView.Adapter<submitadapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var BillNo: TextView = view.findViewById(R.id.txtbillno)
        var BillDAte: TextView = view.findViewById(R.id.billdate)
        var txtnoofproduct: TextView = view.findViewById(R.id.product)
        var vendorname: TextView = view.findViewById(R.id.vendorname)
        var CardView : CardView = view.findViewById(R.id.POListDraftRecyclerView)

    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_submitpolist , parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val revertpolist = draftModel[position]
        holder.BillNo.text = revertpolist.getBillNo()
        holder.BillDAte.text = revertpolist.getBill_date()
        holder.vendorname.text = revertpolist.getVendorname()
        holder.txtnoofproduct.text = revertpolist.getnoofproducts().toString()
//        holder.CardView.setOnClickListener(View.OnClickListener { view ->
//            val intent = Intent(activity, ReceivePO::class.java)
//            activity?.startActivity(intent)
//            //view.findNavController().navigate(R.id.nav_bindpolist)
//        })

    }

    override fun getItemCount(): Int {
        return draftModel.size
    }
}
