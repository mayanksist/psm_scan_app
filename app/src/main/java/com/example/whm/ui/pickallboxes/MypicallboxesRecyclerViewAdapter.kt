package com.example.whm.ui.pickallboxes


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.pickallboxes.AllpickBoxes
import com.example.myapplication.ui.product.ProductList
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

var listarray:MutableList<String> = ArrayList()
internal class MypicallboxesRecyclerViewAdapter(private var allpicboxes: List<AllpickBoxes>, var activity: Context?) :
    RecyclerView.Adapter<MypicallboxesRecyclerViewAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var OrderNo: RadioButton = view.findViewById(R.id.txtLeftBox)
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
        holder.OrderNo.setOnClickListener(View.OnClickListener {view ->
            val getorder = order.getOno()?.split("/")?.toMutableList()
            var boxNo = getorder?.get(1)
            sharedLoadOrderPage.putString("boxNo", boxNo.toString().trim())
            sharedLoadOrderPage.putString("SelectOrderNo", order.getOno())
//            sharedLoadOrderPage.putInt("ClickNo",2)
            sharedLoadOrderPage.apply()
            val activity = view!!.context as AppCompatActivity
            val MyFragment  = ProductList()
            activity.supportFragmentManager.beginTransaction().replace(R.id.Fragment_pickAllboxes,MyFragment).addToBackStack(null).commit()
        })

    }

    override fun getItemCount(): Int {
        return allpicboxes.size
    }
}