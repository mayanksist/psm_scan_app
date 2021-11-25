package com.example.whm.ui.pickallboxes

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.assignorder.OrderModel
import com.example.myapplication.com.example.whm.ui.load_order_page.LoadOrderModel
import com.example.myapplication.com.example.whm.ui.pickallboxes.AllpickBoxes
import com.example.whm.ui.assignorder.AssignOrderAdapter
import com.example.whm.ui.pickallboxes.placeholder.PlaceholderContent

class picallboxesFragment : Fragment() {

    private val picboxesclass = ArrayList<AllpickBoxes>()
    private lateinit var picboxesall: MypicallboxesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_picallboxes_list2, container, false)

        // Set the adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.allpickboxeslist)
//        val sharedUnloadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        setHasOptionsMenu(true)
        picboxesall = MypicallboxesRecyclerViewAdapter(picboxesclass,this.context)
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = picboxesall
        var Ono = ""
                for (i in 0..10) {
                    Ono ="Ord10211458/"+i.toString()
                    DataBindAllBoxes(Ono)
                }


        return view
    }

    private fun DataBindAllBoxes(Ono: String) {
        var boxes = AllpickBoxes(Ono)
        picboxesclass.add(boxes)
        picboxesall.notifyDataSetChanged()
    }
}

