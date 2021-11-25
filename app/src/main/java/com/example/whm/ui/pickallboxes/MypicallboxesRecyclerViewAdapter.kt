package com.example.whm.ui.pickallboxes


import android.content.Context
import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.pickallboxes.AllpickBoxes


internal class MypicallboxesRecyclerViewAdapter(private var allpicboxes: List<AllpickBoxes>, var activity: Context?) :
    RecyclerView.Adapter<MypicallboxesRecyclerViewAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var OrderNo: RadioButton = view.findViewById(R.id.txtLeftBox)
//        var Cardview: CardView = view.findViewById(R.id.picallboxesRecyclerView)
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
//        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//        holder.CardView.setOnClickListener(View.OnClickListener {view ->
//            val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
//            sharedLoadOrderPage.putString("OrderNo", order.getOno())
//            sharedLoadOrderPage.apply()
//            view.findNavController().navigate(R.id.nav_productlist)
//        })


    }
    override fun getItemCount(): Int {
        return allpicboxes.size
    }
}