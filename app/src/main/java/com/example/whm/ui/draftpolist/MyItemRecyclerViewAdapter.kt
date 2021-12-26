package com.example.whm.ui.draftpolist

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.draftpolist.DraftModel



internal class MyItemRecyclerViewAdapter(private var Draftpolist: List<DraftModel>, var activity: Context?) :
    RecyclerView.Adapter<MyItemRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_draft_p_o_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val polist = Draftpolist[position]


    }
    override fun getItemCount(): Int {
        return Draftpolist.size
    }
}
