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
import org.json.JSONArray
import android.content.SharedPreferences
import com.example.myapplication.arraylist
import org.w3c.dom.Text


class ProductList : Fragment() {

    private  lateinit var  productListViewModel: ProductListViewModel
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    val list: MutableList<String> = ArrayList()
    var FirstorderNO:String=""
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

            orderno.requestFocus();
            orderno.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->

                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {

                    val ordernoenter = orderno.text.toString()
                      val noofboxes2=noofboxes1.text.toString()
                    val result1 = ordernoenter.split("/")


                    val  boxno = result1[1]


                    if(FirstorderNO=="") {
                        FirstorderNO = result1[0]

                    }
                    else
                    {
                        if(FirstorderNO==result1[0])
                        {
                            if(boxno.toInt()<=totalBoxes)
                            {

                                var count =0
                                for(i in list) {
                                    if (i.toString() == ordernoenter) {
                                        orderno.getText().clear()
                                        msg!!.text="Box Already Scanned."
//                                       // Toast.makeText(
//                                            this.context,
//                                            "Box Already Scanned.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        count=1
                                    }
                                }
                                    if(count==0){
                                            list.add(list.size, ordernoenter)
                                            orderno.getText().clear()
                                            noofboxes1.text =  list.size.toString() + " out of "+ ""+totalBoxes
                                            lastscanprd.text= list.size.toString()
                                            msg!!.text= list.toString()
                                        if(list.size.toString() == totalBoxes.toString()){
                                            submitorder(orderno.toString())
                                        }
                                    }
                                }


                            }
                            else
                            {
                                msg!!.text="invalid box number"
//                                Toast.makeText(this.context, "invalid box number", Toast.LENGTH_SHORT).show()
                            }

//                        }else
//                        {
//                            orderno.getText().clear()
//                            Toast.makeText(this.context, "are you sure ?you want to scan another order", Toast.LENGTH_SHORT).show()
//                        }
                    }


                    if(checkr==0) {
                        try {
                            orderdetailsbind(FirstorderNO, ordernoenter)
                            orderno.getText().clear()
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
        val txtorderno: TextView = binding.txtorderNo  as TextView
        val txtstop: TextView = binding.txtstoppage  as TextView
        val txtscanproduct: TextView = binding.txtscanproduct  as TextView
        val txtpacked: TextView = binding.txtpackedb  as TextView
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.16"
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
                val  presponsmsg = resultobj.getString("responseMessage");
//                Toast.makeText(context, presponsmsg.toString(), Toast.LENGTH_LONG).show()
                if(presponsmsg=="Orders Found") {

                    val jsondata = resultobj.getJSONArray("responseData")
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
                    val editor1 = preferences.edit()
                    for (i in 0 until  jsondata.length()){
                        val dorderno = jsondata.getJSONObject(i).getString("OrderNo")
                        val noofboxes =jsondata.getJSONObject(i).getInt("PackedBoxes")
                        val stoppage = jsondata.getJSONObject(i).getInt("Stoppage")
                        txtstop.text = "${stoppage?.toInt()}"

                        txtorderno.text = "${dorderno?.toString()}"

                        editor1.putString("OrderNO", dorderno)
                        editor1.putInt("NoofBox", noofboxes.toInt())

                        editor1.apply()
                        list.add(0, barcode)
                        totalBoxes = noofboxes?.toInt()
                        txtpacked.text = list.size.toString()+ " out of "+ "${noofboxes?.toInt()}"
                        txtscanproduct.text=list.size.toString()
                       // Toast.makeText(context,  list.toString() , Toast.LENGTH_LONG).show()
                    //
                        msg!!.text=list.toString()


                    }
//                    var pempautoid = preferences.getInt("HNOofBox",0)
//                    Toast.makeText(context, ""+pempautoid, Toast.LENGTH_LONG).show()
                    checkr=1
                }

                else{


                    val alertemail= AlertDialog.Builder(this.context)
                    alertemail.setTitle("Order No")

                    alertemail.setMessage("Order No does not exist")
                    alertemail.setPositiveButton("ok",null)
                    val dialog: AlertDialog =alertemail.create()
                    dialog.show()
                    val orderno: EditText = binding.txtorderno
                    orderno.getText().clear()

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
    fun submitorder(orderno: String) {
        //Toast.makeText(this.context, barcoded, Toast.LENGTH_SHORT).show()
        val txtorderno: TextView = binding.txtorderNo  as TextView

        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val appversion = "1.1.0.16"
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var empautoid = preferences.getString("EmpAutoId","");
        val queues = Volley.newRequestQueue(this.context)
        details.put("OrderNo",orderno)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        JSONObj.put("requestContainer",Jsonarra.put("userAutoId",empautoid))
        JSONObj.put("requestContainer",Jsonarra.put("accessToken","a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x"))
        JSONObj.put("requestContainer",Jsonarra.put("filterkeyword",details))


        val resorderno= JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,
            {
                    response -> val resobj=(response.toString())

                val  responsemsg = JSONObject(resobj.toString());

                val  resultobj = JSONObject(responsemsg.getString("d"));
                val  presponsmsg = resultobj.getString("responseMessage");
//                Toast.makeText(context, presponsmsg.toString(), Toast.LENGTH_LONG).show()
                if(presponsmsg=="Order loaded successfully") {


                    val alertemail= AlertDialog.Builder(this.context)
                    alertemail.setTitle("Order Submit"+binding.txtorderno)

                    alertemail.setMessage("Order  load successfully")
                    alertemail.setPositiveButton("ok",null)
                    val dialog: AlertDialog =alertemail.create()
                    dialog.show()
                    val orderno: EditText = binding.txtorderno
                    orderno.getText().clear()

                }

                else{


                    val alertemail= AlertDialog.Builder(this.context)
                    alertemail.setTitle("Order Submit")

                    alertemail.setMessage("Order  does not submit")
                    alertemail.setPositiveButton("ok",null)
                    val dialog: AlertDialog =alertemail.create()
                    dialog.show()
                    val orderno: EditText = binding.txtorderno
                    orderno.getText().clear()

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
}