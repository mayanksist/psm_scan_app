package com.example.myapplication.com.example.whm.ui.revertpolist

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
import com.example.whm.ui.inventoryreceive.ReceivePO
import javax.net.ssl.SSLEngineResult


internal class RevertAdapter(private var revertModel: List<RevertModel>, var activity: Context?) :
    RecyclerView.Adapter<RevertAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var BillNo: TextView = view.findViewById(R.id.txtbillno)
        var BillDAte: TextView = view.findViewById(R.id.billdate)
        var txtnoofproduct: TextView = view.findViewById(R.id.product)
        var vendorname: TextView = view.findViewById(R.id.vendorname)
        var CardView : CardView = view.findViewById(R.id.POListRecyclerView)
        var DAutoid:Int=0
        var Status:Int=0
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_revert_polist, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val revertpolist = revertModel[position]
        holder.BillNo.text = revertpolist.getBillNo()
        holder.BillDAte.text = revertpolist.getBill_date()
        holder.vendorname.text = revertpolist.getVendorname()
        holder.txtnoofproduct.text = revertpolist.getnoofproducts().toString()
        holder.DAutoid= revertpolist.getDAutoid()!!
        holder.Status= revertpolist.getStatus()!!.toInt()


        holder.CardView.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(activity, ReceivePO::class.java)
            activity?.startActivity(intent)
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putInt("Status", holder.Status)
            sharedLoadOrderPage.putInt("DAutoid", holder.DAutoid)
            sharedLoadOrderPage.apply()
            //  Toast.makeText(activity, holder.DAutoid.toString(),Toast.LENGTH_SHORT).show()
            //view.findNavController().navigate(R.id.nav_bindpolist)
        })
    }
    override fun getItemCount(): Int {
        if(revertModel.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Revert PO List ("+revertModel.size+")"
        }
        return revertModel.size
    }

}