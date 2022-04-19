package com.example.whm.ui.submitpolist

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.submitpolist.submitadapter
import com.example.myapplication.com.example.whm.ui.submitpolist.submitmodel
import org.json.JSONObject
import java.io.IOException


class submitpolistFragment : Fragment() {
    private val submitmodel = ArrayList<submitmodel>()
    private lateinit var submitadapter: submitadapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_submit_polis_list, container, false)

        if (AppPreferences.internetConnectionCheck(this.context)) {
            val recyclerView: RecyclerView = view.findViewById(R.id.load_order)
            submitadapter = submitadapter(submitmodel, this.context)
            val layoutManager = LinearLayoutManager(this.context)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = submitadapter
            val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()

            val Jsonarrapolist = JSONObject()
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            var empautoid = preferences.getString("EmpAutoId", "")
            var accessToken = preferences.getString("accessToken", "")
            var StatusD = 2
            val sharedLoadOrderPage = preferences.edit()
            //  Toast.makeText(this.context,StatusD.toString(),Toast.LENGTH_LONG).show()
            val queues = Volley.newRequestQueue(this.context)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(
                context?.contentResolver, Settings.Secure.ANDROID_ID)))
            JSONObj.put("cObj", Jsonarrapolist.put("status", StatusD))
            val draftpolist = JsonObjectRequest(
                Request.Method.POST, AppPreferences.DRAFT_PO_LIST,
                JSONObj,
                { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj.toString())
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val resmsg = resultobj.getString("responseMessage")
                    val rescode = resultobj.getString("responseCode")
                    if (rescode == "201") {
                        submitmodel.clear()
                        submitadapter.notifyDataSetChanged()
                        val jsondata = resultobj.getJSONArray("responseData")
                        for (i in 0 until jsondata.length()) {
                            val BillNo = jsondata.getJSONObject(i).getString("BillNo")
                            val Billdate = jsondata.getJSONObject(i).getString("BillDate")
                            val VendorName = jsondata.getJSONObject(i).getString("VAutoId")
                           // val VendorName = jsondata.getJSONObject(i).getString("VendorName")
                            val Status = jsondata.getJSONObject(i).getInt("Status")
                           // val StatusType = jsondata.getJSONObject(i).getInt("StatusType")
                            val NoofProduct = jsondata.getJSONObject(i).getInt("NoOfI")
                            val DAutoId = jsondata.getJSONObject(i).getInt("DAutoId")
                            DataBindLoadorder(
                                BillNo, Billdate, VendorName,Status,NoofProduct,DAutoId)

                        }
                        sharedLoadOrderPage.remove("StatusD")
                        if (pDialog != null) {
                            if (pDialog.isShowing) {
                                pDialog.dismiss()
                            }
                        }
                    } else {
                        val alerts = AlertDialog.Builder(this.context)
                        alerts.setMessage(resmsg.toString())
                        alerts.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alerts.create()
                        dialog.show()
                        if (pDialog != null) {
                            if (pDialog.isShowing) {
                                pDialog.dismiss()
                            }
                        }
                    }
                },
                { response ->
                    Log.e("onError", error(response.toString()))
                    if (pDialog != null) {
                        if (pDialog.isShowing) {
                            pDialog.dismiss()
                        }
                    }
                })
            draftpolist.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queues.add(draftpolist)
            } catch (e: IOException) {
                Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
            }
        } else {
            val dialog = context?.let { Dialog(it) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
            val btDismiss =
                dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
            btDismiss?.setOnClickListener {
                dialog.dismiss()
                this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
            }
            dialog?.show()
        }
        return view
    }

    private fun DataBindLoadorder(
        BillNo: String,
        Billdate: String,
        VendorName: String,
        Status: Int,
        NoofProduct: Int,
        DAutoId: Int
    ) {
        var RevertPoList = submitmodel(BillNo, Billdate, VendorName, Status, NoofProduct,DAutoId)
        submitmodel.add(RevertPoList)
        submitadapter.notifyDataSetChanged()
    }
}