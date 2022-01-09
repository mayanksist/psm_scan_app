package com.example.whm.ui.inventoryreceive

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
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
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1
import org.json.JSONArray
import org.json.JSONObject


class ReceivePO : AppCompatActivity() {
   // var backBTN: ImageView?=null
    var addbarcode: EditText?=null
    var backarrow: ImageView?=null
    var spUnitType: Spinner? = null
    var sproductid: String? = null
    var spunitypeid: String? = null
     var toolbar:Toolbar?=null
     var LinearLayoutV:LinearLayout?=null

    var autotextView: AutoCompleteTextView? = null
    var qty: TextView? = null
    var totalpicesqty: TextView? = null
    var list: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var ReceiverpoList: ArrayList<ReceiveModel> = arrayListOf()
    var spvendorid: String? = null
    var Quantity:Int=0
    var totalpices:Int=0
    var Qty:Int=0
    var  DUnit:Int=0
    private lateinit var ReceivePOAdapterl:ReceivePOAdapter1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        toolbar = findViewById(R.id.toolbarAction)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle("PO Receive")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbacode)
        backarrow = findViewById(com.example.myapplication.R.id.imgbackbtm)
        LinearLayoutV = findViewById(com.example.myapplication.R.id.LinearFragmentContainer)
     //   bindvenderlist()
        if (AppPreferences.internetConnectionCheck(this)) {
            backarrow?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val preferencesid = PreferenceManager.getDefaultSharedPreferences(this@ReceivePO)
                    var EmpAutoId = preferencesid.getString("EmpAutoId", "")
                    var EmpTypeNo = preferencesid.getString("EmpTypeNo", "")
                    var Empname = preferencesid.getString("Empname", "")
                    var Username = preferencesid.getString("Username", "")
                        val intent = Intent(this@ReceivePO, MainActivity2::class.java)
                         startActivity(intent)
                }
            })
        } else {
            CheckInterNetDailog()
        }

        if (AppPreferences.internetConnectionCheck(this)) {
            addbarcode!!.requestFocus()
            addbarcode!!.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    var     scanbarcodeproduct = addbarcode!!.text.toString()

                    val recyclerView: RecyclerView =
                        findViewById(com.example.myapplication.R.id.POLIST)
                    val layoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager

                    if (scanbarcodeproduct!!.trim().isEmpty()) {
                        addbarcode!!.text.clear()
                        addbarcode!!.setText("")
                        Toast.makeText(this, "Scan product", Toast.LENGTH_SHORT).show()
                        addbarcode!!.requestFocus()
                    }
                    else {
                        Addproductlist()
                    }
                    ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                    recyclerView.adapter = ReceivePOAdapterl
                }

                false

            })
        } else {
            CheckInterNetDailog()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menuitem, menu)




        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.savesubmit -> {
                if(ReceiverpoList.size>0){
                    SubmitPoList(2)
                    ReceiverpoList.clear()
                    true
                }
                else{
                    Itemlist()
                    false
                }

            }
            R.id.draftsubmit -> {
                if(ReceiverpoList.size>0) {
                    SubmitPoList(1)
                    true
                }
                else{
                    Itemlist()
                    false
                }
            }
            R.id.manualaddproduct->{

                manualproductadd()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    fun Itemlist() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Atleast One Item Required").show()
    }
    fun Addproductlist() {

        val barcodeadd: EditText = findViewById(com.example.myapplication.R.id.enterbacode)
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        var accessToken = preferences.getString("accessToken", "")
        var Bill_No = preferences.getString("Bill_No", "")
        var Bill_Date = preferences.getString("Bill_Date", "")
        var VENDORID = preferences.getInt("VENDORID", 0)
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )

        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftAutoIdTV.text.toString().toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("billNo", Bill_No))
        JSONObj.put("cObj", Jsonarrabarcode.put("billDate", Bill_Date))
        JSONObj.put("cObj", Jsonarrabarcode.put("vendorAutoId", VENDORID.toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("barcode", barcodeadd!!.text.toString()))
        JSONObj.put("cObj", Jsonarrabarcode.put("Remark", ""))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.SCAND_BARCODE_PADD, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val JSONOBJ = JSONObject(jsondata.toString())
                    val draftAutoId = JSONOBJ.getInt("draftAutoId")
                    val ProductId = JSONOBJ.getInt("ProductId")
                    val ProductName = JSONOBJ.getString("ProductName")
                    val UnitType = JSONOBJ.getString("UnitType")
                    val Qty = JSONOBJ.getInt("Qty")
                    draftAutoIdTV.text=draftAutoId.toString()
                    var check=false
                    var poreqqty:Int=0

                    for (n in 0..ReceiverpoList.size-1) {
                        if(ReceiverpoList[n].getPID()==ProductId){
                            check=true;
                            if (ReceiverpoList[n].getPOQTY() != null) {
                                poreqqty = ReceiverpoList[n].getPOQTY()!! + 1
//                                ReceiverpoList[n].setPOQTY(poreqqty)
//                                ReceiverpoList[n].setTotalPieces(poreqqty)
                                ReceivePOAdapterl.notifyItemChanged(n)
                                ReceiverpoList.removeAt(n)
                                DataBindPOLIST(
                                    ProductId,
                                    ProductName,
                                    UnitType,
                                    Qty,
                                    poreqqty,
                                    draftAutoId
                                )

                            }

                        }
                    }
                    if(!check) {
                        DataBindPOLIST(
                            ProductId,
                            ProductName,
                            UnitType,
                            Qty,
                            1,
                            draftAutoId
                        )
                    }


                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()
                        AppPreferences.playSoundbarcode()
//                  /  Toast.makeText(this, , Toast.LENGTH_SHORT).show()

                }
                barcodeadd.setText("")
                barcodeadd.requestFocus()
            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        BARCODEADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BARCODEADDPRODUCT)
    }
    private fun DataBindPOLIST(PID: Int, PNAME: String,
                               UNITTYPE: String,
                               UnitQTY:Int,
                               POQTY:Int,
                               DRaftID: Int) {
        var POLIST = ReceiveModel(PID, PNAME, UNITTYPE,UnitQTY, POQTY,UnitQTY, DRaftID)
        ReceiverpoList.add(0,POLIST)
        ReceivePOAdapterl.notifyDataSetChanged()


    }
    fun SubmitPoList(Status:Int) {
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftAutoIdTV.text.toString().toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("status", Status))

        val SUBMITPOLITS = JsonObjectRequest(
            Request.Method.POST, AppPreferences.SUBMIT_PO_LIST, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))

                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {

                    var intent = Intent(this, ReceivePO::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show()

                }

            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        SUBMITPOLITS.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(SUBMITPOLITS)
    }
    fun CheckInterNetDailog(){
        val dialog = this?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            var intent = Intent(this, ReceivePO::class.java)
            startActivity(intent)        }
        dialog?.show()
    }


    fun manualproductadd() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.manualproductadddailog, null)

         autotextView = view.findViewById<AutoCompleteTextView>(R.id.txtmpid)

         qty = view.findViewById<TextView>(R.id.qty)
        qty!!.isEnabled =false
        totalpicesqty = view.findViewById<TextView>(R.id.totalpicesqty)
     //   BindProductList()
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

            }

            override fun afterTextChanged(s: Editable) {
                BindProductList()
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
                    Quantity = qty!!.text.toString().toInt()
                    totalpices = Qty * Quantity
                    totalpicesqty!!.setText(totalpices.toString())
                }
            }
        })
        val btnpoqty: Button = view.findViewById(R.id.btnsaevpoqty)
        val btncancel: Button = view.findViewById(R.id.btncancel)
        spUnitType = view.findViewById(R.id.spunity)
//        spvendorid= spUnitType.toString()
        btnpoqty.setOnClickListener(View.OnClickListener {
            var name: String? = null
            name = spUnitType!!.getSelectedItem() as String?
            if (spUnitType!!.getSelectedItem() != null && qty!!.text.toString()!="" && qty!!.text.toString()!="0" && autotextView!!.text.toString()!="") {
                val recyclerView: RecyclerView =
                    findViewById(com.example.myapplication.R.id.POLIST)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                AddproductlistManual()
                ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                recyclerView.adapter = ReceivePOAdapterl
                dialog?.dismiss()
            }
            else {
                if (autotextView!!.text.toString()!="" || autotextView!!.text.toString()!=null)  {
                    Select_product()
                }
                else if(name == null ){
                    Select_UnitType()
                }
                else{
                    Select_Qty()
                }
            }
        })
        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
       // BindUnitList()

    }

    fun Select_UnitType() {

        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Unit Type").show()
    }
    fun Select_Qty() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Quantity can't be left empty or zero.").show()
    }
    fun Select_product() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Product ").show()
    }
    fun BindUnitList(){
        val Jsonarra = JSONObject()
        val Jsonarryproduct = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID",AppPreferences.Device_ID))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer",Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer",Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", Jsonarryproduct))
        JSONObj.put("filterkeyword", Jsonarryproduct.put("productId", sproductid))
        val BINDUNITTYPE =spUnitType
        val BindProductDetails = JsonObjectRequest(
            Request.Method.POST,AppPreferences.GET_PRODUCT_DETAILS, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {

                    val unitlist = resultobj.getJSONArray("responseData")
                    val n = unitlist.length()
                    val spinnerArray = arrayOfNulls<String>(n)
                    val spinnerArrayId = arrayOfNulls<String>(n)
                    for (i in 0 until unitlist.length()) {
                        var unittype = unitlist.getJSONObject(i).getString("UName")
                         Qty = unitlist.getJSONObject(i).getInt("Qty")
                        var UnitType = unitlist.getJSONObject(i).getInt("UnitType")
                         DUnit = unitlist.getJSONObject(i).getInt("DUnit")

                        Quantity=  qty!!.text.toString().toInt()
                        totalpices= Qty.toInt()*Quantity.toInt()
                        totalpicesqty!!.setText(totalpices.toString())
                        spinnerArray[i] = unittype+"("+Qty+"pcs)"
                        spinnerArrayId[i] = UnitType.toString()

                        BINDUNITTYPE?.adapter = this?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, spinnerArray) } as SpinnerAdapter
                        BINDUNITTYPE?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                BINDUNITTYPE!!.setSelection(spinnerArray.indexOf(DUnit.toString()))
                               spunitypeid = spinnerArrayId[position].toString()

                               // val unitposition = parent?.getItemIdAtPosition(position).toString()


                              //  Toast.makeText(this@ReceivePO,spunitypeid,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    }
                else{
                    Toast.makeText(this@ReceivePO,responseMessage,Toast.LENGTH_SHORT).show()

                }

            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BindProductDetails.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BindProductDetails)
    }



fun BindProductList(){

    val Jsonarra = JSONObject()
    val JSONObj = JSONObject()
    val Jsonarraplist = JSONObject()
    val queues = Volley.newRequestQueue(this)
    JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
    var accessToken = preferences.getString("accessToken", "")
    var EmpAutoId = preferences.getString("EmpAutoId", "")
    JSONObj.put("requestContainer",Jsonarra.put("accessToken", accessToken))
    JSONObj.put("requestContainer",Jsonarra.put("UserAutoId", EmpAutoId))
    JSONObj.put("requestContainer",Jsonarra.put("deviceID", AppPreferences.Device_ID))
    JSONObj.put("cObj", Jsonarraplist.put("search", autotextView!!.text))
    val BINDPRODUCTLIST = JsonObjectRequest(
        Request.Method.POST, AppPreferences.BIND_PRODUCT_IDNAME_BY_SEARCH, JSONObj,
        { response ->
            val resobj = (response.toString())
            val responsemsg = JSONObject(resobj)
            val resultobj = JSONObject(responsemsg.getString("d"))
            val responseCode = resultobj.getString("responseCode")
            val responseMessage = resultobj.getString("responseMessage")
            if (responseCode == "201") {
                val ProductList: JSONArray = resultobj.getJSONArray("responseData")
                val n = ProductList.length()
                val productArray = arrayOfNulls<String>(n)
                val productArrayId = arrayOfNulls<String>(n)
                for (i in 0 until n) {
                    val BINDLIST = ProductList.getJSONObject(i)
                    val PID = BINDLIST.getInt("PId")
                    val PNAME = BINDLIST.getString("PName")
                    productArray[i] = PID.toString() +"-"+ PNAME
                    productArrayId[i] = PID.toString()
                }
                val adapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, productArray)
                    autotextView?.setAdapter(adapter)
                    autotextView?.setOnItemClickListener( OnItemClickListener { adapterView, view, j, l ->
                       sproductid = productArrayId[j].toString()
                        qty!!.isEnabled =true

                         BindUnitList()


                })
            } else {
               Toast.makeText(this@ReceivePO,responseMessage,Toast.LENGTH_SHORT).show()
            }
        }, { response ->

            Log.e("onError", error(response.toString()))
        })
    BINDPRODUCTLIST.retryPolicy = DefaultRetryPolicy(
        1000000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )
    queues.add(BINDPRODUCTLIST)
}

    fun AddproductlistManual() {

        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
          var accessToken = preferences.getString("accessToken", "")
        var Bill_No = preferences.getString("Bill_No", "")
        var Bill_Date = preferences.getString("Bill_Date", "")
        var VENDORID = preferences.getInt("VENDORID", 0)
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )

        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftAutoIdTV.text.toString().toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("billNo", Bill_No))
        JSONObj.put("cObj", Jsonarrabarcode.put("billDate", Bill_Date))
        JSONObj.put("cObj", Jsonarrabarcode.put("vendorAutoId", VENDORID.toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("productId", sproductid))
        JSONObj.put("cObj", Jsonarrabarcode.put("UnitAutoId", spunitypeid))
        JSONObj.put("cObj", Jsonarrabarcode.put("Remark", ""))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.ADD_PRODUCT_MANAUL, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val JSONOBJ = JSONObject(jsondata.toString())
                    val draftAutoId = JSONOBJ.getInt("draftAutoId")
                    val ProductId = JSONOBJ.getInt("ProductId")
                    val ProductName = JSONOBJ.getString("ProductName")
                    val UnitType = JSONOBJ.getString("UnitType")
                    val Qty = JSONOBJ.getInt("Qty")
                    draftAutoIdTV.text=draftAutoId.toString()
                    var check=false
                    var poreqqty:Int=0

                    for (n in 0..ReceiverpoList.size-1) {
                        if(ReceiverpoList[n].getPID()==ProductId){
                            check=true;
                            if (ReceiverpoList[n].getPOQTY() != null) {
                                poreqqty = ReceiverpoList[n].getPOQTY()!! + qty!!.text.toString().toInt()

                                ReceivePOAdapterl.notifyItemChanged(n)
                                ReceiverpoList.removeAt(n)
                                DataBindPOLIST(
                                    ProductId,
                                    ProductName,
                                    UnitType,
                                    Qty,
                                    poreqqty,
                                    draftAutoId
                                )
                            }

                        }
                    }
                    if(!check) {
                        DataBindPOLIST(
                            ProductId,
                            ProductName,
                            UnitType,
                            Qty,
                            qty!!.text.toString().toInt(),
                            draftAutoId
                        )
                    }
                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()
                }

            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BARCODEADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BARCODEADDPRODUCT)
    }
}












