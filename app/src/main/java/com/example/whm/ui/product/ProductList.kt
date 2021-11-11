package com.example.myapplication.ui.product

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.myapplication.ui.ProductList.ProductListViewModel
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.databinding.FragmentProductListBinding
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ProductList : Fragment() {

    private  lateinit var  productListViewModel: ProductListViewModel
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    val list: MutableList<String> = ArrayList()
    var FirstorderNO:String=""
    var ordernoenter:String=""
    var checkr=0
    var totalBoxes =0
    var msg:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productListViewModel= ViewModelProvider(this).get(ProductListViewModel::class.java)
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var count =0
        val view=inflater.inflate(com.example.myapplication.R.layout.fragment_product_list,container,false)
        val orderno: EditText = binding.txtorderno
        val noofboxes1: TextView = binding.txtpackedb
        val lastscanprd: TextView = binding.txtscanproduct
        val alert = AlertDialog.Builder(this.context)
         msg = binding.txtmsg
        productListViewModel.text.observe(viewLifecycleOwner, Observer {
            orderno.requestFocus()
            orderno.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
                    pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                    pDialog.titleText = "Fetching ..."
                    pDialog.setCancelable(true)
                    pDialog.show()
                    ordernoenter = orderno.text.toString()
                    if (ordernoenter.contains("/")) {
                        val result1 = ordernoenter.split("/")
                        val boxno = result1[1]
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
                                        orderno.text.clear()
                                        msg!!.text = "Box Already Scanned."
                                        count = 1
                                    }
                                }
                                if (count == 0) {
                                    list.add(list.size, ordernoenter)
                                    orderno.text.clear()
                                    noofboxes1.text =
                                        list.size.toString() + " out of " + "" + totalBoxes
                                    lastscanprd.text = list.toString() + "\n"
//                                    msg!!.text = list.toString()
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
                            alert.setNegativeButton("No")
                            { dialog, which -> dialog.dismiss() }
                            alert.setPositiveButton("Yes")
                            { dialog, which ->
                                list.clear()
                                count = 0
                                orderdetailsbind(result1[0], ordernoenter)
                                FirstorderNO = result1[0]
                                dialog.dismiss()
                            }

                            alert.show()
                            orderno.text.clear()
                        }
                        }
                        if (checkr == 0) {
                            try {
                                pDialog.dismiss()
                                orderdetailsbind(FirstorderNO, ordernoenter)
                                orderno.text.clear()
                            } catch (e: IOException) {
                                Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else {
                        pDialog.dismiss()
                         alert.setMessage("Invalid scanned Order")
                        alert.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alert.create()
                        dialog.show()
                        val orderno1: EditText = binding.txtorderno
                        orderno1.text.clear()
                    }
                         return@OnKeyListener true
                }
                false
            })
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    val APIURL: String = "http://api.a1whm.com/AndroidAPI/WDriverOrder.asmx/getOrders"
    val APISUBMITORDER : String= "https://api.a1whm.com/AndroidAPI/WDriverOrder.asmx/SubmitLoadOrder"

    fun orderdetailsbind(orderno: String,barcode:String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(true)
        pDialog.show()
        val cardview:CardView=binding.cardView2
        //Toast.makeText(this.context, barcoded, Toast.LENGTH_SHORT).show()
        val txtorderno: TextView = binding.txtorderNo
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.25"
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNO", orderno)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
        JSONObj.put("requestContainer", Jsonarra.put("userAutoId", empautoid))
        JSONObj.put("requestContainer",Jsonarra.put("accessToken","a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x"))
        JSONObj.put("requestContainer",Jsonarra.put("filterkeyword",details))
          val resorderno= JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,
            {
                    response ->
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

                }
                else {
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
                            txtorderno.text = "$dorderno"
                            editor1.putString("OrderNO", dorderno)
                            editor1.putInt("NoofBox", noofboxes)
                            editor1.apply()
                            list.add(0, barcode)
                            totalBoxes = noofboxes.toInt()
                            txtpacked.text =
                                list.size.toString() + " out of " + "${noofboxes}"
                            txtscanproduct.text = list.toString()
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
                    }
                }
            }, { response ->
                  Log.e("onError", error(response.toString()))
              })
        try {
            queues.add(resorderno)
        }
        catch (e:IOException){
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }
    }
    fun submitorder(sorderno: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(true)
        pDialog.show()
        val Jsonarra = JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId", "")
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        JSONObj.put("requestContainer",Jsonarra.put("userAutoId",empautoid))
        JSONObj.put("requestContainer",Jsonarra.put("accessToken","a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x"))
        JSONObj.put("OrderNo",sorderno)
        val alertsuborder= AlertDialog.Builder(this.context)
        val resordernos= JsonObjectRequest(Request.Method.POST,APISUBMITORDER,JSONObj,
            {
                    response ->
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
                }
                else {
                    if (rspCode.toString() == "200") {
                        pDialog.dismiss()
                        alertsuborder.setTitle(sorderno.toString().uppercase(Locale.getDefault()))
                        alertsuborder.setMessage(rspMsg.toString())
                        alertsuborder.setPositiveButton(
                            "ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                val orderno3: EditText = binding.txtorderno
                                orderno3.text.clear()
                            })
                        FirstorderNO = ""
                        checkr = 0
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                        list.clear()
                        clear()
                        msg!!.text = ""
                        clear()
                    } else {
                        pDialog.dismiss()
                        alertsuborder.setTitle(sorderno.toString())
                        alertsuborder.setMessage("Order  does not submit!!!")
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                    }
                }
            }, { response -> Log.e("onError", error(response.toString())) })
        try {
            queues.add(resordernos)
        } catch (e: IOException) {
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }
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
}