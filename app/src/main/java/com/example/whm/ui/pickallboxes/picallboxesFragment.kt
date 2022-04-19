package com.example.whm.ui.pickallboxes

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
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
import com.example.myapplication.com.example.whm.ui.pickallboxes.AllpickBoxes
import com.example.myapplication.ui.product.ProductList
import org.json.JSONObject
import java.io.IOException

class picallboxesFragment : Fragment() {

    private val picboxesclass = ArrayList<AllpickBoxes>()
    private lateinit var picboxesall: MypicallboxesRecyclerViewAdapter
    var listarray: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_picallboxes_list2, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.allpickboxeslist)
        var PickAllBtn: Button = view.findViewById(R.id.SelectAllPickBtn)
        var  BtnBack :Button =  view.findViewById(R.id.BtnBack)
        val sharedLoadOrderPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        BtnBack.setOnClickListener(View.OnClickListener {view->
            val sharedLoadOrderPage = sharedLoadOrderPreferences.edit()
            sharedLoadOrderPage.putInt("ClickNo",1)
            sharedLoadOrderPage.apply()
//            this.findNavController()
//                .navigate(R.id.nav_scanorder)
            val activity = requireView().context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.Fragment_pickAllboxes,ProductList()).addToBackStack(null).commit()
        })
        setHasOptionsMenu(true)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar
                ?.setDisplayHomeAsUpEnabled(false)
        }
        picboxesall = MypicallboxesRecyclerViewAdapter(picboxesclass, this.context)
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = picboxesall

        var OrderNO = sharedLoadOrderPreferences.getString("OrderNo", "").toString()
        var Stoppage = sharedLoadOrderPreferences.getString("Stoppage", "").toString()
        var EnabledPickallBoxes = sharedLoadOrderPreferences.getBoolean("EnabledPickallBoxes", false)
        var PackedBoxes = sharedLoadOrderPreferences.getInt("PackedBoxes", 0)
        var ArraysL = sharedLoadOrderPreferences.getString("boxlist", "").toString()
        listarray = ArraysL.replace("[", "").replace("]", "").split(",") as MutableList<String>
        val constraintLayoutManager: ConstraintLayout = view.findViewById(R.id.button)
        constraintLayoutManager.visibility = View.VISIBLE
        if (EnabledPickallBoxes == false) {
            PickAllBtn.visibility = View.GONE
        }
        var Ono = ""
        try {
            for (i in 1..PackedBoxes) {
                var flage = 0
                for (j in listarray) {
                    if (j != "") {
                        if (j.trim().toInt() == i) {
                            flage = 1
                            break
                        }
                    }
                }
                if (flage == 0) {
                    Ono = "$OrderNO/$i"
                    DataBindAllBoxes(Ono)
                }
            }
        } catch (e: IOException) {

        }
        PickAllBtn.setOnClickListener(View.OnClickListener { view ->
            val pDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Fetching ..."
            pDialog.setCancelable(false)
            pDialog.show()
            val Jsonarra = JSONObject()
            val JSONObj = JSONObject()
            val appversion = AppPreferences.AppVersion
            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            var empautoid = preferences.getString("EmpAutoId", "")
            var accessToken = preferences.getString("accessToken", "")
            val queues = Volley.newRequestQueue(activity)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
            JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("requestContainer", Jsonarra.put("deviceID",
                Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
            JSONObj.put("OrderNo", OrderNO)
            val alertsuborder = AlertDialog.Builder(activity)
            val resordernos = JsonObjectRequest(
                Request.Method.POST,
                AppPreferences.BASEURL + AppPreferences.SUBMIT_LOAD_ORDER,
                JSONObj,
                { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj.toString())
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val rspCode = resultobj.getString("responseCode")
                    val rspMsg = resultobj.getString("responseMessage")

                    if (rspCode.toString() == "200") {
                        AppPreferences.playSound()
                        pDialog.dismiss()
                        alertsuborder.setTitle(OrderNO)
                        alertsuborder.setMessage(rspMsg.toString())
                        alertsuborder.setPositiveButton(
                            "ok",
                            DialogInterface.OnClickListener { dialog, which ->

                                this.findNavController()
                                    .navigate(com.example.myapplication.R.id.nav_orderlist)
                            })

                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                    } else {
                        pDialog.dismiss()
                        alertsuborder.setTitle(OrderNO)
                        alertsuborder.setMessage(rspMsg)
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                    }

                },
                { response ->
                    Log.e("onError", error(response.toString()))
                })
            resordernos.retryPolicy = DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queues.add(resordernos)
            } catch (e: IOException) {
                Toast.makeText(activity, "Server Error", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }
    private fun DataBindAllBoxes(Ono: String) {
        var boxes = AllpickBoxes(Ono)
        picboxesclass.add(boxes)
        picboxesall.notifyDataSetChanged()
    }

}

