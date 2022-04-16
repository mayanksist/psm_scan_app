package com.example.myapplication.com.example.whm.ui.internalpolist

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.*
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
        var BillNo: TextView = view.findViewById(R.id.txtpoNO)
        var BillDAte: TextView = view.findViewById(R.id.txtpodate)
        var txtnoofproduct: TextView = view.findViewById(R.id.product)
        var vendorname: TextView = view.findViewById(R.id.vendorname)
        var CardView : CardView = view.findViewById(R.id.InternalPOListView)
        var POAutoid:Int=0
        var searchpo: MenuItem? = null
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    }



    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.internalpo_list_fragment , parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val POtpolist = internalpoListViewModel[position]
        holder.BillNo.text = POtpolist.getBillNo()
        holder.BillDAte.text = POtpolist.getBill_date()
        holder.vendorname.text = POtpolist.getVendorname()
        holder.txtnoofproduct.text = POtpolist.getnoofproducts().toString()
        holder.POAutoid= POtpolist.getPOAutoid()!!
        holder.CardView.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(activity, Internalpodetails::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putInt("POAutoid", holder.POAutoid)
            sharedLoadOrderPage.apply()
        })
    }

    override fun getItemCount(): Int {
        if(internalpoListViewModel.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Internal PO List ("+internalpoListViewModel.size+")"
        }
        return internalpoListViewModel.size
    }
}