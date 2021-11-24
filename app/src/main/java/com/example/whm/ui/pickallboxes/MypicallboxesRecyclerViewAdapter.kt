package com.example.whm.ui.pickallboxes


import android.content.Context
import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.pickallboxes.AllpickBoxes


internal class MypicallboxesRecyclerViewAdapter(private var allpicboxes: List<AllpickBoxes>, var activity: Context?) :
    RecyclerView.Adapter<MypicallboxesRecyclerViewAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var OrderNo: TextView = view.findViewById(R.id.txtallpickbox)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_picallboxes, parent, false)
        return MyViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val order = allpicboxes[position]
        holder.OrderNo.text = order.getOno()
        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
//        sharedLoadOrderPage.putString("OrderNo", order.getOno())


    }
    override fun getItemCount(): Int {
        return allpicboxes.size
    }
}