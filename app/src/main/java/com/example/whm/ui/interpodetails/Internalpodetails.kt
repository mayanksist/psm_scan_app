package com.example.whm.ui.interpodetails

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
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
import com.example.myapplication.com.example.whm.ui.home.HomeFragment
import com.example.myapplication.com.example.whm.ui.interpodetails.Detailsadapter
import org.json.JSONObject
import android.widget.CompoundButton
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1


class Internalpodetails : AppCompatActivity() {
    var toolbar: Toolbar?=null
    var  POAutoid:Int=0
    var autotextView: AutoCompleteTextView? = null
    var qty: TextView? = null
    var btnpoadd: ImageButton? = null
    var btnpoqtyminus: ImageButton? = null
    var totalpicesqty: TextView? = null
    var getdefault:String?=null
    var tqty:Int=0
    var qtyperunit:Int=0
    var Quantity:Int=0
    var totalpices:Int=0
    var Qty:Int=0
    var spUnitType: Spinner? = null
    var ReceiverpoList: ArrayList<DetailsItemsViewModel> = arrayListOf()
    lateinit var Detailsadapterpo: Detailsadapter
    var backarrow: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internalpodetails)
        val preferencesid = PreferenceManager.getDefaultSharedPreferences(this@Internalpodetails)
        POAutoid = preferencesid.getInt("POAutoid", 0)

        backarrow = findViewById(com.example.myapplication.R.id.imgbackbtm)
        if (POAutoid != null && POAutoid != 0) {
            val recyclerView: RecyclerView =
                findViewById(com.example.myapplication.R.id.POlistproduct)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            POproductlist()
            Detailsadapterpo = Detailsadapter(ReceiverpoList, this)
            recyclerView.adapter = Detailsadapterpo
        }
        else{
            CheckInterNetDailog()
        }
        if (AppPreferences.internetConnectionCheck(this)) {
            backarrow?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Internalpodetails)
                    val editor = preferences.edit()
                    editor.remove("POAutoid")
                    val intent = Intent(this@Internalpodetails, MainActivity2::class.java)
                    startActivity(intent)
                }
            })
        } else {
            CheckInterNetDailog()
        }
    }

    fun POproductlist() {
        val txtpono: TextView = findViewById(com.example.myapplication.R.id.txtrefno)
        val txtbilldatepo: TextView = findViewById(com.example.myapplication.R.id.txtpodate)
        val vendornamepo: TextView = findViewById(com.example.myapplication.R.id.vendornamepo)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)))
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

        JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POAutoid))

        val DRAFTADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.InternalPOLISTDETAILS, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                var PID:Int=0
                var POtAutoId:Int=0
                var UnitAutoId:Int=0
                var TotalPieces:Int=0
                var Quantity:Int=0
                var QtyPerUnit:Int=0
                var isFreeItem:Int=0
                var IsExchange:Int=0
                var PName: String? =null
                var PODate: String? =null
                var VendorName: String? =null
                var PONo: String? =null
                var UnitType: String? =null
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepdu = JSONObject(jsondata.toString())
                    val PODetails = jsonrepdu.getJSONArray("a")
                    for (i in 0 until PODetails.length()) {
                        PONo = PODetails.getJSONObject(i).getString("PONo")
                        PODate = PODetails.getJSONObject(i).getString("PODate")
                        VendorName = PODetails.getJSONObject(i).getString("VendorName")
                        POtAutoId = PODetails.getJSONObject(i).getInt("AutoId")
                        txtpono.text = PONo
                        txtbilldatepo.text = PODate
                        vendornamepo.text = VendorName
                    }



                    val POItems = jsonrepdu.getJSONArray("POItems")
                    for (i in 0 until POItems.length()) {
                        PName = POItems.getJSONObject(i).getString("ProductName")
                        PID = POItems.getJSONObject(i).getInt("ProductId")
                        UnitAutoId = POItems.getJSONObject(i).getInt("Unit")
                        UnitType = POItems.getJSONObject(i).getString("UnitType")
                        TotalPieces = POItems.getJSONObject(i).getInt("TotalPieces")
                        Quantity = POItems.getJSONObject(i).getInt("PackedQty")
                        QtyPerUnit = POItems.getJSONObject(i).getInt("QtyPerUnit")
                        isFreeItem = POItems.getJSONObject(i).getInt("isFreeItem")
                        IsExchange = POItems.getJSONObject(i).getInt("IsExchange")
                        var check=false
                        var poreqqty:Int=0

                        for (n in 0..ReceiverpoList.size-1) {

                            if(ReceiverpoList[n].getPID()==PID){
                                check=true
                                if (ReceiverpoList[n].getPOQTY() != null) {
                                    poreqqty = ReceiverpoList[n].getPOQTY()!! + 1
                                    Detailsadapterpo.notifyItemChanged(n)
                                    ReceiverpoList.removeAt(n)
                                    if (UnitType != null) {
                                        if (PName != null) {
                                            DataBindPOLIST(
                                                PID,
                                                PName,
                                                UnitType,
                                                QtyPerUnit,
                                                Quantity,
                                                POtAutoId,
                                                isFreeItem,
                                                IsExchange
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
                                        POtAutoId,
                                        isFreeItem,
                                        IsExchange
                                    )
                                }

                            }

                        }

                    }


                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()
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
                               DRaftID: Int,
                               IS_free: Int,
                               Is_exchaNGe: Int) {
        var POLIST = DetailsItemsViewModel(PID, PNAME, UNITTYPE,UnitQTY, POQTY,UnitQTY, DRaftID,IS_free,Is_exchaNGe)
        ReceiverpoList.add(0,POLIST)
        Detailsadapterpo.notifyDataSetChanged()


    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {

            val checked: Boolean = view.isChecked
          val  isexchange = findViewById<CheckBox>(R.id.isexchange);
            val isfreeitem = findViewById<CheckBox>(R.id.isfreeitem);
            when (view.id) {
                R.id.isexchange -> {
                    if (checked) {
                        isexchange.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
                            if (b) {
                                isfreeitem.setChecked(false)
                            }
                        })

                    } else {

                    }
                }
                R.id.isfreeitem -> {
                    if (checked) {

                    } else {

                    }
                }

            }
        }
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

        }
        dialog.show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menuitem, menu)
        var menuitems: MenuItem? = null
        var manualaddproduct: MenuItem? = null
        var verifyproduct: MenuItem? = null
         menuitems = menu?.findItem(R.id.draftsubmit)
        manualaddproduct = menu?.findItem(R.id.manualaddproduct)
        verifyproduct = menu?.findItem(R.id.verifyproduct)

        if (menuitems!=null){

            menuitems.isVisible=false

        }
        if (manualaddproduct != null) {
            manualaddproduct.isVisible=false
        }
        if(verifyproduct!=null){

            verifyproduct.isVisible=true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.savesubmit -> {
                if(ReceiverpoList.size>0){

                    ReceiverpoList.clear()
                    true
                }
                else{

                    false
                }

            }

            R.id.verifyproduct->{
                manualproductadd()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    fun manualproductadd() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.manualproductadddailog, null)
        val btnpoqty: Button = view.findViewById(R.id.btnsaevpoqty)
        val btncancel: Button = view.findViewById(R.id.btncancel)
        btnpoadd = view.findViewById(R.id.btnpoadd)
        btnpoqtyminus = view.findViewById(R.id.btnpoqtyminus)
        autotextView = view.findViewById<AutoCompleteTextView>(R.id.txtmpid)
        qty = view.findViewById<TextView>(R.id.qtym)
        qty!!.isEnabled =false
        btnpoadd?.isEnabled  = false
        btnpoqtyminus?.isEnabled  = false
        totalpicesqty = view.findViewById<TextView>(R.id.totalpicesqty)
        autotextView!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
               // BindProductList()
            }

            override fun afterTextChanged(s: Editable) {
               // BindProductList()
            }
        })
        qty!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (qty!!.text.toString() != "") {
                    tqty=getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt()
                    totalpices = tqty  * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                }
                else{
                    totalpicesqty!!.text = ""
                }
            }
        })

        spUnitType = view.findViewById(R.id.spunity)


        btnpoqty.setOnClickListener(View.OnClickListener {
            var name: String? = null
            name = spUnitType!!.selectedItem as String?
            var productname=autotextView!!.text.toString()
            if (spUnitType!!.selectedItem != null && qty!!.text.toString()!="" && qty!!.text.toString().toInt()!=0 && productname!="") {
                val recyclerView: RecyclerView =
                    findViewById(com.example.myapplication.R.id.POLIST)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager

                Detailsadapterpo = Detailsadapter(ReceiverpoList, this)
                recyclerView.adapter = Detailsadapterpo
                dialog?.dismiss()
            }
            else {
                if (productname=="")  {

                }
                else if(name == null ){

                }
                else{

                }
            }
        })
        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        btnpoadd?.setOnClickListener {
            if(qty!!.text.toString()!=""){
                if (qty!!.text.toString().toInt() >=0) {
                    tqty=getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt()+1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                }
                else{
                    totalpicesqty!!.text = "0"
                    qty!!.text = "0"
                }
            }
            else{
                qty!!.text = "1"
                tqty=getdefault!!.trim().toInt()
                Quantity = qty!!.text.toString().toInt()
                totalpicesqty!!.text = ((tqty * Quantity).toString())

            }

        }
        btnpoqtyminus!!.setOnClickListener {
            if (qty!!.text.toString() != "") {
                if (qty!!.text.toString().toInt() >0) {
                    tqty = getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt() - 1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                }

            }
            else {
                totalpicesqty!!.text = "0"
                qty!!.text = "0"
            }
        }
    }
}