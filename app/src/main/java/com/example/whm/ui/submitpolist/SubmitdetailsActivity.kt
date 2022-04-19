package com.example.whm.ui.submitpolist

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.home.HomeFragment
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1
import com.example.myapplication.com.example.whm.ui.submitpolist.submitdetailsadapter
import com.example.whm.ui.inventoryreceive.ReceivePO
import org.json.JSONObject

class SubmitdetailsActivity : AppCompatActivity() {
    var toolbar: Toolbar?=null
    var  DAutoid:Int=0
    var ReceiverpoList: ArrayList<SubmitDetailsItemViewModel> = arrayListOf()
    var backarrow: ImageView?=null
    private lateinit var ReceivePOAdapterl: submitdetailsadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitdetails)
        val preferencesid = PreferenceManager.getDefaultSharedPreferences(this@SubmitdetailsActivity)
        DAutoid = preferencesid.getInt("DAutoid", 0)
        backarrow = findViewById(com.example.myapplication.R.id.imgbackbtm)

        if (AppPreferences.internetConnectionCheck(this)) {
            if (DAutoid != null && DAutoid != 0) {
                val recyclerView: RecyclerView =
                    findViewById(com.example.myapplication.R.id.POLIST)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                Draftproductlist()
                ReceivePOAdapterl = submitdetailsadapter(ReceiverpoList, this)
                recyclerView.adapter = ReceivePOAdapterl
            }

        }
        else{
            CheckInterNetDailog()
        }

        if (AppPreferences.internetConnectionCheck(this)) {
            backarrow?.setOnClickListener {
                val intent = Intent(this@SubmitdetailsActivity, MainActivity2::class.java)
                startActivity(intent)
//                val intent = Intent(applicationContext, HomeFragment::class.java)
//                startActivity(intent)
            }
        } else {
            CheckInterNetDailog()
        }

    }
    fun Draftproductlist() {
        val txtbillno: TextView = findViewById(com.example.myapplication.R.id.txtbillno)
        val txtbilldatepo: TextView = findViewById(com.example.myapplication.R.id.txtbilldatepo)
        val vendornamepo: TextView = findViewById(com.example.myapplication.R.id.vendornamepo)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)))
        var accessToken = preferences.getString("accessToken", "")

        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("userAutoId", EmpAutoId)
        )

        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))

        val DRAFTADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.Draft_PRODUCT_List, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                var PID:Int=0
                var UnitAutoId:Int=0
                var TotalPieces:Int=0
                var Quantity:Int=0
                var QtyPerUnit:Int=0
                var PName: String? =null
                var UnitType: String? =null
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepdu = JSONObject(jsondata.toString())
                    val VendorAutoId = jsonrepdu.getInt("VendorAutoId")
                    val BillNo = jsonrepdu.getString("BillNo")
                    val draftAutoId = jsonrepdu.getInt("DAutoId")
                    val BillDate = jsonrepdu.getString("BillDate")
                    val Remarks = jsonrepdu.getString("Remarks")
                    val VendorName = jsonrepdu.getString("VendorName")
                    val Status = jsonrepdu.getInt("Status")
                    val POItems = jsonrepdu.getJSONArray("POItems")
                    txtbillno.text = BillNo
                    txtbilldatepo.text = BillDate
                    vendornamepo.text = VendorName
                    // Toast.makeText(this,POItems.toString(),Toast.LENGTH_SHORT).show()
                    for (i in 0 until POItems.length()) {
                        PName = POItems.getJSONObject(i).getString("PName")
                        PID = POItems.getJSONObject(i).getInt("PID")
                        UnitAutoId = POItems.getJSONObject(i).getInt("UnitAutoId")
                        UnitType = POItems.getJSONObject(i).getString("UnitType")
                        TotalPieces = POItems.getJSONObject(i).getInt("TotalPieces")
                        Quantity = POItems.getJSONObject(i).getInt("Quantity")
                        QtyPerUnit = POItems.getJSONObject(i).getInt("QtyPerUnit")

                        var check=false
                        var poreqqty:Int=0
                        for (n in 0..ReceiverpoList.size-1) {
                            if(ReceiverpoList[n].getPID()==PID){
                                check=true
                                if (ReceiverpoList[n].getPOQTY() != null) {
                                    poreqqty = ReceiverpoList[n].getPOQTY()!! + 1
                                    ReceivePOAdapterl.notifyItemChanged(n)
                                    ReceiverpoList.removeAt(n)
                                    if (UnitType != null) {
                                        if (PName != null) {
                                            DataBindPOLIST(
                                                PID,
                                                PName,
                                                UnitType,
                                                QtyPerUnit,
                                                Quantity,
                                                draftAutoId
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if(!check) {
                            if (UnitType != null) {
                                if (PName != null) {
                                    DataBindPOLIST(
                                        PID,
                                        PName,
                                        UnitType,
                                        TotalPieces,
                                        Quantity,
                                        draftAutoId
                                    )
                                }
                            }
                        }
                        if(ReceiverpoList.size!=0) {
                            toolbar = findViewById(R.id.toolbarAction)
                            setSupportActionBar(toolbar)
                            supportActionBar?.title = "Pending PO ("+ReceiverpoList.size+")"
                        }
                    }
                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()

//                  /  Toast.makeText(this, , Toast.LENGTH_SHORT).show()

                }

            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        DRAFTADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(DRAFTADDPRODUCT)
    }
    private fun DataBindPOLIST(PID: Int, PNAME: String,
                               UNITTYPE: String,
                               UnitQTY:Int,
                               POQTY:Int,
                               DRaftID: Int) {
        var POLIST = SubmitDetailsItemViewModel(PID, PNAME, UNITTYPE,UnitQTY, POQTY,UnitQTY, DRaftID)
        ReceiverpoList.add(0,POLIST)
        ReceivePOAdapterl.notifyDataSetChanged()


    }
    fun CheckInterNetDailog(){
        val dialog = this.let { Dialog(it) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            val mServiceIntent = Intent(this, HomeFragment::class.java)
            startActivity(mServiceIntent)
//            var intent = Intent(this, HomeFragment::class.java)
//            startActivity(intent)
            }
        dialog.show()
    }
}