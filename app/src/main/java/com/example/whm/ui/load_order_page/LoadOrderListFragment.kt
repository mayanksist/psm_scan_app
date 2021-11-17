package com.example.whm.ui.load_order_page

import android.app.AlertDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.load_order_page.LoadOrderAdapter
import com.example.myapplication.com.example.whm.ui.load_order_page.LoadOrderModel
import org.json.JSONObject
import java.io.IOException




class LoadOrderListFragment : Fragment() {
    private val LoadorderList = ArrayList<LoadOrderModel>()
    private lateinit var LoadorderAdapter: LoadOrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.example.myapplication.R.layout.fragment_loadorderlists,
            container,
            false
        )
        val recyclerView: RecyclerView = view.findViewById(R.id.orderlist)
        LoadorderAdapter = LoadOrderAdapter(LoadorderList)
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = LoadorderAdapter
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
            Request.Method.POST, AppPreferences.BASEURL + AppPreferences.GET_ASSIGN_ORDER_LIST, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val rescode = resultobj.getString("responseCode")
                if (rescode == "201") {
                    val jsondata = resultobj.getJSONArray("responseData")
                    for (i in 0 until jsondata.length()) {
                        val OrderNo = jsondata.getJSONObject(i).getString("ONo")
                        val Orderdate = jsondata.getJSONObject(i).getString("PB")
                        val Stoppage = jsondata.getJSONObject(i).getString("ST")
                        DataBindLoadorder(
                            OrderNo,
                            Orderdate,
                            Stoppage
                        )
                    }
                } else {
                    val alerts = AlertDialog.Builder(this.context)
                    alerts.setMessage("Order List not found")
                    alerts.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alerts.create()
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

    private fun DataBindLoadorder(
       Ono: String, PackedBoxes: String,
        Stoppage: String
    ) {

        var Loadorder = LoadOrderModel(Ono, PackedBoxes, Stoppage)
        LoadorderList.add(Loadorder)
        LoadorderAdapter.notifyDataSetChanged()
    }
}