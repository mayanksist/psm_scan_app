package com.example.whm.ui.inventoryreceive
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
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
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import org.json.JSONArray
import org.json.JSONObject
import android.widget.Spinner





class ReceivePO : AppCompatActivity() {
   // var backBTN: ImageView?=null
    var addbarcode: EditText?=null
    var backarrow: ImageView?=null

     var toolbar:Toolbar?=null
     var LinearLayoutV:LinearLayout?=null
     var LinearLayoutInventoey:LinearLayout?=null

    var ReceiverpoList: ArrayList<ReceiveModel> = arrayListOf()

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
        menuInflater.inflate(R.menu.menuitem, menu)
        return true
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

                manualproductadd("46464")

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
                    // this.findNavController().navigate(com.example.myapplication.R.id.nav_orderlist)
//                    val manager: FragmentManager = supportFragmentManager
//                    val count: Int = manager.getBackStackEntryCount()
//                    if (count > 0) {
//                        val mfragment: FragmentManager.BackStackEntry = manager.getBackStackEntryAt(count - 1)
//                        val ft: android.app.FragmentTransaction? = fragmentManager.beginTransaction()
//                        if (ft != null) {
//                            ft.replace(R.id.fragment_loadorder,  mfragment)
//                        }
//                        if (ft != null) {
//                            ft.commit()
//                        }
//                    }
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


    fun manualproductadd(PID: String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(com.example.myapplication.R.layout.manualproductadddailog, null)
        val PIPID: TextView = view.findViewById(com.example.myapplication.R.id.txtmpid)
        val btnpoqty: Button = view.findViewById(com.example.myapplication.R.id.btnsaevpoqty)
        val btncancel: Button = view.findViewById(com.example.myapplication.R.id.btncancel)

        PIPID.setText(PID)

        btnpoqty.setOnClickListener(View.OnClickListener {

                dialog?.dismiss()


        })

        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })

        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        BindUnitList()
    }
    fun BindUnitList(){
        var spUnitType: Spinner? = null
         spUnitType = findViewById(R.id.spunity)
        //val BINVENDERLIST =spvendor
        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer",Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer",Jsonarra.put("UserAutoId", EmpAutoId))
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

                    spinnerArray[0] = "Select unit"
                    spUnitType?.adapter = this?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, spinnerArray) } as SpinnerAdapter
                    spUnitType?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                            println("erreur")
                        }
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                            val VenderSelect = parent?.getItemIdAtPosition(position).toString()
                            if(position!=0) {
                                for (i in 0 until VenderSelect.toInt() + 1) {
                                  var  spvendorid = spinnerArrayId.get(index = i)
                                }
                            }
                        }
                    }
                } else {

                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText(responseMessage).show()


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
}








