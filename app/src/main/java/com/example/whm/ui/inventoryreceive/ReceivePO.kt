package com.example.whm.ui.inventoryreceive
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.R
import android.app.PendingIntent.getActivity
import android.view.Menu
import androidx.fragment.app.FragmentTransaction
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.home.HomeFragment
import com.example.myapplication.com.example.whm.ui.inventoryreceive.ReceiveModel
import com.example.myapplication.com.example.whm.ui.load_order_page.LoadOrderListAdapter
import org.json.JSONArray
import org.json.JSONObject


class ReceivePO : AppCompatActivity() {
     var backBTN: ImageView?=null
     var addbarcode: EditText?=null
    var scanbarcode:String?=null
    var layoutId: FrameLayout?=null

//    private lateinit var LoadorderAdapter: LoadOrderListAdapter
    private  val ReceiverpoList=ArrayList<ReceiveModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        backBTN = findViewById(com.example.myapplication.R.id.back)
        addbarcode = findViewById(com.example.myapplication.R.id.enterbacode)

        val recyclerView: RecyclerView = findViewById(com.example.myapplication.R.id.POLIST)
      //  LoadorderAdapter = LoadOrderListAdapter(LoadorderList, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()


      //  recyclerView.adapter = LoadorderAdapter


        backBTN?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {


                    onBackPressed()

//                var intent = Intent(applicationContext, FragmentInventory::class.java)
//                startActivity(intent)
//                fragmentManager.beginTransaction()
//                    .add(com.example.myapplication.R.id.layoutpo, FragmentInventory())
//                    .addToBackStack(FragmentInventory::class.java.getSimpleName())
//                    .commit()
                 //   val mServiceIntent = Intent(applicationContext, FragmentInventory::class.java)
               // mServiceIntent.data = Uri.parse(savedFilePath)
              //  startActivity(mServiceIntent)
//                val ft = supportFragmentManager.beginTransaction()
//                ft.replace(com.example.myapplication.R.layout.activity_receive_po, FragmentInventory())
//                ft.addToBackStack(null)
//                ft.commit()

            }
        })

        addbarcode!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if ((keyCode==KeyEvent.KEYCODE_ENTER) && (event.action==KeyEvent.ACTION_DOWN)){

                Addproductlist()
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

//        val barcodeadd: Spinner = findViewById(com.example.myapplication.R.id.enterbacode)
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
        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", 0))
        JSONObj.put("cObj", Jsonarrabarcode.put("billNo", Bill_No))
        JSONObj.put("cObj", Jsonarrabarcode.put("billDate", Bill_Date))
        JSONObj.put("cObj", Jsonarrabarcode.put("vendorAutoId", VENDORID.toInt()))
        JSONObj.put("cObj", Jsonarrabarcode.put("barcode", addbarcode!!.text))
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
                    val Qty = JSONOBJ.getString("Qty")

                    Toast.makeText(this, draftAutoId.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, responseMessage, Toast.LENGTH_SHORT).show();


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
}





