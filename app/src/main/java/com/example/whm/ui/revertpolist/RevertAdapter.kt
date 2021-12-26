package com.example.myapplication.com.example.whm.ui.revertpolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


internal class RevertAdapter(private var revertModel: List<RevertModel>, var activity: Context?) :
    RecyclerView.Adapter<RevertAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_revert_polist, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val revertpolist = revertModel[position]


    }
    override fun getItemCount(): Int {
        return revertModel.size
    }
}