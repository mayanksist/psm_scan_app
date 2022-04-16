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
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.whm.ui.internalpolist.InternalpoListFragment
import org.json.JSONArray
import android.widget.CheckBox





class Internalpodetails : AppCompatActivity() {


    var toolbar: Toolbar? = null
    var POAutoid: Int = 0
    var addbarcode: EditText? = null
    var autotextView: AutoCompleteTextView? = null
    var qty: TextView? = null
    var btnpoadd: ImageButton? = null
    var btnpoqtyminus: ImageButton? = null
    var totalpicesqty: TextView? = null
    var getdefault: String? = null
    var tqty: Int = 0
    var PoQuantity: Int = 0
    var qtyperunit: Int = 0
    public final var ChkIsFree: CheckBox? = null
    public final var ChkIsExchange: CheckBox? = null
    var Quantity: Int = 0
    var totalpices: Int = 0
    var spunitypeid: String? = null
    var sproductid: String? = null
    var DUnit: Int = 0
    var Qty: Int = 0
    var LinearLayoutV: LinearLayout? = null
    var spUnitType: Spinner? = null
    var scheckedIsfree: Int = 0
    var scheckedIsexchange: Int = 0
    var ReceiverpoList: ArrayList<DetailsItemsViewModel> = arrayListOf()
    lateinit var Detailsadapterpo: Detailsadapter
    var backarrow: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_internalpodetails)
        toolbar = findViewById(R.id.toolbarAction)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val preferencesid = PreferenceManager.getDefaultSharedPreferences(this@Internalpodetails)
        val editor = getSharedPreferences("CheckShared", MODE_PRIVATE).edit()
        POAutoid = preferencesid.getInt("POAutoid", 0)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbarcode)
        backarrow = findViewById(com.example.myapplication.R.id.imgbackbtm)
        LinearLayoutV = findViewById(com.example.myapplication.R.id.LinearFragmentContainer)
        if (POAutoid != null && POAutoid != 0) {
            // supportActionBar?.setTitle("Internal PO List")

            val recyclerView: RecyclerView =
                findViewById(com.example.myapplication.R.id.POlistproduct)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            POproductlist()
            Detailsadapterpo = Detailsadapter(ReceiverpoList, this)
            recyclerView.adapter = Detailsadapterpo

        } else {
            CheckInterNetDailog()
        }
        if (AppPreferences.internetConnectionCheck(this)) {
            backarrow?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val preferences =
                        PreferenceManager.getDefaultSharedPreferences(this@Internalpodetails)
                    val editor = preferences.edit()
                    editor.remove("POAutoid")
                    val intent = Intent(this@Internalpodetails, MainActivity2::class.java)
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
                    var scanbarcodeproduct = addbarcode!!.text.toString()

                    val recyclerView: RecyclerView =
                        findViewById(com.example.myapplication.R.id.POlistproduct)
                    val layoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager

                    if (scanbarcodeproduct.trim().isEmpty()) {
                        addbarcode!!.text.clear()
                        addbarcode!!.setText("")
                        Toast.makeText(this, "Scan product", Toast.LENGTH_SHORT).show()
                        addbarcode!!.requestFocus()
                    } else {
                        Scanproductlist()
                    }
                    Detailsadapterpo = Detailsadapter(ReceiverpoList, this)
                    recyclerView.adapter = Detailsadapterpo
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
        var menuitems: MenuItem? = null
        var manualaddproduct: MenuItem? = null
        var verifyproduct: MenuItem? = null
        menuitems = menu?.findItem(R.id.draftsubmit)
        manualaddproduct = menu?.findItem(R.id.manualaddproduct)
        verifyproduct = menu?.findItem(R.id.verifyproduct)

        if (menuitems != null) {

            menuitems.isVisible = false

        }
        if (manualaddproduct != null) {
            manualaddproduct.isVisible = false
        }
        if (verifyproduct != null) {

            verifyproduct.isVisible = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.savesubmit -> {
                if (ReceiverpoList.size > 0) {
                    SubmitPoList()
                    ReceiverpoList.clear()
                    true
                } else {

                    false
                }

            }

            R.id.verifyproduct -> {
                manualproductadd()
                true
            }

            else -> super.onOptionsItemSelected(item)
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
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
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

        val POADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.InternalPOLISTDETAILS, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                var PID: Int = 0
                var POtAutoIdh: Int = 0
                var UnitAutoId: Int = 0
                var TotalPieces: Int = 0

                var PQuantity: Int = 0
                var VerifiedQty: Int = 0
                var QtyPerUnit: Int = 0
                var isFreeItem: Int = 0
                var IsExchange: Int = 0
                var PName: String? = null
                var PODate: String? = null
                var VendorName: String? = null
                var PONo: String? = null
                var UnitType: String? = null
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepdu = JSONObject(jsondata.toString())
                    val PODetails = jsonrepdu.getJSONArray("a")
                    for (i in 0 until PODetails.length()) {
                        PONo = PODetails.getJSONObject(i).getString("PONo")
                        PODate = PODetails.getJSONObject(i).getString("PODate")
                        VendorName = PODetails.getJSONObject(i).getString("VendorName")
                        POtAutoIdh = PODetails.getJSONObject(i).getInt("AutoId")
                        txtpono.text = PONo
                        txtbilldatepo.text = PODate
                        vendornamepo.text = VendorName
                    }
                    val POItems = jsonrepdu.getJSONArray("POItems")
                    for (i in 0 until POItems.length()) {
                        PName = POItems.getJSONObject(i).getString("ProductName")
                        PID = POItems.getJSONObject(i).getInt("ProductId")
                        UnitAutoId = POItems.getJSONObject(i).getInt("L_Unit")
                        UnitType = POItems.getJSONObject(i).getString("L_UnitType")
                        TotalPieces = POItems.getJSONObject(i).getInt("TotalPackedPieces")
                        PQuantity = POItems.getJSONObject(i).getInt("PackedQty")
                        QtyPerUnit = POItems.getJSONObject(i).getInt("L_QtyPerUnit")
                        isFreeItem = POItems.getJSONObject(i).getInt("isFreeItem")
                        IsExchange = POItems.getJSONObject(i).getInt("IsExchange")
                        VerifiedQty = POItems.getJSONObject(i).getInt("VerifiedQty")
                        var check = false
                        if (!check) {
                            if (UnitType != null) {
                                if (PName != null) {
                                    DataBindPOLIST(
                                        PID,
                                        PName,
                                        UnitType,
                                        PQuantity,
                                        TotalPieces,
                                        POtAutoIdh,
                                        isFreeItem,
                                        IsExchange,
                                        VerifiedQty,
                                        UnitAutoId
                                    )
                                }

                            }


                        }

                    }


                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(
                        responseMessage
                    ).show()
                }

            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        POADDPRODUCT.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(POADDPRODUCT)
    }

    private fun DataBindPOLIST(
        PID: Int, PNAME: String,
        UNITTYPE: String,
        PackedQTY: Int,
        Totalpieces: Int,
        POautoID: Int,
        IS_free: Int,
        Is_exchaNGe: Int,
        Is_VerifyQty: Int,
        UnitAutoid: Int
    ) {
        var POLIST = DetailsItemsViewModel(
            PID,
            PNAME,
            UNITTYPE,
            PackedQTY,
            Totalpieces,
            POautoID,
            IS_free,
            Is_exchaNGe,
            Is_VerifyQty,
            UnitAutoid
        )
        ReceiverpoList.add(0, POLIST)
        Detailsadapterpo.notifyDataSetChanged()


    }


    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {

            val checked: Boolean = view.isChecked
            val isexchange = findViewById<CheckBox>(R.id.isexchange);
            val isfreeitem = findViewById<CheckBox>(R.id.isfreeitem);

            when (view.id) {
                R.id.isexchange -> {
                    if (checked) {
                                isfreeitem.isChecked = false
                    } else {
                        isexchange.isChecked = false
                    }
                }
                R.id.isfreeitem -> {
                    if (checked) {
                                isexchange.isChecked = false
                    } else {
                        isfreeitem.isChecked = false
                    }
                }
            }
        }
    }

    fun CheckInterNetDailog() {
        val dialog = this.let { Dialog(it) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss =
            dialog.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            val mServiceIntent = Intent(this, HomeFragment::class.java)
            startActivity(mServiceIntent)

        }
        dialog.show()
    }


    fun manualproductadd() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.internalpomanualproductadd, null)
        val btnpoqty: Button = view.findViewById(R.id.btnaddbarcode)
        val btncancel: Button = view.findViewById(R.id.btncancel)

        btnpoadd = view.findViewById(R.id.btnpoadd)
        ChkIsExchange = view.findViewById(R.id.chkexchange)
        ChkIsFree = view.findViewById(R.id.chkfree)
        btnpoqtyminus = view.findViewById(R.id.btnpoqtyminus)
        autotextView = view.findViewById<AutoCompleteTextView>(R.id.txtmpid)
        qty = view.findViewById<TextView>(R.id.qtym)
        qty!!.isEnabled = false
        btnpoadd?.isEnabled = false
        btnpoqtyminus?.isEnabled = false
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
                BindProductPOList()
            }

            override fun afterTextChanged(s: Editable) {
              //  BindProductPOList()
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
                    tqty = getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt()
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                } else {
                    totalpicesqty!!.text = ""
                }
            }
        })

        spUnitType = view.findViewById(R.id.spunity)
        btnpoqty.setOnClickListener(View.OnClickListener {
            var name: String? = null
            name = spUnitType!!.selectedItem as String?
            var productname = autotextView!!.text.toString()
            if (spUnitType!!.selectedItem != null && qty!!.text.toString() != "" && qty!!.text.toString()
                    .toInt() != 0 && productname != ""
            ) {
                val recyclerView: RecyclerView =
                    findViewById(com.example.myapplication.R.id.POlistproduct)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                AddproductlistManual()
                Detailsadapterpo = Detailsadapter(ReceiverpoList, this)
                recyclerView.adapter = Detailsadapterpo
                dialog?.dismiss()


            } else {
                if (productname == "") {
                    Select_product()
                } else if (name == null) {
                    Select_UnitType()
                } else {
                    Select_Qty()
                }
            }
        })

        ChkIsExchange!!.setOnClickListener { view ->
            if (ChkIsExchange!!.isChecked) {
                ChkIsFree!!.isChecked = false


            } else {
                ChkIsExchange!!.isChecked = false

            }
        }
        ChkIsFree!!.setOnClickListener { view ->
            if (ChkIsFree!!.isChecked) {
                ChkIsExchange!!.isChecked = false


            } else {
                ChkIsFree!!.isChecked = false
            }
        }
        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        btnpoadd?.setOnClickListener {
            if (qty!!.text.toString() != "") {

                if (qty!!.text.toString().toInt() >= 0) {
                    tqty = getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt() + 1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                } else {
                    totalpicesqty!!.text = "0"
                    qty!!.text = "0"
                }
            } else {
                qty!!.text = "1"
                tqty = getdefault!!.trim().toInt()
                Quantity = qty!!.text.toString().toInt()
                totalpicesqty!!.text = ((tqty * Quantity).toString())

            }

        }
        btnpoqtyminus!!.setOnClickListener {
            if (qty!!.text.toString() != "") {
                if (qty!!.text.toString().toInt() > 0) {
                    tqty = getdefault!!.trim().toInt()
                    Quantity = qty!!.text.toString().toInt() - 1
                    totalpices = tqty * Quantity
                    totalpicesqty!!.text = totalpices.toString()
                    qty!!.text = Quantity.toString()
                }

            } else {
                totalpicesqty!!.text = "0"
                qty!!.text = "0"
            }
        }
    }

    fun Select_UnitType() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Unit Type")
            .show()
    }

    fun Select_Qty() {
        SweetAlertDialog(
            this,
            SweetAlertDialog.ERROR_TYPE
        ).setContentText("Quantity can not be left empty or zero.").show()
    }

    fun Select_product() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Select Product ").show()
    }

    fun Scanproductlist() {
        val barcodeadd: EditText = findViewById(com.example.myapplication.R.id.enterbarcode)
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.POAutoId)
        val isexchange: CheckBox = findViewById(com.example.myapplication.R.id.isexchange)
        val isfreeitem: CheckBox = findViewById(com.example.myapplication.R.id.isfreeitem)
        val txtrefno: TextView = findViewById(com.example.myapplication.R.id.txtrefno)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
        var accessToken = preferences.getString("accessToken", "")


        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        if (POAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POAutoid))
        } else {
            JSONObj.put(
                "cObj",
                Jsonarrabarcode.put("POAutoId", draftAutoIdTV.text.toString().toInt())
            )
        }


        scheckedIsexchange = 0
        scheckedIsfree = 0
        if (isexchange!!.isChecked) {
            isfreeitem.setChecked(false)
            scheckedIsexchange = 1
            JSONObj.put("cObj", Jsonarrabarcode.put("IsExchange", scheckedIsexchange))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("IsExchange", 0))
        }
        if (isfreeitem!!.isChecked) {
            isexchange.setChecked(false)
            scheckedIsfree = 1
            JSONObj.put("cObj", Jsonarrabarcode.put("IsFree", scheckedIsfree))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("IsFree", 0))
        }
        JSONObj.put("cObj", Jsonarrabarcode.put("barcode", barcodeadd.text.toString().trim()))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.INTERNAL_PO_SCANBARCODE_PRODUCT, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "200") {
                    val jsondata = resultobj.getString("responseData")
                    val JSONOBJ = JSONObject(jsondata.toString())
                    val EMessage = JSONOBJ.getString("response")
                    if (EMessage == "Error") {
                        val ErrorMessage = JSONOBJ.getString("ErrorMessage")
                        val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        dialog.contentText = ErrorMessage.toString()
                        dialog.setCancelable(false)
                        dialog.show()
                    } else {
                        val ProductId = JSONOBJ.getInt("ProductId")
                        val ProductName = JSONOBJ.getString("ProductName")
                        val UnitType = JSONOBJ.getString("L_UnitType")
                        val UnitAutoid = JSONOBJ.getInt("L_Unit")
                        val PQty = JSONOBJ.getInt("PackedQty")
                        val VerifiedQty = JSONOBJ.getInt("VerifiedQty")
                        val TotalPackedPieces = JSONOBJ.getInt("TotalPackedPieces")
                        for (n in 0..ReceiverpoList.size - 1) {
                            if (ReceiverpoList[n].getPID() == ProductId && ReceiverpoList[n].getISfree() == scheckedIsfree && ReceiverpoList[n].getIsexchaNGe() == scheckedIsexchange) {

                                if (ReceiverpoList[n].getIs_VerifyQty() != null) {
                                    Detailsadapterpo.notifyItemChanged(n)
                                    ReceiverpoList.removeAt(n)
                                    DataBindPOLIST(
                                        ProductId,
                                        ProductName,
                                        UnitType,
                                        PQty,
                                        TotalPackedPieces,
                                        POAutoid,
                                        scheckedIsfree,
                                        scheckedIsexchange,
                                        VerifiedQty,
                                        UnitAutoid

                                    )
                                }
                            }
                        }

                    }

                } else {
                    val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    dialog.contentText = responseMessage
                    dialog.setCancelable(false)
                    dialog.show()
                    AppPreferences.playSoundbarcode()
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


    fun BindProductPOList() {

        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val Jsonarraplist = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
        JSONObj.put("cObj", Jsonarraplist.put("search", autotextView!!.text))
        JSONObj.put("cObj", Jsonarraplist.put("POAutoId", POAutoid))
        val BINDPRODUCTLISTm = JsonObjectRequest(
            Request.Method.POST, AppPreferences.INTERNAL_PO_PRODUCT_LIST, JSONObj,
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
                        val PID = BINDLIST.getInt("ProductId")
                        val PNAME = BINDLIST.getString("ProductName")
                        val IsExchange = BINDLIST.getInt("IsExchange")
                        val isFreeItem = BINDLIST.getInt("isFreeItem")
                        PoQuantity = BINDLIST.getInt("Qty")

                        productArray[i] = PID.toString() + "-" + PNAME
                        productArrayId[i] = PID.toString()

                    }
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line, productArray
                    )
//                    autotextView?.showDropDown()
                    autotextView?.threshold = 2
                    autotextView?.setAdapter(adapter)

                    adapter?.setNotifyOnChange(true)
                    adapter?.notifyDataSetChanged()
                    autotextView?.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, j, _ ->
                            sproductid = productArrayId[j].toString()
                            qty!!.isEnabled = true
                            btnpoadd?.isEnabled = true
                            btnpoqtyminus?.isEnabled = true
                            BindUnitList()
                        }

                }
            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BINDPRODUCTLISTm.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BINDPRODUCTLISTm)
    }


    fun BindUnitList() {
        val Jsonarra = JSONObject()
        val Jsonarryproduct = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", Jsonarryproduct))
        JSONObj.put("filterkeyword", Jsonarryproduct.put("productId", sproductid))
        val BINDUNITTYPE = spUnitType
        val BindProductDetails = JsonObjectRequest(
            Request.Method.POST, AppPreferences.GET_PRODUCT_DETAILS, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var UnitType: Int = 0
                    val unitlist = resultobj.getJSONArray("responseData")
                    val n = unitlist.length()
                    val spinnerArray = arrayOfNulls<String>(n)
                    val spinnerArrayId = arrayOfNulls<String>(n)
                    for (i in 0 until unitlist.length()) {
                        var unittype = unitlist.getJSONObject(i).getString("UName")

                        Qty = unitlist.getJSONObject(i).getInt("Qty")
                        UnitType = unitlist.getJSONObject(i).getInt("UnitType")
                        DUnit = unitlist.getJSONObject(i).getInt("DUnit")
                        if (DUnit == UnitType) {
                            spinnerArray[i] = unittype + " (" + Qty + "pcs) *"
                        } else {
                            spinnerArray[i] = unittype + " (" + Qty + "pcs)"
                        }
                        spinnerArrayId[i] = UnitType.toString()

                        BINDUNITTYPE?.adapter = this.let {
                            ArrayAdapter(
                                it,
                                R.layout.support_simple_spinner_dropdown_item,
                                spinnerArray
                            )
                        }
                        BINDUNITTYPE!!.setSelection(spinnerArrayId.indexOf(DUnit.toString()))
                        BINDUNITTYPE.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    spunitypeid = spinnerArrayId[position].toString()
                                    var spunitypename = spinnerArray[position].toString()
                                    if (spunitypename.contains("(")) {
                                        val result1 =
                                            spunitypename.trim().split("(").toMutableList()
                                        getdefault = result1[1].replace("pcs)", "").replace("*", "")
                                        totalpicesqty!!.text = getdefault.toString()
                                        var defaultqty: Int = 1
                                        qty!!.text = defaultqty.toString()

                                    }
                                }
                            }
                    }
                } else {
                    Toast.makeText(this@Internalpodetails, responseMessage, Toast.LENGTH_SHORT)
                        .show()

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

    fun AddproductlistManual() {
        val PAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.POAutoId)

        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )

        if (POAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POAutoid))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", PAutoIdTV.text.toString().toInt()))
        }
        JSONObj.put("cObj", sproductid?.let { Jsonarrabarcode.put("productId", it.toInt()) })
        JSONObj.put("cObj", spunitypeid?.let { Jsonarrabarcode.put("UnitAutoId", it.toInt()) })
        scheckedIsexchange = 0
        if (ChkIsExchange!!.isChecked) {
            ChkIsExchange!!.setChecked(false)
            scheckedIsexchange = 1
            JSONObj.put("cObj", Jsonarrabarcode.put("IsExchange", scheckedIsexchange))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("IsExchange", 0))
        }
        scheckedIsfree = 0
        if (ChkIsFree!!.isChecked) {
            scheckedIsfree = 1
            ChkIsFree!!.setChecked(false)
            JSONObj.put("cObj", Jsonarrabarcode.put("IsFree", scheckedIsfree))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("IsFree", 0))
        }
        JSONObj.put("cObj", Jsonarrabarcode.put("POQty", qty!!.text.toString().toInt()))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.INTERNAL_PO_ADD_PRODUCT_MANAUL, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val JSONOBJ = JSONObject(jsondata.toString())
                    val EMessage = JSONOBJ.getString("response")
                    if (EMessage == "Error") {
                        val ErrorMessage = JSONOBJ.getString("ErrorMessage")
                        val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        dialog.contentText = ErrorMessage.toString()
                        dialog.setCancelable(false)
                        dialog.show()
                    } else {

                        val ProductId = JSONOBJ.getInt("ProductId")
                        val ProductName = JSONOBJ.getString("ProductName")
                        val UnitType = JSONOBJ.getString("L_UnitType")
                        val UnitTypeid = JSONOBJ.getInt("L_Unit")
                        val PQty = JSONOBJ.getInt("PackedQty")
                        val TotalPackedPieces = JSONOBJ.getInt("TotalPackedPieces")
                        PAutoIdTV.text = POAutoid.toString()


                        for (n in 0..ReceiverpoList.size - 1) {

                            if (ReceiverpoList[n].getPID() == ProductId && ReceiverpoList[n].getISfree() == scheckedIsfree && ReceiverpoList[n].getIsexchaNGe() == scheckedIsexchange) {

                                if (ReceiverpoList[n].getIs_VerifyQty() != null) {
                                    Detailsadapterpo.notifyItemChanged(n)
                                    ReceiverpoList.removeAt(n)
                                    DataBindPOLIST(
                                        ProductId,
                                        ProductName,
                                        UnitType,
                                        PQty,
                                        TotalPackedPieces,
                                        POAutoid,
                                        scheckedIsfree,
                                        scheckedIsexchange,
                                        qty!!.text.toString().toInt(),
                                        UnitTypeid

                                    )
                                }
                            }
                        }
                    }

                } else {
                    val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    dialog.contentText = responseMessage
                    dialog.setCancelable(false)
                    dialog.show()
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


    fun SubmitPoList() {
        val POAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.POAutoId)
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
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
            )
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        if (POAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POAutoid))
        } else {
            JSONObj.put(
                "cObj",
                Jsonarrabarcode.put("POAutoId", POAutoIdTV.text.toString().toInt())
            )
        }

        JSONObj.put("cObj", Jsonarrabarcode.put("Status", "1"))

        val SUBMITPOLITS = JsonObjectRequest(
            Request.Method.POST, AppPreferences.INTERNAL_PO_Submit, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))

                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var alertbox = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.confirmText = "ok"
                    alertbox.setConfirmClickListener { sDialog ->
                        var intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

                } else {
                    var alertbox = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.cancelButtonBackgroundColor = Color.parseColor("#4cae4c")

                    alertbox.confirmText = "ok"
                    alertbox.confirmButtonBackgroundColor = Color.parseColor("#E60606")
                    alertbox.setConfirmClickListener { sDialog ->
                        var intent = Intent(this, InternalpoListFragment::class.java)
                        startActivity(intent)

                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

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



}

