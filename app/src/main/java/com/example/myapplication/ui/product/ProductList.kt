package com.example.myapplication.ui.product

import android.app.AlertDialog
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.myapplication.ui.ProductList.ProductListViewModel
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
//    var msg: TextView? =null
    var checkr=0
    var present=0
    var totalBoxes =0
    var msg:TextView?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        productListViewModel= ViewModelProvider(this).get(ProductListViewModel::class.java)
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val view=inflater.inflate(com.example.myapplication.R.layout.fragment_product_list,container,false)

        val orderno: EditText = binding.txtorderno
        val noofboxes1: TextView = binding.txtpackedb
        val lastscanprd: TextView = binding.txtscanproduct
         msg = binding.txtmsg

        productListViewModel.text.observe(viewLifecycleOwner, Observer {

            orderno.requestFocus()
            orderno.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->

                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {

                    ordernoenter = orderno.text.toString()
                    val noofboxes2 = noofboxes1.text.toString()
                    val result1 = ordernoenter.split("/")


                    val boxno = result1[1]

                    if(FirstorderNO=="") {
                        FirstorderNO = result1[0]

                    }
                    else
                    {
                        if(FirstorderNO==result1[0]) {

                                if (boxno.toInt() <= totalBoxes) {

                                    var count = 0
                                    for (i in list) {
                                        if (i.toString() == ordernoenter) {
                                            orderno.text.clear()
                                            msg!!.text = "Box Already Scanned."
//                                       // Toast.makeText(
//                                            this.context,
//                                            "Box Already Scanned.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                            count = 1
                                        }
                                    }

                                    if (count == 0) {
                                        list.add(list.size, ordernoenter)
                                        orderno.text.clear()
                                        noofboxes1.text =
                                            list.size.toString() + " out of " + "" + totalBoxes
                                        lastscanprd.text = list.toString() + "\n"
                                        msg!!.text = list.toString()
                                        if (list.size.toString() == totalBoxes.toString()) {
                                            submitorder(FirstorderNO.toString())
//                                            Toast.makeText(this.context, FirstorderNO.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    }


                                }

                              else{
                                    msg!!.text = "invalid box number"
                              }

                        }
                            else
                            {
                                val alertorfailed=AlertDialog.Builder(this.context)
                                alertorfailed.setTitle(result1[0].toString())
                                alertorfailed.setMessage("You want to skip current order " +FirstorderNO + "?")
                                alertorfailed.setPositiveButton("No",null)
                                alertorfailed.setNegativeButton("yes",null)
                                val dialog:AlertDialog=alertorfailed.create()
                                dialog.show()
                                orderno.text.clear()
//                                Toast.makeText(
//                                    this.context,
//                                    "Are you sure? You want to scan another order",
//                                    Toast.LENGTH_SHORT).show()

//                                Toast.makeText(this.context, "Invalid box number", Toast.LENGTH_SHORT).show()
                            }

                    }


                    if(checkr==0) {
                        try {

                            orderdetailsbind(FirstorderNO, ordernoenter)
                            orderno.text.clear()

                        } catch (e: IOException) {
                            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()

                        }
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
        //Toast.makeText(this.context, barcoded, Toast.LENGTH_SHORT).show()
        val txtorderno: TextView = binding.txtorderNo
        val txtstop: TextView = binding.txtstoppage
        val txtscanproduct: TextView = binding.txtscanproduct
        val txtpacked: TextView = binding.txtpackedb
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.16"
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
//                Toast.makeText(context, presponsmsg.toString(), Toast.LENGTH_LONG).show()
                if (resmsg == "failed") {
                    val alertorfailed = AlertDialog.Builder(this.context)
                    alertorfailed.setTitle(orderno.toString().uppercase(Locale.getDefault()))
                    alertorfailed.setMessage(presponsmsg.toString())
                    alertorfailed.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alertorfailed.create()
                    dialog.show()
                    // Toast.makeText(this@MainActivity, "", Toast.LENGTH_LONG).show()
                }
                else {
                    if (presponsmsg == "Orders Found") {

                        val jsondata = resultobj.getJSONArray("responseData")
                        val preferences =
                            PreferenceManager.getDefaultSharedPreferences(this.context)
                        val editor1 = preferences.edit()
                        for (i in 0 until jsondata.length()) {
                            val dorderno = jsondata.getJSONObject(i).getString("OrderNo")
                            val noofboxes = jsondata.getJSONObject(i).getInt("PackedBoxes")
                            val stoppage = jsondata.getJSONObject(i).getInt("Stoppage")
                            txtstop.text = "${stoppage.toInt()}"
                            txtorderno.text = "$dorderno"
                            editor1.putString("OrderNO", dorderno)
                            editor1.putInt("NoofBox", noofboxes.toInt())
                            editor1.apply()
                            list.add(0, barcode)
                            totalBoxes = noofboxes.toInt()
                            txtpacked.text =
                                list.size.toString() + " out of " + "${noofboxes.toInt()}"
                            txtscanproduct.text = list.toString()
//                             Toast.makeText(context,  list.toString() , Toast.LENGTH_LONG).show()
                            if (list.size.toString() == totalBoxes.toString()) {
                                submitorder(dorderno)
                                Toast.makeText(
                                    this.context,
                                    FirstorderNO.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            msg!!.text = list.toString()


                        }
//                    var pempautoid = preferences.getInt("HNOofBox",0)
//                    Toast.makeText(context, ""+pempautoid, Toast.LENGTH_LONG).show()
                        checkr = 1
                    } else {

                        val alertscanord = AlertDialog.Builder(this.context)

                        alertscanord.setTitle(orderno.toString().uppercase(Locale.getDefault()))

                        alertscanord.setMessage("Order No does not exist")
                        alertscanord.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertscanord.create()
                        dialog.show()
                        val orderno: EditText = binding.txtorderno
                        orderno.text.clear()

                    }
                }

            }, {
                    response ->
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

        val Jsonarra = JSONObject()

        val JSONObj = JSONObject()
        val appversion = "1.1.0.16"
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
//                Toast.makeText(context, resultobj.toString(), Toast.LENGTH_LONG).show()
                val rspCode = resultobj.getString("responseCode")
                val rspMsg = resultobj.getString("responseMessage")
//                val  resmsgso = resultobj.getString("response");
                if (rspCode == "202") {

                    alertsuborder.setTitle(sorderno.toString().uppercase(Locale.getDefault()))
                    alertsuborder.setMessage(rspMsg.toString())
                    alertsuborder.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alertsuborder.create()
                    dialog.show()
                    // Toast.makeText(this@MainActivity, "", Toast.LENGTH_LONG).show()
                }

                else {
                    if (rspCode.toString() == "200") {

                        alertsuborder.setTitle(sorderno.toString().uppercase(Locale.getDefault()))

                        alertsuborder.setMessage(rspMsg.toString())
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                        // Toast.makeText(context, rspMsg.toString(), Toast.LENGTH_LONG).show()
                        msg!!.text = ""
                        clear()

                    } else {


                        alertsuborder.setTitle(sorderno.toString())

                        alertsuborder.setMessage("Order  does not submit!!!")
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
//                    val sorderno: EditText = binding.txtorderno
//                    sorderno.getText().clear()

                    }
                }

            }, {
                    response ->
                Log.e("onError", error(response.toString()))
            })
        try {
            queues.add(resordernos)
        }
        catch (e:IOException){
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