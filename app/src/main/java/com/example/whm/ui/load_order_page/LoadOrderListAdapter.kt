package com.example.myapplication.com.example.whm.ui.load_order_page

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.product.setSupportActionBar


internal class LoadOrderListAdapter(private var loadorderList: List<LoadOrderModel>,var activity: Context?) :
    RecyclerView.Adapter<LoadOrderListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var OrderNo: TextView = view.findViewById(R.id.txtOrderNoL)
        var PackedBoxes: TextView = view.findViewById(R.id.txtboxes)
        var StopNo: TextView = view.findViewById(R.id.txtstopno)
        var CardView :  CardView = view.findViewById(R.id.OrderListRecyclerView)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

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
        var count:String = "Load Orders ["+loadorderList.size+"]"
        holder.CardView.setOnClickListener(View.OnClickListener {view ->
            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putString("OrderNo", order.getOno())
            sharedLoadOrderPage.putString("PackedBoxes", order.getPackedBoxes().toString())
            sharedLoadOrderPage.putString("Stoppage", order.getStoppage().toString())
            sharedLoadOrderPage.putInt("PageValue", 2)
            sharedLoadOrderPage.apply()
            view.findNavController().navigate(R.id.nav_productlist)
        })
        (activity as? AppCompatActivity)?.setSupportActionBar(holder.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(count)
    }
    override fun getItemCount(): Int {
        return loadorderList.size
    }
}