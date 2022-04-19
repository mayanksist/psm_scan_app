package com.example.whm.ui.AddBarcode

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import org.json.JSONArray
import org.json.JSONObject

class AddBarcodeFragment : Fragment() {
    lateinit var mView: View
    var spUnitType: Spinner? = null
    var addbarcode: EditText? = null
    var barcode: String? = ""
    var sproductid: String? = null
    var toolbar: Toolbar? = null
    var autotextView: AutoCompleteTextView? = null
    var qty: TextView? = null
    var list: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var DAutoid: Int = 0
    var Status: Int = 0
    var Qty: Int = 0
    var DUnit: Int = 0
    var spunitypeid: String? = null
    var getdefault: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.add_barcode_fragment, container, false)
        autotextView = mView.findViewById(R.id.txtmpid)
        spUnitType = mView.findViewById(R.id.spunity)
        addbarcode = mView.findViewById<EditText>(R.id.enterbarcode)
        val btnSaveBarcode: Button = mView.findViewById(R.id.btnaddbarcode)
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
                BindProductList()
            }

            override fun afterTextChanged(s: Editable) {
                // BindProductList()
            }
        })
        btnSaveBarcode.setOnClickListener(View.OnClickListener {
            var name: String? = null
            name = spUnitType!!.selectedItem as String?
            var productname = autotextView!!.text.toString().trim()
            if (spUnitType!!.selectedItem != null && addbarcode!!.text.toString().trim() != "" && productname != "") {
                APIAddBarcode()
            } else {
                if (productname == "") {
                    Select_product()
                } else if (name == null) {
                    Select_UnitType()
                } else {
                    Check_Barcode()
                }
            }
        })
        return mView
    }

    fun BindProductList() {

        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val Jsonarraplist = JSONObject()
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(
                    context?.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        )
        JSONObj.put("cObj", Jsonarraplist.put("search", autotextView!!.text))
        val BINDPRODUCTLISTm = JsonObjectRequest(
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
                        productArray[i] = PID.toString() + "-" + PNAME
                        productArrayId[i] = PID.toString()
                    }
                    adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_dropdown_item_1line, productArray
                        )
                    }
                    autotextView?.threshold = 2
                    autotextView?.setAdapter(adapter)

                    adapter?.setNotifyOnChange(true)
                    adapter?.notifyDataSetChanged()
                    autotextView?.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, j, _ ->
                            sproductid = productArrayId[j].toString()
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
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(
                    context?.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        )
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
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

                        BINDUNITTYPE?.adapter = this.context?.let {
                            ArrayAdapter(
                                it,
                                R.layout.support_simple_spinner_dropdown_item, spinnerArray
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
                                    }
                                }
                            }
                    }
                } else {
                    Toast.makeText(this.context, responseMessage, Toast.LENGTH_SHORT).show()
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

    fun APIAddBarcode() {
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this.context)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put(
                "deviceID",
                Settings.Secure.getString(
                    context?.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", preferences.getString("accessToken", ""))
        )
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("userAutoId", preferences.getString("EmpAutoId", ""))
        )
        JSONObj.put("pObj", Jsonarrabarcode.put("productId", sproductid))
        JSONObj.put("pObj", Jsonarrabarcode.put("UnitAutoId", spunitypeid))
        JSONObj.put("pObj", Jsonarrabarcode.put("barcode", addbarcode!!.text.toString().trim()))
        val BARCODEADDPRODUCT = JsonObjectRequest(
            Request.Method.POST, AppPreferences.API_ADD_BARCODE, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "200") {
                    var alertbox = SweetAlertDialog(
                        this.context, SweetAlertDialog.SUCCESS_TYPE
                    )
                    alertbox.contentText = responseMessage
                    alertbox.confirmText = "Ok"
                    alertbox.confirmButtonBackgroundColor = Color.parseColor("#4cae4c")
                    alertbox.setConfirmClickListener { sDialog ->
                        autotextView?.setText("")
                        spUnitType?.adapter = null
                        addbarcode?.setText("")
                        alertbox.dismiss()
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

                } else {
                    val dialog = SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE)
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

    fun Select_UnitType() {
        SweetAlertDialog(
            this.context,
            SweetAlertDialog.ERROR_TYPE
        ).setContentText("Select Unit Type").show()
    }

    fun Check_Barcode() {
        SweetAlertDialog(
            this.context,
            SweetAlertDialog.ERROR_TYPE
        ).setContentText("Please enter barcode").show()
    }

    fun Select_product() {
        SweetAlertDialog(
            this.context,
            SweetAlertDialog.ERROR_TYPE
        ).setContentText("Select Product ").show()
    }
}