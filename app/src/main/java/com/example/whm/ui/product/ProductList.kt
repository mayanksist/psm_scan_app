package com.example.myapplication.ui.product

import android.R.attr
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.android.volley.toolbox.Volley
import com.example.myapplication.apisettings
import com.example.myapplication.com.example.myapplication.ui.ProductList.ProductListViewModel
import com.example.myapplication.databinding.FragmentProductListBinding
import org.json.JSONObject
import java.io.IOException
import android.preference.PreferenceManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import android.R.string
import android.text.TextUtils.split
import android.R.attr.data
import android.content.DialogInterface
import org.json.JSONArray
import android.content.SharedPreferences
import com.example.myapplication.arraylist
import org.w3c.dom.Text
import com.example.myapplication.MainActivity

import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.R


class ProductList : Fragment() {

    private  lateinit var  productListViewModel: ProductListViewModel
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    val list: MutableList<String> = ArrayList()
    var FirstorderNO:String=""
    var ordernoenter:String=""
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
        var count =0
        val view=inflater.inflate(com.example.myapplication.R.layout.fragment_product_list,container,false)
        val orderno: EditText = binding.txtorderno
        val noofboxes1: TextView = binding.txtpackedb
        val lastscanprd: TextView = binding.txtscanproduct
        val alert = AlertDialog.Builder(this.context)
         msg = binding.txtmsg
        productListViewModel.text.observe(viewLifecycleOwner, Observer {
            orderno.requestFocus();
            orderno.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    ordernoenter = orderno.text.toString()
                    if (ordernoenter.contains("/")) {
                    val noofboxes2 = noofboxes1.text.toString()
                    val result1 = ordernoenter.split("/")
                    val boxno = result1[1]
                    if (FirstorderNO == "") {
                        FirstorderNO = result1[0]
                    } else {
                        if (FirstorderNO == result1[0]) {
                            if (boxno.toInt() <= totalBoxes) {
                                count = 0
                                for (i in list) {
                                    if (i.toString() == ordernoenter) {
                                        orderno.getText().clear()
                                        msg!!.text = "Box Already Scanned."
                                        count = 1
                                    }
                                }
                                if (count == 0) {
                                    list.add(list.size, ordernoenter)
                                    orderno.getText().clear()
                                    noofboxes1.text =
                                        list.size.toString() + " out of " + "" + totalBoxes
                                    lastscanprd.text = list.toString() + "\n"
                                    msg!!.text = list.toString()
                                    if (list.size.toString() == totalBoxes.toString()) {
                                        submitorder(FirstorderNO.toString())
                                    }
                                }
                            } else {
                                msg!!.text = "invalid box number"
                            }
                        } else {
                            alert.setTitle(result1[0])
                            alert.setMessage("Are you sure you want  to skip current order " + FirstorderNO + "?")
                            alert.setPositiveButton("Yes") { dialog, which ->
                                list.clear()
                                count = 0
                                orderdetailsbind(result1[0], ordernoenter)
                                FirstorderNO = result1[0]
                                dialog.dismiss()
                            }
                            alert.setNegativeButton(
                                "No"
                            ) { dialog, which -> dialog.dismiss() }
                            alert.show()
                            orderno.getText().clear()
                        }
                    }
                    if (checkr == 0) {


                        try {

                            orderdetailsbind(FirstorderNO, ordernoenter)
                            orderno.getText().clear()

                        } catch (e: IOException) {
                            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
                    else{
                        alert.setTitle(orderno.toString().toUpperCase())
                        alert.setMessage("Invalid Order")
                        alert.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alert.create()
                        dialog.show()
                        val orderno1: EditText = binding.txtorderno
                        orderno1.getText().clear()
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
        val txtorderno: TextView = binding.txtorderNo  as TextView
        val txtstop: TextView = binding.txtstoppage  as TextView
        val txtscanproduct: TextView = binding.txtscanproduct  as TextView
        val txtpacked: TextView = binding.txtpackedb  as TextView
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.25"
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId","");
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNO",orderno)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        JSONObj.put("requestContainer",Jsonarra.put("userAutoId",empautoid))
        JSONObj.put("requestContainer",Jsonarra.put("accessToken","a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x"))
        JSONObj.put("requestContainer",Jsonarra.put("filterkeyword",details))
          val resorderno= JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,
            {
                    response -> val resobj=(response.toString())
                val  responsemsg = JSONObject(resobj.toString());
                val  resultobj = JSONObject(responsemsg.getString("d"));
                val  resmsg = resultobj.getString("response");
                val  presponsmsg = resultobj.getString("responseMessage");
                  if(resmsg=="failed") {
                    val alertorfailed=AlertDialog.Builder(this.context)
                    alertorfailed.setTitle(orderno)
                    alertorfailed.setMessage(presponsmsg.toString())
                    alertorfailed.setPositiveButton("ok",DialogInterface.OnClickListener { dialog, which ->
                        val orderno4: EditText = binding.txtorderno
                        orderno4.getText().clear()
                    })
                      FirstorderNO =""
                    val dialog:AlertDialog=alertorfailed.create()
                    dialog.show()
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
                            txtstop.text = "${stoppage?.toInt()}"
                            txtorderno.text = "${dorderno?.toString()}"
                            editor1.putString("OrderNO", dorderno)
                            editor1.putInt("NoofBox", noofboxes.toInt())
                            editor1.apply()
                            list.add(0, barcode)
                            totalBoxes = noofboxes?.toInt()
                            txtpacked.text =
                                list.size.toString() + " out of " + "${noofboxes?.toInt()}"
                            txtscanproduct.text = list.toString()
                            if (list.size.toString() == totalBoxes.toString()) {
                                submitorder(dorderno)
                            }
                            msg!!.text = list.toString()
                        }
                        checkr = 1
                    } else {
                        val alertscanord = AlertDialog.Builder(this.context)
                        alertscanord.setTitle(orderno.toString().toUpperCase())
                        alertscanord.setMessage("Order No does not exist")
                        alertscanord.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertscanord.create()
                        dialog.show()
                        val orderno1: EditText = binding.txtorderno
                        orderno1.getText().clear()
                    }
                }

            }, {
                    response ->
                Log.e("onError", error(response.toString()));
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
        val appversion = "1.1.0.25"
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId","");
        val queues = Volley.newRequestQueue(this.context)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        JSONObj.put("requestContainer",Jsonarra.put("userAutoId",empautoid))
        JSONObj.put("requestContainer",Jsonarra.put("accessToken","a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x"))
        JSONObj.put("OrderNo",sorderno)
        val alertsuborder= AlertDialog.Builder(this.context)
        val resordernos= JsonObjectRequest(Request.Method.POST,APISUBMITORDER,JSONObj,
            {
                    response -> val resobj=(response.toString())

                val  responsemsg = JSONObject(resobj.toString());

                val  resultobj = JSONObject(responsemsg.getString("d"));
                val  rspCode = resultobj.getString("responseCode");
                val  rspMsg = resultobj.getString("responseMessage");
                if(rspCode=="202") {
                    alertsuborder.setTitle(sorderno.toString().toUpperCase())
                    alertsuborder.setMessage(rspMsg.toString())
                    alertsuborder.setNegativeButton(
                        "ok"
                    ) { dialog, which ->
                        val orderno2: EditText = binding.txtorderno
                        orderno2.getText().clear()
                         }
                    val dialog:AlertDialog=alertsuborder.create()
                    dialog.show()
                }
                else {
                    if (rspCode.toString() == "200") {
                        alertsuborder.setTitle(sorderno.toString().toUpperCase())
                        alertsuborder.setMessage(rspMsg.toString())
                        alertsuborder.setPositiveButton("ok",DialogInterface.OnClickListener { dialog, which ->
                            val orderno3: EditText = binding.txtorderno
                            orderno3.getText().clear()
                        })
                        FirstorderNO =""
                        checkr=0
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()
                        list.clear()
                        clear()
                        msg!!.text = ""
                        clear()
                    }
                    else {
                        alertsuborder.setTitle(sorderno.toString())
                        alertsuborder.setMessage("Order  does not submit!!!")
                        alertsuborder.setPositiveButton("ok", null)
                        val dialog: AlertDialog = alertsuborder.create()
                        dialog.show()

                    }
                }
            }, {
                    response ->
                Log.e("onError", error(response.toString()));
            })
        try {
            queues.add(resordernos)
        }
        catch (e:IOException){
            Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
        }
    }

   fun clear(){
        val txtorderno: TextView = binding.txtorderNo  as TextView
        val txtstop: TextView = binding.txtstoppage  as TextView
        val txtscanproduct: TextView = binding.txtscanproduct  as TextView
        val txtpacked: TextView = binding.txtpackedb  as TextView
        txtorderno.setText("N/A")
        txtstop.setText("N/A")
        txtscanproduct.setText("N/A")
        txtpacked.setText("0")
    }
}