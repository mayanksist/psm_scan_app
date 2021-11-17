package com.example.whm.ui.assignorder

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.assignorder.OrderModel
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private val orderList = ArrayList<OrderModel>()
    private lateinit var orderAdapter: AssignOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.example.myapplication.R.layout.fragment_item_list,
            container,
            false
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        val cardOrderList = view.findViewById<CardView>(R.id.OrderListRecyclerView)
        val cardError = view.findViewById<CardView>(R.id.Error)
        orderAdapter = AssignOrderAdapter(orderList)
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = orderAdapter

        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        var accessToken = preferences.getString("accessToken", "")
        val queues = Volley.newRequestQueue(this.context)

        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))

        val resorderno = JsonObjectRequest(
            Request.Method.POST, AppPreferences.BASEURL + AppPreferences.GET_ASSIGN_ORDER, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val rescode = resultobj.getString("responseCode")
                if (rescode == "201") {
                    val jsondata = resultobj.getJSONArray("responseData")
                        for (i in 0 until jsondata.length()) {
                            val CustomerName = jsondata.getJSONObject(i).getString("CM")
                            val OrderNo = jsondata.getJSONObject(i).getString("ONo")
                            val Orderdate = jsondata.getJSONObject(i).getString("Od")
                            val Salesperson = jsondata.getJSONObject(i).getString("SP")
                            val PackedBoxes = jsondata.getJSONObject(i).getString("PB")
                            val Stoppage = jsondata.getJSONObject(i).getString("Stop")
                            val payableamount = jsondata.getJSONObject(i).getString("AMT")
                            val ST = jsondata.getJSONObject(i).getString("ST")
                            prepareMovieData(
                                CustomerName,
                                OrderNo,
                                Orderdate,
                                Salesperson,
                                PackedBoxes,
                                Stoppage,
                                payableamount
                            )
                    }
                } else {
                    val alertscanord = AlertDialog.Builder(this.context)
                    alertscanord.setMessage("Order not found")
                    alertscanord.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alertscanord.create()
                    dialog.show()
                }
            }, { response ->
                Log.e("onError", error(response.toString()))
            })
        try {
            queues.add(resorderno)
        } catch (e: IOException) {
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }

        return view
    }


    private fun prepareMovieData(
        CustomerName: String, Ono: String, Od: String, SP: String, PackedBoxes: String,
        Stoppage: String, PayableAmount: String
    ) {

        var order = OrderModel(CustomerName, Ono, Od, SP, PackedBoxes, Stoppage, PayableAmount)
        orderList.add(order)
        orderAdapter.notifyDataSetChanged()
    }
}