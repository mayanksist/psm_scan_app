package com.example.myapplication.com.example.whm.ui.submitpolist

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.whm.ui.inventoryreceive.ReceivePO
import com.example.whm.ui.submitpolist.SubmitdetailsActivity


internal class submitadapter(private var draftModel: List<submitmodel>, var activity: Context?) :
    RecyclerView.Adapter<submitadapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var BillNo: TextView = view.findViewById(R.id.txtbillno)
        var BillDAte: TextView = view.findViewById(R.id.billdate)
        var txtnoofproduct: TextView = view.findViewById(R.id.product)
        var vendorname: TextView = view.findViewById(R.id.vendorname)
        var CardView : CardView = view.findViewById(R.id.POListSubmitRecyclerView)

        var DAutoid:Int=0
        val StatusSub:Int=2
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
        holder.DAutoid= revertpolist.getDAutoid()!!


        holder.CardView.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(activity, SubmitdetailsActivity::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putInt("DAutoid", holder.DAutoid)
            sharedLoadOrderPage.apply()

        })

    }

    override fun getItemCount(): Int {
        return draftModel.size
    }
}
