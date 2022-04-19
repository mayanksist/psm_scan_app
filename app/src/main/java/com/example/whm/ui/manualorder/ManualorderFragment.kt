package com.example.whm.ui.manualorder

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.databinding.FragmentManualorderBinding
import org.json.JSONObject
import java.io.IOException
import java.util.*


class ManualorderFragment : Fragment() {
    private lateinit var viewModelManualorder: ManualViewModel
    private var _binding: FragmentManualorderBinding?=null
    private val binding get() = _binding!!
    val list: MutableList<String> = ArrayList()
    val boxlist: MutableList<String> = ArrayList()
    var FirstorderNO: String = ""
    var ordernoenter: String = ""
    var checkr = 0
    var totalBoxes = 0
    var boxno=0
    var  maxTextSize: String = ""
    var msg: TextView? = null
    var count = 0
    var txtscanproducts: TextView? = null
    var txtorderno: TextView?=null
    var orderno: EditText?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelManualorder = ViewModelProvider(this).get(ManualViewModel::class.java)
        _binding = FragmentManualorderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(
            com.example.myapplication.R.layout.fragment_manualorder,
            container,
            false
        )
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    false
                }
                true
            }
            true
        })
        val txtallpicbox: TextView = binding.txtallpickbox
        if(AppPreferences.internetConnectionCheck(this.context)) {
             orderno = binding.txtorderno
            val noofboxes1: TextView = binding.txtpackedb
            val lastscanprd: TextView = binding.txtscanproduct
            val layout = binding.txtmsg
            msg = binding.txtmsg
            val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
            viewModelManualorder.text.observe(viewLifecycleOwner,{
                orderno!!.requestFocus()
                orderno!!.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        if(AppPreferences.internetConnectionCheck(this.context)) {
                            ordernoenter = orderno!!.text.toString().uppercase(Locale.getDefault())
                            layout.visibility = View.GONE
                            if(ordernoenter.length>0) {
                                if (ordernoenter.contains("/")) {
                                    val result1 = ordernoenter.trim().split("/")
                                    if (result1[0].trim() != "") {
                                        if (result1[1].toIntOrNull() != null) {
                                            boxno = result1[1].toIntOrNull()!!
                                            if (FirstorderNO.trim() == "") {
                                                FirstorderNO = result1[0].trim()
                                            } else {
                                                if (FirstorderNO == result1[0].trim()) {

                                                    if (boxno.toInt() <= totalBoxes) {
                                                        count = 0
                                                        for (i in list) {
                                                            if (i == ordernoenter) {

                                                                layout.visibility = View.VISIBLE
                                                                orderno!!.setText("")
                                                                msg!!.text = "Box Already Scanned."
                                                                AppPreferences.playSoundinvalidalready()
                                                                count = 1
                                                                orderno!!.requestFocus()
                                                                val params =
                                                                    LinearLayout.LayoutParams(
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                                    ).apply {
                                                                        setMargins(0, 70, 0, 70)
                                                                    }
                                                            }
                                                        }
                                                        if (count == 0) {
                                                            maxTextSize = list.size.toString()
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
                                                            list.add(list.size, ordernoenter)
                                                            boxlist.add(0, boxno.toString())
                                                            orderno!!.setText("")
                                                            noofboxes1.text =
                                                                list.size.toString() + " out of " + "" + totalBoxes
                                                            lastscanprd.text = boxlist.toString()
                                                            msg!!.text = ""
                                                            if (list.size.toString() == totalBoxes.toString()) {
                                                                submitorder(FirstorderNO)
                                                            }
                                                        }
                                                    } else {
                                                        layout.visibility = View.VISIBLE
                                                        orderno!!.setText("")
                                                        msg!!.text = "Invalid Box Scanned."
                                                        AppPreferences.playSoundinvalid()
                                                    }
                                                } else {
                                                    // pDialog.dismiss()

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
                                                    )
                                                        .show()
                                                }
                                            }
                                        } else {
                                            layout.visibility = View.VISIBLE
                                            orderno!!.setText("")
                                            msg!!.text = "Invalid box scanned."
                                            AppPreferences.playSoundinvalid()

                                        }
                                    } else {
                                        layout.visibility = View.VISIBLE
                                        orderno!!.setText("")
                                        msg!!.text = "Invalid Box Scanned."
                                        AppPreferences.playSoundinvalid()
                                    }
                                } else {
                                    layout.visibility = View.VISIBLE
                                    orderno!!.setText("")
                                    msg!!.text = "Invalid Box Scanned"
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

                orderno!!.requestFocus()
                txtallpicbox.setOnClickListener {
                    if (AppPreferences.internetConnectionCheck(this.context)) {
                        pDialog.dismiss()
                        PickAllBoxesDailog(
                            "Are you sure you want to pick all boxes?",
                            txtorderno!!.text.toString()
                        )
                    }

                    else {
                        CheckInterNetDailog()
                    }
                }
                val view: ScrollView =binding.scrollView
                view.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                view.isFocusable = false
                view.isFocusableInTouchMode = false
                view.setOnTouchListener { v, event ->
                    v.requestFocusFromTouch()
                    false
                }
            } )
        } else {
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
        val layout = binding.txtmsg
        txtorderno = binding.txtorderNo
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        var accessToken = preferences.getString("accessToken", "")
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNO", orderno1)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", details))
        JSONObj.put("requestContainer", Jsonarra.put("deviceID",
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
        val resorderno = JsonObjectRequest(
            Request.Method.POST, AppPreferences.BASEURL+ AppPreferences.GET_ORDERS, JSONObj,
            { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val presponsmsg = resultobj.getString("responseMessage")
                    if (presponsmsg == "Orders Found") {
                        pDialog.dismiss()
                        val jsondata = resultobj.getJSONArray("responseData")
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this.context)
                        val editor1 = preferences.edit()
                        for (i in 0 until jsondata.length()) {
                            val dorderno = jsondata.getJSONObject(i).getString("OrderNo")
                            val noofboxes = jsondata.getJSONObject(i).getInt("PackedBoxes")
                            val stoppage = jsondata.getJSONObject(i).getInt("Stoppage")
                                if (boxno <= noofboxes) {
                                    cardview.visibility = View.GONE
                                    cardview.visibility = View.VISIBLE
                                    txtstop.text = "${stoppage}"
                                    txtorderno!!.text = "$dorderno"
                                    editor1.putString("OrderNO", dorderno)
                                    editor1.putInt("NoofBox", noofboxes)
                                    editor1.apply()
                                    totalBoxes=noofboxes
                                    list.add(0, barcode)
                                    boxlist.add(0, boxno.toString())
                                    txtpacked.text =
                                        list.size.toString() + " out of " + "${noofboxes}"
                                    txtscanproduct.text = boxlist.toString()
                                    if (list.size.toString() == totalBoxes.toString()) {
                                        submitorder(dorderno)
                                    }
                                    checkr = 1
                                }
                                else {
                                    layout.visibility = View.VISIBLE
                                    msg!!.text = "Invalid box scanned."
                                    FirstorderNO = ""
                                    AppPreferences.playSoundinvalid()
                                }
                        }
                    } else {
                        pDialog.dismiss()
                        val alertorfailed = AlertDialog.Builder(this.context)

                        alertorfailed.setTitle(orderno1)
                        alertorfailed.setCancelable(false)
                        alertorfailed.setMessage(presponsmsg.toString())
                        alertorfailed.setPositiveButton(
                            "ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                clear()

                                orderno!!.text.clear()

                            })
                        FirstorderNO = ""
                        checkr = 0
                        val dialog: AlertDialog = alertorfailed.create()
                        dialog.show()
                        cardview.visibility = View.GONE
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
        JSONObj.put("requestContainer", Jsonarra.put("deviceID",
            Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)))
        JSONObj.put("OrderNo", sorderno)
        val alertsuborder = AlertDialog.Builder(this.context)
        alertsuborder.setCancelable(false)
        val resordernos = JsonObjectRequest(
            Request.Method.POST, AppPreferences.BASEURL+ AppPreferences.SUBMIT_LOAD_ORDER, JSONObj,
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
                        this.findNavController().navigate(com.example.myapplication.R.id.nav_productlist)
                        FirstorderNO = ""
                        checkr = 0
                        val timerout = Timer()
                        timerout.schedule(object : TimerTask() {
                            override fun run() {
                                //dialog.dismiss()
                                timerout.cancel()
                            }
                        }, 2000)

                    }
                    else {
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
    fun clear() {
        val txtorderno: TextView = binding.txtorderNo
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        txtorderno.text = "N/A"
        txtstop.text = "N/A"
        txtscanproduct.text = "N/A"
        txtpacked.text = "0"
    }


    fun CheckInterNetDailog(){
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
            this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
        }
        dialog?.show()
    }
}