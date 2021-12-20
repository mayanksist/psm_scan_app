package com.example.whm.ui.inventoryreceive
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1
import com.example.myapplication.ui.product.setSupportActionBar
import org.json.JSONObject


class ReceivePO : AppCompatActivity() {
    var backBTN: ImageView?=null
    var addbarcode: EditText?=null
    var BtnSave: Button?=null
    var BtnDraft: Button?=null
    var toolbar:Toolbar?=null
    //    var POQTY:Int=0
    private  val ReceiverpoList=ArrayList<ReceiveModel>()
    private lateinit var ReceivePOAdapterl:ReceivePOAdapter1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        toolbar = findViewById(R.id.toolbarAction)
//        toolbar.setTitleTextColor(Color.RED)
      //  supportActionBar!!.show()
      //  supportActionBar!!.title="Text"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(toolbar)
        backBTN = findViewById(com.example.myapplication.R.id.back)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbacode)
        BtnSave = findViewById(com.example.myapplication.R.id.btnsubmit)
        BtnDraft = findViewById(com.example.myapplication.R.id.btnsaveasdraft)



        if (AppPreferences.internetConnectionCheck(this)) {
            backBTN?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onBackPressed()

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
                    if (scanbarcodeproduct!!.trim().isEmpty()) {
                        addbarcode!!.text.clear()
                        addbarcode!!.setText("")
                        Toast.makeText(this, "Scan product", Toast.LENGTH_SHORT).show()
                        addbarcode!!.requestFocus()


                    }
                    else {
                        Addproductlist()
                        val recyclerView: RecyclerView =
                            findViewById(com.example.myapplication.R.id.POLIST)
                        ReceivePOAdapterl = ReceivePOAdapter1(ReceiverpoList, this)
                        val layoutManager = LinearLayoutManager(this)
                        recyclerView.layoutManager = layoutManager
                        recyclerView.itemAnimator = DefaultItemAnimator()
                        recyclerView.adapter = ReceivePOAdapterl
                    }


                }

                false

            })
        } else {
            CheckInterNetDailog()
        }
        if (AppPreferences.internetConnectionCheck(this)) {

            BtnSave?.setOnClickListener { SubmitPoList(2) }
        }
        else{
            CheckInterNetDailog()
        }
        if (AppPreferences.internetConnectionCheck(this)) {

            BtnDraft?.setOnClickListener { SubmitPoList(1) }
        }
        else{
            CheckInterNetDailog()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuitem, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_main_setting2 -> {

                true
            }
            R.id.menu_main_setting -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            fragmentManager.popBackStack()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(com.example.myapplication.R.id.receive_PO, FragmentInventory())
                .addToBackStack(null).commit()

        } else {
            super.onBackPressed()

        }
    }
    fun Addproductlist() {

        val barcodeadd: EditText = findViewById(com.example.myapplication.R.id.enterbacode)
        val draftAutoIdTV: TextView = findViewById(com.example.myapplication.R.id.draftAutoId)
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
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
                            check=true
                            if (ReceiverpoList[n].getPOQTY() != null) {
                                poreqqty = ReceiverpoList[n].getPOQTY()!! + 1
                                ReceiverpoList[n].setPOQTY(poreqqty)
                                ReceiverpoList[n].setTotalPieces(poreqqty)
                                //  Toast.makeText(this,ReceiverpoList[0].getPID().toString(),Toast.LENGTH_LONG).show()

                            }
                        }
                    }
                    if(!check) {
                        DataBindPOLIST(
                            ProductId,
                            ProductName,
                            UnitType,
                            Qty,
                            1
                        )
                    }


                } else {
                    Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show()

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
    private fun DataBindPOLIST( PID: Int, PNAME: String,UNITTYPE: String,UnitQTY:Int,POQTY:Int) {
        var POLIST = ReceiveModel(PID, PNAME, UNITTYPE,UnitQTY, POQTY,UnitQTY)
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
}







