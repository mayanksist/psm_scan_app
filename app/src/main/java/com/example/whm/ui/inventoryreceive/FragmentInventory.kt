package com.example.whm.ui.inventoryreceive
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import org.json.JSONObject
import org.json.JSONArray
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import cn.pedant.SweetAlert.SweetAlertDialog


class FragmentInventory  : Fragment(R.layout.fragment_inventory_fragment){
    lateinit var mView: View

    var txtbildate: TextView? = null
    var edtbillNo: EditText? = null
    var spvendor: Spinner? = null
    var spvendorid: String? = null
    var btnNext: Button?=null
    var test:Int?=0




    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_inventory_fragment, container, false)
        txtbildate = mView.findViewById(R.id.txtbildate)
        edtbillNo = mView.findViewById(R.id.txtbillno)
        spvendor = mView.findViewById(R.id.ddlvenderlist)
        btnNext = mView.findViewById(R.id.btnnext)
        bindvenderlist()


        txtbildate?.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        txtbildate?.text =  date

                    }

                }
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

        btnNext?.setOnClickListener {
                val Bill_No = edtbillNo
                val Bill_Date: CharSequence? =  txtbildate?.text
//                val Vendor_ID =spvendor
                val VENDORID = spvendorid
            // Toast.makeText(context,VENDORID!!.toString(),Toast.LENGTH_LONG).show()
                if (TextUtils.isEmpty(Bill_No?.getText().toString())) {
                   EnertBill_No()
                }
                else if (TextUtils.isEmpty(Bill_Date.toString())) {
                    EnertBill_Date()

                }
                else if(VENDORID==null || VENDORID=="" || VENDORID=="0"){
                    Select_Vendor()
                }
                else {
                    val intent = Intent(context, ReceivePO::class.java)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = preferences.edit()
                    editor.putString("Bill_No", Bill_No?.text.toString())
                    editor.putString("Bill_Date", Bill_Date.toString())
                    editor.remove("DAutoid")
                    if (VENDORID != null) {
                        editor.putInt("VENDORID", VENDORID.toInt())
                    }
                    editor.apply()
//                intent.putExtra("billna", txt);
                    startActivity(intent)
                }
            }

   return mView

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, save
//
//        dInstanceState)
//        _binding = FragmentInventoryFragmentBinding.bind(view)
//
//        binding.apply {
//            txtbildate.setOnClickListener {
//                // create new instance of DatePickerFragment
//                val datePickerFragment = DatePickerFragment()
//                val supportFragmentManager = requireActivity().supportFragmentManager
//
//                // we have to implement setFragmentResultListener
//                supportFragmentManager.setFragmentResultListener(
//                    "REQUEST_KEY",
//                    viewLifecycleOwner
//                ) { resultKey, bundle ->
//                    if (resultKey == "REQUEST_KEY") {
//                        val date = bundle.getString("SELECTED_DATE")
//                        txtbildate.text =  date
//                    }
//                }
//
//                // show
//                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
//            }
//
//
//        }
//
//    }

    fun bindvenderlist(){
        val BINVENDERLIST =spvendor
        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID",AppPreferences.Device_ID))
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
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
        val BINDVENDERLIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.Bind_VENDER_LIST, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val venderlist: JSONArray = resultobj.getJSONArray("responseData")
                    val n = venderlist.length()
                    val spinnerArray = arrayOfNulls<String>(n)
                    val spinnerArrayId = arrayOfNulls<String>(n)
                    for (i in 0 until n) {
                        val BINDLIST = venderlist.getJSONObject(i)
                        val VID = BINDLIST.getInt("Aid")
                        val VNAME = BINDLIST.getString("VName")
                        spinnerArray[i] = VNAME
                        spinnerArrayId[i] = VID.toString()
                    }

                    spinnerArray[0] = "Select Vendor"
                    BINVENDERLIST?.adapter = context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, spinnerArray) } as SpinnerAdapter
                    BINVENDERLIST?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                            println("erreur")
                        }
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            val VenderSelect = parent?.getItemIdAtPosition(position).toString()
                            if(position!=0) {
                                for (i in 0 until VenderSelect.toInt() + 1) {
                                    spvendorid = spinnerArrayId.get(index = i)
                                }
                            }
                        }
                    }
                } else {

                    SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()


                }
            }, { response ->

                Log.e("onError", error(response.toString()))
            })
        BINDVENDERLIST.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(BINDVENDERLIST)
    }



    fun EnertBill_No() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Enter Bill No").show()
    }
    fun EnertBill_Date() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Select Bill Date").show()
    }
    fun Select_Vendor() {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText("Select Vendor").show()
    }
}

