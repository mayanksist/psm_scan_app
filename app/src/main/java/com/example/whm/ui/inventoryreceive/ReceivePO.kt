package com.example.whm.ui.inventoryreceive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceivePOAdapter1
import org.json.JSONObject


class ReceivePO : AppCompatActivity() {
     var backBTN: ImageView?=null
     var addbarcode: EditText?=null


    private  val ReceiverpoList=ArrayList<ReceiveModel>()
    private lateinit var ReceivePOAdapterl:ReceivePOAdapter1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        backBTN = findViewById(com.example.myapplication.R.id.back)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbacode)

        backBTN?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                    onBackPressed()

            }
        })

        addbarcode!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if ((keyCode==KeyEvent.KEYCODE_ENTER) && (event.action==KeyEvent.ACTION_DOWN)){

                Addproductlist()
                val recyclerView: RecyclerView = findViewById(com.example.myapplication.R.id.POLIST)
                ReceivePOAdapterl= ReceivePOAdapter1(ReceiverpoList,this)
                val layoutManager = LinearLayoutManager(this)
                recyclerView.layoutManager = layoutManager
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = ReceivePOAdapterl
            }

            false

        })

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

        Toast.makeText(this, draftAutoIdTV.text.toString(), Toast.LENGTH_SHORT).show();


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
                    DataBindPOLIST(
                        ProductId,
                        ProductName,
                        UnitType,
                        Qty
                    )
//                    Toast.makeText(this, draftAutoId.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show()

                }
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
    private fun DataBindPOLIST( PID: Int, PNAME: String,UNITTYPE: String,POQTY:Int) {
        var POLIST = ReceiveModel(PID, PNAME, UNITTYPE,POQTY)
        ReceiverpoList.add(POLIST)
        ReceivePOAdapterl.notifyDataSetChanged()
    }
}





