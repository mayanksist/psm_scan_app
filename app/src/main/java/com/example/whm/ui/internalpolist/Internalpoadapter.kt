package com.example.myapplication.com.example.whm.ui.internalpolist

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.draftpolist.draftpomodel
import com.example.whm.ui.internalpolist.InternalpoListViewModel
import com.example.whm.ui.interpodetails.Internalpodetails
import com.example.whm.ui.inventoryreceive.ReceivePO



internal class Internalpoadapter(private var internalpoListViewModel: List<InternalpoListViewModel>, var activity: Context?) :
    RecyclerView.Adapter<Internalpoadapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var BillNo: TextView = view.findViewById(R.id.txtbillno)
        var BillDAte: TextView = view.findViewById(R.id.billdate)
        var txtnoofproduct: TextView = view.findViewById(R.id.product)
        var vendorname: TextView = view.findViewById(R.id.vendorname)
        var CardView : CardView = view.findViewById(R.id.InternalPOListView)
        var DAutoid:Int=0
        var Status:Int=0
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.internalpo_list_fragment , parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val revertpolist = internalpoListViewModel[position]
        holder.BillNo.text = revertpolist.getBillNo()
        holder.BillDAte.text = revertpolist.getBill_date()
        holder.vendorname.text = revertpolist.getVendorname()
        holder.txtnoofproduct.text = revertpolist.getnoofproducts().toString()
        holder.DAutoid= revertpolist.getDAutoid()!!
        holder.Status= revertpolist.getStatus()!!.toInt()
        holder.CardView.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(activity, Internalpodetails::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putInt("DAutoid", holder.DAutoid)
            sharedLoadOrderPage.putInt("Status", holder.Status)
            sharedLoadOrderPage.putString("Bill_No", holder.BillNo.text.toString())
            sharedLoadOrderPage.putString("Bill_Date", holder.BillDAte.text.toString())
            sharedLoadOrderPage.putString("VENDORName", holder.txtnoofproduct.text.toString())
            sharedLoadOrderPage.putInt("Status", holder.Status)
            sharedLoadOrderPage.apply()

        })

    }

    override fun getItemCount(): Int {
        if(internalpoListViewModel.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Internal Pending  PO List ("+internalpoListViewModel.size+")"
        }
        return internalpoListViewModel.size
    }
}