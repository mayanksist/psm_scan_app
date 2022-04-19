package com.example.myapplication.ui.product

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.myapplication.ui.ProductList.ProductListViewModel
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.databinding.FragmentProductListBinding
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ProductList : Fragment() {

    private lateinit var productListViewModel: ProductListViewModel
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    var boxlist: MutableList<String> = ArrayList()
    var listarrayp: MutableList<String> = ArrayList()
    var FirstorderNO: String = ""
    var ordernoenter: String = ""
    var checkr = 0
    var PackedBoxes = 0
    var boxno: String = ""
    var maxTextSize: String = ""
    var SharedOrderNo = ""
    var msg: TextView? = null
    var txtscanproducts: TextView? = null
    var txtstop: TextView? = null
    var toolbar: Toolbar? = null
    var count = 0
    var orderno: EditText?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(
            R.layout.fragment_product_list,
            container,
            false
        )

        val txtallpicbox: TextView = binding.txtallpickbox
        if (AppPreferences.internetConnectionCheck(this.context)) {
             orderno = binding.txtorderno
            val noofboxes1: TextView = binding.txtpackedb
            val lastscanprd: TextView = binding.txtscanproduct

            msg = binding.txtmsg
            val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
            productListViewModel.text.observe(viewLifecycleOwner, Observer {
                orderno!!.requestFocus()
                val sharedLoadOrderPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this.context)
                SharedOrderNo = sharedLoadOrderPreferences.getString("OrderNo", "").toString()
                PackedBoxes = sharedLoadOrderPreferences.getInt("PackedBoxes", 0)
                var SharedStopNo = sharedLoadOrderPreferences.getString("Stoppage", "")
                val listofarray = sharedLoadOrderPreferences.getString("boxlist", "")
                val listofsize = sharedLoadOrderPreferences.getString("listsize", "")
                val SelectOrderNo = sharedLoadOrderPreferences.getString("SelectOrderNo", "")
                boxno = sharedLoadOrderPreferences.getString("boxNo", "").toString()
                if (listofarray != "[]") {
                    if (listofarray != null) {
                        if (listofarray != "") {
                            listarrayp =
                                listofarray.replace("[", "").replace("]", "").replace(" ", "")
                                    .split(",") as MutableList<String>
                        }
                    }
                }
                if(SelectOrderNo!="" && SelectOrderNo!=null) {
                    for (key in listarrayp) {
                        var test = key.trim()
                        boxlist.add(test)
                    }
                }
                setHasOptionsMenu(true)
                toolbar = view?.findViewById(R.id.toolbar)
                (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
                (activity as? AppCompatActivity)?.supportActionBar?.show()
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = SharedOrderNo
                (activity as AppCompatActivity?)
                    ?.closeOptionsMenu()
                if (activity is AppCompatActivity) {
                    (activity as AppCompatActivity?)?.supportActionBar
                        ?.setDisplayHomeAsUpEnabled(true)
                }
                val StopNo: TextView = binding.txtstoppage
                val cardview: CardView = binding.cardView2
                StopNo.text = SharedStopNo
                cardview.visibility = View.VISIBLE
                if (boxno != "") {
                    try {
                        if (boxlist.size != 0) {
                            boxlist.add(0, boxno.trim())
                            checkr = 1
                            FirstorderNO = SharedOrderNo
                            if (boxlist.size.toString() == PackedBoxes.toString()) {
                                submitorder(SharedOrderNo)
                            }
                        } else {
                            if (SelectOrderNo != null) {
                                checkr = 1
                                orderdetailsbind(SharedOrderNo, SelectOrderNo)
                            }
                        }
                        lastscanprd.text = boxlist.toString()
                        noofboxes1.text = boxlist.size.toString() + " out of " + "" + PackedBoxes
                    } catch (e: IOException) {
                        Toast.makeText(this.context, e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
                if (listofarray == "" && listofsize == "") {
                    noofboxes1.text = "0 out of " + PackedBoxes
                }
                orderno!!.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        if (AppPreferences.internetConnectionCheck(this.context)) {
                            ordernoenter = orderno!!.text.toString().uppercase(Locale.getDefault())
                            val layout = binding.txtmsg
                            layout.visibility = View.GONE
                            if(ordernoenter.length>0) {
                                if (ordernoenter.contains("/")) {
                                    val result1 = ordernoenter.trim().split("/").toMutableList()

                                    if (result1[0].trim() == SharedOrderNo) {
                                        if (SelectOrderNo != null && result1[0].length == 0) {
                                            if (SelectOrderNo.split("/")[0] != "") {
                                                FirstorderNO = result1[0]
                                                try {
                                                    if (boxlist.size > 0) {
                                                        checkr = 1
                                                    }
                                                } catch (e: IOException) {
                                                }
                                            }
                                        }
                                        boxno = result1[1]
                                        val convertboxno = boxno.toIntOrNull()
                                        if (convertboxno != null) {
                                            if (convertboxno <= PackedBoxes) {
                                                if (FirstorderNO == "") {
                                                    FirstorderNO = result1[0]
                                                } else {
                                                    count = 0
                                                    if (FirstorderNO == result1[0]) {
                                                        for (i in boxlist) {
                                                            if (i == result1[1]) {
                                                                val layout = binding.txtmsg
                                                                layout.visibility = View.VISIBLE
                                                                orderno!!.setText("")
                                                                msg!!.text = "Box Already Scanned."
                                                                AppPreferences.playSoundinvalidalready()
                                                                count = 1
                                                                orderno!!.requestFocus()
                                                            }
                                                        }
                                                        if (count == 0) {
                                                            maxTextSize = boxlist.size.toString()
                                                            txtscanproducts = binding.txtscanproduct
                                                            if (maxTextSize == "30") {
                                                                txtscanproducts!!.setTextSize(
                                                                    TypedValue.COMPLEX_UNIT_SP,
                                                                    30f
                                                                )
                                                            }
                                                            if (maxTextSize == "45") {
                                                                txtscanproducts!!.setTextSize(
                                                                    TypedValue.COMPLEX_UNIT_SP,
                                                                    25f
                                                                )
                                                            }

                                                            boxlist.add(0, boxno)
                                                            orderno!!.setText("")
                                                            noofboxes1.text =
                                                                boxlist.size.toString() + " out of " + "" + PackedBoxes
                                                            lastscanprd.text =
                                                                boxlist.toString()
                                                                    .replace("  ", " ")
                                                            msg!!.text = ""
                                                            if (boxlist.size.toString() == PackedBoxes.toString()) {
                                                                submitorder(FirstorderNO)
                                                            }
                                                        }
                                                    } else {
                                                        val layout = binding.txtmsg
                                                        layout.visibility = View.VISIBLE
                                                        orderno!!.setText("")
                                                        msg!!.text = "Invalid box scanned."
                                                        AppPreferences.playSoundinvalid()
                                                    }
                                                }

                                                if (checkr == 0) {
                                                    try {
                                                        orderdetailsbind(FirstorderNO, ordernoenter)
                                                        orderno!!.setText("")
                                                    } catch (e: IOException) {
                                                        Toast.makeText(
                                                            this.context,
                                                            "Error",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            } else {
                                                //msg for box no not equal to packedbox
                                                //   pDialog.dismiss()
                                                val layout = binding.txtmsg
                                                layout.visibility = View.VISIBLE
                                                orderno!!.setText("")
                                                msg!!.text = "Invalid box scanned."
                                                AppPreferences.playSoundinvalid()
                                            }
                                        } else {
                                            //msg for enter int value and some text
                                            val layout = binding.txtmsg
                                            layout.visibility = View.VISIBLE
                                            orderno!!.setText("")
                                            msg!!.text = "Invalid box scanned."
                                            AppPreferences.playSoundinvalid()
                                        }
                                    } else {
                                        val layout = binding.txtmsg
                                        layout.visibility = View.VISIBLE
                                        orderno!!.setText("")
                                        msg!!.text = "Invalid box scanned."
                                        AppPreferences.playSoundinvalid()
                                    }
                                } else {
                                    val layout = binding.txtmsg
                                    layout.visibility = View.VISIBLE
                                    orderno!!.setText("")
                                    msg!!.text = "Invalid box scanned."
                                    AppPreferences.playSoundinvalid()
                                }
                            }

                            return@OnKeyListener true
                        }
                        else{
                            CheckInterNetDailog()
                        }
                    }
                    false
                })
                orderno!!.requestFocus()
                txtallpicbox.setOnClickListener {
                    if (AppPreferences.internetConnectionCheck(this.context)) {
                        pDialog.dismiss()
                        PickAllBoxesDailog(
                            "Are you sure you want to pick all boxes?",
                            SharedOrderNo
                        )
                    }

                else {
                        CheckInterNetDailog()
                    }
            }
                val view: ScrollView = binding.scrollView
                view.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                view.isFocusable = false
                view.isFocusableInTouchMode = false
                view.setOnTouchListener { v, event ->
                    v.requestFocusFromTouch()
                    false
                }
            })
        }
        else {
            CheckInterNetDailog()
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun orderdetailsbind(orderno1: String, barcode: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        val cardview: CardView = binding.cardView2
        txtstop = binding.txtstoppage
        txtscanproducts = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        var accessToken = preferences.getString("accessToken", "")
        JSONObj.put("requestContainer",Jsonarra.put("deviceID",
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNO", orderno1)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", details))
        val resorderno = JsonObjectRequest(Request.Method.POST,
            AppPreferences.BASEURL + AppPreferences.GET_ORDERS,
            JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val presponsmsg = resultobj.getString("responseMessage")
                if (presponsmsg == "Orders Found") {
                    cardview.visibility = View.GONE
                    cardview.visibility = View.VISIBLE
                    pDialog.dismiss()
                    val jsondata = resultobj.getJSONArray("responseData")
                    val preferences =
                        PreferenceManager.getDefaultSharedPreferences(this.context)
                    val editor1 = preferences.edit()
                    for (i in 0 until jsondata.length()) {
                        var OrderNo= jsondata.getJSONObject(i).getString("OrderNo")
                        val noofboxes = jsondata.getJSONObject(i).getInt("PackedBoxes")
                        val stoppage = jsondata.getJSONObject(i).getInt("Stoppage")
                        if(SharedOrderNo==OrderNo) {
                            if (boxno.toInt() <= noofboxes) {
                                txtstop!!.text = "${stoppage}"
                                FirstorderNO = SharedOrderNo
                                editor1.putString("OrderNO", SharedOrderNo)
                                editor1.putInt("NoofBox", noofboxes)
                                editor1.apply()
                                boxlist.add(0, boxno)
                                PackedBoxes = noofboxes
                                txtpacked.text =
                                    boxlist.size.toString() + " out of " + "${noofboxes}"
                                txtscanproducts!!.text = boxlist.toString()
                                if (boxlist.size.toString() == PackedBoxes.toString()) {
                                    submitorder(SharedOrderNo)
                                }
                            }
                            else {
                                pDialog.dismiss()
                                val layout = binding.txtmsg
                                layout.visibility = View.VISIBLE
                                msg!!.text= "Invalid box scanned."
                                AppPreferences.playSoundinvalid()
                                orderno!!.text.clear()
                            }
                            checkr = 1
                            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
                            (activity as AppCompatActivity?)!!.supportActionBar!!.title = SharedOrderNo
                        }
                        else{
                            pDialog.dismiss()
                            val layout = binding.txtmsg
                            layout.visibility = View.VISIBLE
                            msg!!.text= "Invalid box scanned."
                            AppPreferences.playSoundinvalid()
                            checkr = 0
                            orderno!!.text.clear()
                            FirstorderNO=""
                        }
                    }
                }
                else {
                    pDialog.dismiss()
                    val alertorfailed = AlertDialog.Builder(this.context)
                    alertorfailed.setCancelable(false)
                    alertorfailed.setTitle(orderno1)
                    alertorfailed.setMessage(presponsmsg.toString())
                    alertorfailed.setPositiveButton(
                        "ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            clear()
                            val orderno4: EditText = binding.txtorderno
                            orderno4.text.clear()
                            this.findNavController()
                                .navigate(com.example.myapplication.R.id.nav_orderlist)
                        })
                    FirstorderNO = ""
                    checkr = 0
                    val dialog: AlertDialog = alertorfailed.create()
                    dialog.show()
                }
            },
            { response ->
                Log.e("onError", error(response.toString()))
            })
        resorderno.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        try {
            queues.add(resorderno)
        } catch (e: IOException) {
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }
    }
    fun submitorder(sorderno: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        val cardview1: CardView = binding.cardView2
        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        var accessToken = preferences.getString("accessToken", "")
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("deviceID",
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
        JSONObj.put("OrderNo", sorderno)
        val alertsuborder = AlertDialog.Builder(this.context)
        val resordernos = JsonObjectRequest(Request.Method.POST,
            AppPreferences.BASEURL + AppPreferences.SUBMIT_LOAD_ORDER,
            JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val rspCode = resultobj.getString("responseCode")
                val rspMsg = resultobj.getString("responseMessage")
                if (rspCode.toString() == "200") {
                    AppPreferences.playSound()
                    pDialog.dismiss()
                            val orderno3: EditText = binding.txtorderno
                            orderno3.text.clear()
                            this.findNavController()
                                .navigate(com.example.myapplication.R.id.nav_orderlist)
                                     FirstorderNO = ""
                    checkr = 0
                    val timerout = Timer()
                        timerout.schedule(object : TimerTask() {
                        override fun run() {
                            //dialog.dismiss()
                            timerout.cancel()
                        }
                    }, 2000)

                    boxlist.clear()
                    clear()
                    msg!!.text = ""
                    cardview1.visibility = View.GONE
                } else {
                    pDialog.dismiss()
                    alertsuborder.setCancelable(false)
                    alertsuborder.setTitle(sorderno.uppercase(Locale.getDefault()))
                    alertsuborder.setMessage(rspMsg.toString())
                    alertsuborder.setPositiveButton(
                        "ok"
                    ) { dialog, which ->
                        clear()
                        orderno!!.text.clear()
                        this.findNavController()
                            .navigate(com.example.myapplication.R.id.nav_orderlist)
                    }
                    FirstorderNO = ""
                    checkr = 0
                    val dialog: AlertDialog = alertsuborder.create()
                    dialog.show()
                    cardview1.visibility = View.GONE
                }
            },
            { response ->
                Log.e("onError", error(response.toString()))
            })
        resordernos.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        try {
            queues.add(resordernos)
        } catch (e: IOException) {
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }
    }
    fun clear() {
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        txtstop.text = "N/A"
        txtscanproduct.text = "N/A"
        txtpacked.text = "0"
    }
    //pick all boxes dailog function
    fun PickAllBoxesDailog(MSG: String,title: String){
        val sharedLoadOrderPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        val sharedLoadOrderPage =
            sharedLoadOrderPreferences.edit()
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        val alert = AlertDialog.Builder(this.context)
        pDialog.dismiss()
        alert.setCancelable(false)
        alert.setTitle(title)
        alert.setMessage(MSG)
        alert.setNegativeButton("YES")
        { dialog, which ->
            boxlist.clear()
            maxTextSize = ""
            count = 0
            alert.setTitle("")

            submitorder(title)
            dialog.dismiss()
        }
        alert.setPositiveButton("NO")
        { dialog, which ->
            dialog.dismiss()
            alert.setTitle("")
        }
        alert.setCancelable(false)
        val dialog = alert.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setBackgroundColor(resources.getColor(R.color.red))
            val negative: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            negative.isFocusable = true
            negative.isFocusableInTouchMode = true
            negative.requestFocus(View.FOCUS_FORWARD)
            val positive: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            positive.isFocusable = false
            positive.isFocusableInTouchMode = false
            positive.requestFocus(View.FOCUS_FORWARD)


        }
        dialog.show()
        sharedLoadOrderPage.remove("OrderNo")
        sharedLoadOrderPage.remove("PackedBoxes")
        sharedLoadOrderPage.remove("SelectOrderNo")
        sharedLoadOrderPage.apply()
        val orderno1: EditText = binding.txtorderno
        orderno1.setText("")
        //alert.show()
    }
    //check internet connection
    fun CheckInterNetDailog(){
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
        }
        dialog?.setCancelable(false)
        dialog?.show()
//        val alertnet = AlertDialog.Builder(activity)
//        alertnet.setTitle("Connection")
//        alertnet.setMessage("Please check your internet connection")
//        alertnet.setPositiveButton("ok")
//        { dialog, which ->
//            dialog.dismiss()
//
//        }
//        val dialog: AlertDialog = alertnet.create()
//        dialog.show()
    }
}
fun AppCompatActivity?.setSupportActionBar(toolbar: Toolbar?) {
}