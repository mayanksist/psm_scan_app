package com.example.myapplication.ui.product

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import android.widget.FrameLayout

import android.widget.ScrollView
import android.view.MotionEvent
import android.view.View.OnTouchListener


class ProductList : Fragment() {

    private lateinit var productListViewModel: ProductListViewModel
    private var _binding: FragmentProductListBinding? = null


    private val binding get() = _binding!!
    val list: MutableList<String> = ArrayList()
    val boxlist: MutableList<String> = ArrayList()
    var FirstorderNO: String = ""
    var ordernoenter: String = ""
    var checkr = 0
    var totalBoxes = 0
    var boxno: String = ""
    var  maxTextSize: String = ""
    var SharedOrderNo = ""
    var msg: TextView? = null
    var txtscanproducts: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        productListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var count = 0
        val txtallpicbox: TextView = binding.txtallpickbox

        val view = inflater.inflate(
            com.example.myapplication.R.layout.fragment_product_list,
            container,
            false
        )
        view.requestFocus();
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    false
                }
                true
            }
            true
        })

        if(AppPreferences.internetConnectionCheck(this.context)) {
            val orderno: EditText = binding.txtorderno
            val noofboxes1: TextView = binding.txtpackedb
            val lastscanprd: TextView = binding.txtscanproduct

            val alert = AlertDialog.Builder(this.context)
            msg = binding.txtmsg
            productListViewModel.text.observe(viewLifecycleOwner, Observer {
                orderno.requestFocus()
                val sharedLoadOrderPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this.context)
                 SharedOrderNo = sharedLoadOrderPreferences.getString("OrderNo", "").toString()
                var PackedBoxes = sharedLoadOrderPreferences.getString("PackedBoxes", "")
                var SharedStopNo = sharedLoadOrderPreferences.getString("Stoppage", "")
                    setHasOptionsMenu(true)
                    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
                    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
                    (activity as? AppCompatActivity)?.supportActionBar?.show()
                    (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""+SharedOrderNo
                    var Ono = SharedOrderNo
                    val StopNo: TextView = binding.txtstoppage
                    val cardview: CardView = binding.cardView2
                    cardview.visibility = View.VISIBLE
                    noofboxes1.text = "0 out of " + PackedBoxes
                    StopNo.text = SharedStopNo
                    val sharedLoadOrder = PreferenceManager.getDefaultSharedPreferences(activity)
                    val sharedLoadOrderPage = sharedLoadOrder.edit()
                    sharedLoadOrderPage.putString("OrderNo", "")
                    sharedLoadOrderPage.putString("PackedBoxes", "")
                    sharedLoadOrderPage.putString("Stoppage", "")
                    sharedLoadOrderPage.putInt("PageValue", 0)
                    sharedLoadOrderPage.apply()
                orderno.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
                        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                        pDialog.titleText = "Fetching ..."
                        pDialog.setCancelable(false)
                        pDialog.show()
                        ordernoenter = orderno.text.toString().uppercase(Locale.getDefault())
                        val layout = binding.txtmsg
                        layout.visibility = View.GONE
                        if (ordernoenter.contains("/")) {
                            val result1 = ordernoenter.split("/")
                            boxno = result1[1]
                            if (FirstorderNO == "") {
                                pDialog.dismiss()
                                FirstorderNO = result1[0]
                            } else {
                                if (FirstorderNO == result1[0]) {
                                    pDialog.dismiss()

                                    if (boxno.toInt() <= totalBoxes) {
                                        count = 0
                                        for (i in list) {
                                            if (i == ordernoenter) {
                                                val layout = binding.txtmsg
                                                layout.visibility = View.VISIBLE
                                                orderno.setText("")
                                                msg!!.text = "Box Already Scanned."
                                                count = 1
                                                orderno.requestFocus()
                                                val params = LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                ).apply {
                                                    setMargins(0,70,0,70)
                                                }
                                            }
                                        }
                                        if (count == 0) {
                                            maxTextSize = list.size.toString()
                                            txtscanproducts = binding.txtscanproduct
                                            if (maxTextSize == "30") {
                                                txtscanproducts!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f);
                                            }
                                            if (maxTextSize == "45") {
                                                txtscanproducts!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
                                            }
                                            list.add(list.size, ordernoenter)
                                            boxlist.add(0, boxno)
                                            orderno.setText("")
                                            noofboxes1.text =
                                                list.size.toString() + " out of " + "" + totalBoxes
                                            lastscanprd.text = boxlist.toString()
                                            msg!!.text = ""
                                            if (list.size.toString() == totalBoxes.toString()) {
                                                submitorder(FirstorderNO)
                                            }


                                        }
                                    } else {
                                        msg!!.text = "invalid box number"
                                    }
                                } else {
                                    pDialog.dismiss()
                                    alert.setTitle(result1[0])
                                    alert.setMessage("Are you sure you want  to skip current order " + FirstorderNO + "?")
                                    alert.setNegativeButton("YES")
                                    { dialog, which ->
                                        list.clear()
                                        boxlist.clear()
                                        maxTextSize=""
                                        txtscanproducts!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45f);
                                        count = 0
                                        alert.setTitle("")
                                        orderdetailsbind(result1[0], ordernoenter)
                                        FirstorderNO = result1[0]
                                        dialog.dismiss()
                                    }
                                    alert.setPositiveButton("NO")
                                    { dialog, which ->
                                        dialog.dismiss()
                                        alert.setTitle("")
                                    }

                                    alert.show()
                                    orderno.setText("")
                                }

                            }
                            if (checkr == 0) {
                                try {
                                    pDialog.dismiss()
                                    orderdetailsbind(FirstorderNO, ordernoenter)
                                    orderno.setText("")
                                } catch (e: IOException) {
                                    Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            pDialog.dismiss()
                            AppPreferences.playSoundinvalid()
                            alert.setMessage("Invalid box scanned")
                            alert.setPositiveButton("ok")
                            { dialog, which ->
                                alert.setCancelable(true)
                                orderno.setText("")
                            }

                            alert.setNegativeButton("", null)
                            val adialog: AlertDialog = alert.create()
                            adialog.show()
                            val orderno1: EditText = binding.txtorderno
                            orderno1.setText("")
                            msg!!.text = ""

                        }
                        return@OnKeyListener true
                    }
                    false
                })
                orderno.requestFocus()
                txtallpicbox.setOnClickListener {
                    this.findNavController().navigate(com.example.myapplication.R.id.nav_allpickbox)
                    var SharedOrderNo = sharedLoadOrderPreferences.getString("OrderNo", "")
                    Toast.makeText(this.context, SharedOrderNo, Toast.LENGTH_LONG).show()

                }
                val view:ScrollView =binding.scrollView
                view.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                view.isFocusable = false
                view.isFocusableInTouchMode = false
                view.setOnTouchListener { v, event ->
                    v.requestFocusFromTouch()
                    false
                }


            })
        } else {
            val alertnet = AlertDialog.Builder(activity)
            alertnet.setTitle("Connection")
            alertnet.setMessage("Please check your internet connection")
            alertnet.setPositiveButton("ok")
            { dialog, which ->
                dialog.dismiss()
                this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
            }
            val dialog: AlertDialog = alertnet.create()
            dialog.show()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    fun orderdetailsbind(orderno: String, barcode: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(false)
        pDialog.show()
        val cardview: CardView = binding.cardView2
        //Toast.makeText(this.context, barcoded, Toast.LENGTH_SHORT).show()
        var txtorderno =SharedOrderNo
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb

        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.25"
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        var accessToken = preferences.getString("accessToken", "")
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNO", orderno)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", details))
        val resorderno = JsonObjectRequest(Request.Method.POST, AppPreferences.BASEURL+AppPreferences.GET_ORDERS, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val resmsg = resultobj.getString("response")
                val presponsmsg = resultobj.getString("responseMessage")
                if (resmsg == "failed") {
                    pDialog.dismiss()
                    val alertorfailed = AlertDialog.Builder(this.context)
                    alertorfailed.setTitle(orderno)
                    alertorfailed.setMessage(presponsmsg.toString())
                    alertorfailed.setPositiveButton(
                        "ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            clear()
                            val orderno4: EditText = binding.txtorderno
                            orderno4.text.clear()

                        })
                    FirstorderNO = ""
                    checkr = 0
                    val dialog: AlertDialog = alertorfailed.create()
                    dialog.show()
                    cardview.visibility = View.GONE

                } else {
                    if (presponsmsg == "Orders Found") {
                        cardview.visibility = View.GONE
                        cardview.visibility = View.VISIBLE
                        pDialog.dismiss()
                        val jsondata = resultobj.getJSONArray("responseData")
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this.context)
                        val editor1 = preferences.edit()
                        for (i in 0 until jsondata.length()) {
                            val dorderno = jsondata.getJSONObject(i).getString("OrderNo")
                            val noofboxes = jsondata.getJSONObject(i).getInt("PackedBoxes")
                            val stoppage = jsondata.getJSONObject(i).getInt("Stoppage")
                            txtstop.text = "${stoppage}"

                            editor1.putString("OrderNO", dorderno)
                            editor1.putInt("NoofBox", noofboxes)
                            editor1.apply()
                            list.add(0, barcode)
                            boxlist.add(0, boxno)
                            totalBoxes = noofboxes.toInt()
                            txtpacked.text =
                                list.size.toString() + " out of " + "${noofboxes}"
                            txtscanproduct.text = boxlist.toString()
                            if (list.size.toString() == totalBoxes.toString()) {
                                submitorder(dorderno)
                            }
//                            msg!!.text = list.toString()
                        }
                        checkr = 1
                    } else {
                        pDialog.dismiss()
                        val alertscanord = AlertDialog.Builder(this.context)
                        alertscanord.setTitle(orderno.toString().uppercase(Locale.getDefault()))
                        alertscanord.setMessage("Order No does not exist")
                        alertscanord.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertscanord.create()
                        dialog.show()
                        val orderno1: EditText = binding.txtorderno
                        orderno1.text.clear()
                        checkr = 0
                    }
                }
            }, { response ->
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
        JSONObj.put("OrderNo", sorderno)
        val alertsuborder = AlertDialog.Builder(this.context)
        val resordernos = JsonObjectRequest(Request.Method.POST, AppPreferences.BASEURLSU+AppPreferences.SUBMIT_LOAD_ORDER, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val rspCode = resultobj.getString("responseCode")
                val rspMsg = resultobj.getString("responseMessage")
                if (rspCode == "202") {
                    pDialog.dismiss()

                    alertsuborder.setTitle(sorderno.uppercase(Locale.getDefault()))
                    alertsuborder.setMessage(rspMsg.toString())
                    alertsuborder.setPositiveButton(
                        "ok"
                    ) { dialog, which ->
                        clear()
                        val orderno2: EditText = binding.txtorderno
                        orderno2.text.clear()

                    }
                    FirstorderNO = ""
                    checkr = 0
                    val dialog: AlertDialog = alertsuborder.create()
                    dialog.show()
                    cardview1.visibility = View.GONE
                } else {
                    if (rspCode.toString() == "200") {
                       AppPreferences.playSound()
                        pDialog.dismiss()
                        alertsuborder.setTitle(sorderno.toString().uppercase(Locale.getDefault()))
                        alertsuborder.setMessage(rspMsg.toString())
                        alertsuborder.setPositiveButton(
                            "ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                val orderno3: EditText = binding.txtorderno
                                orderno3.text.clear()
                                this.findNavController()
                                    .navigate(com.example.myapplication.R.id.nav_orderlist)
                            })
                        FirstorderNO = ""
                        checkr = 0
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                        list.clear()
                        boxlist.clear()
                        clear()
                        msg!!.text = ""
                        clear()
                        cardview1.visibility = View.GONE
                    }
                    else {
                        pDialog.dismiss()
                        alertsuborder.setTitle(sorderno.toString())
                        alertsuborder.setMessage("Order  does not submit!!!")
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                    }
                }
            }, { response ->
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
}


fun AppCompatActivity?.setSupportActionBar(toolbar: Toolbar?) {

}
