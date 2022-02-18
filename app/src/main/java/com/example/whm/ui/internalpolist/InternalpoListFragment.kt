package com.example.whm.ui.internalpolist

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
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
import com.example.myapplication.com.example.whm.ui.internalpolist.Internalpoadapter
import com.example.myapplication.ui.product.setSupportActionBar
import org.json.JSONObject
import java.io.IOException


class InternalpoListFragment : Fragment() {

    private val InternalModel = ArrayList<InternalpoListViewModel>()
    private lateinit var InternalAdapter: Internalpoadapter
    var searcpo: MenuItem? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.internalpo_polist_lists, container, false)


        if (AppPreferences.internetConnectionCheck(this.context)) {
            val recyclerView: RecyclerView = view.findViewById(R.id.internalpo_list)
            InternalAdapter = Internalpoadapter(InternalModel, this.context)
            val layoutManager = LinearLayoutManager(this.context)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = InternalAdapter
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
            var StatusPO = 11

            val queues = Volley.newRequestQueue(this.context)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
            JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
            JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(getContext()?.getContentResolver(), Settings.Secure.ANDROID_ID)))
            JSONObj.put("cObj", Jsonarrapolist.put("Status", StatusPO))
            val internalpolist = JsonObjectRequest(
                Request.Method.POST, AppPreferences.InternalPOLIST,
                JSONObj,
                { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj.toString())
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val resmsg = resultobj.getString("responseMessage")
                    val rescode = resultobj.getString("responseCode")
                    if (rescode == "201") {
                        InternalModel.clear()
                        InternalAdapter.notifyDataSetChanged()
                        val jsondata = resultobj.getJSONArray("responseData")
                        for (i in 0 until jsondata.length()) {
                            val PONO = jsondata.getJSONObject(i).getString("PoNo")
                            val PODATE = jsondata.getJSONObject(i).getString("PODate")
                            val VendorName = jsondata.getJSONObject(i).getString("VendorName")
                            val POAUTOID = jsondata.getJSONObject(i).getInt("AutoId")
                            val POStatus = jsondata.getJSONObject(i).getString("POStatus")
                            val NoofProduct = jsondata.getJSONObject(i).getInt("NoofItems")
                            DataBindInternalPOLIST(
                                PONO, PODATE, VendorName,POStatus,NoofProduct,POAUTOID)

                        }

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
            internalpolist.retryPolicy = DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            try {
                queues.add(internalpolist)
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
        return  view
    }

    override fun onCreateOptionsMenu(x: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ipomenuitem, x)
        super.onCreateOptionsMenu(x, inflater)
        searcpo= x.findItem(R.id.searchpo)


        if(searcpo!=null) {
            searcpo?.isVisible = false
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchpo -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun DataBindInternalPOLIST(
        PONO: String,
        PODATE: String,
        VendorName: String,
        Status: String,
        NoofProduct: Int,
        DAutoId: Int
    ) {
        var RevertPoList = InternalpoListViewModel(PONO, PODATE, VendorName, Status, NoofProduct, DAutoId)
        InternalModel.add(RevertPoList)
        InternalAdapter.notifyDataSetChanged()
    }

}




