package com.example.myapplication.com.example.whm.ui.gallery

import android.R
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.databinding.FragmentGalleryBinding
import org.json.JSONObject
import java.io.IOException
import android.view.MenuInflater
import android.widget.GridLayout.Spec
import org.json.JSONArray
import android.widget.Toast

import org.json.JSONException

import android.widget.ArrayAdapter

import android.widget.GridView











class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    var productId:TextView?=null
    var Gridlauyoutstock:GridLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(
            com.example.myapplication.R.layout.fragment_gallery,
            container,
            false
        )
        root.requestFocus()
        root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
            }
            true
        })
        setHasOptionsMenu(true)

        if (AppPreferences.internetConnectionCheck(this.context)) {
            val barcode: EditText = binding.barcodetype
            val editicon:ImageView=binding.txtedit


           editicon.setOnClickListener(View.OnClickListener {
               val productdetils = binding.producdetails
               val editlayout = binding.editlayout
               productdetils.visibility = View.GONE
               editlayout.visibility = View.VISIBLE

           })

            galleryViewModel.text.observe(viewLifecycleOwner, Observer {
                barcode.requestFocus()
                barcode.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        if (AppPreferences.internetConnectionCheck(this.context)) {
                            var barcodeenter = barcode.text.toString()
                            try {
                                if (barcodeenter.trim().isEmpty()) {
                                    val alertemail = AlertDialog.Builder(this.context)
                                    alertemail.setMessage("Scan Barcode")
                                    alertemail.setPositiveButton("ok")
                                    { dialog, which ->
                                        dialog.dismiss()
                                        barcode.text.clear()
                                        barcode.setText("")
                                        barcodeenter = ""
                                    }
                                    val dialog: AlertDialog = alertemail.create()
                                    dialog.show()
                                } else {
                                    bindproductdetails(barcodeenter)
                                    barcode.text.clear()
                                }

                            } catch (e: IOException) {
                                Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                            }
                            return@OnKeyListener true
                        }
                        else{
                            CheckInterNetDailog()
                        }
                    }
                    false
                })
            })
        } else {
            CheckInterNetDailog()
        }
        return root
    }


    val APIURL: String = AppPreferences.apiurl + "WPackerProductList.asmx/getProductsList"
    fun bindproductdetails(barcoded: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(true)
        pDialog.show()
        val produname: TextView = binding.productname
         productId = binding.txtProductId
        val unitype: TextView = binding.txtunitype
        val price: TextView = binding.priductprise
        val imagur: ImageView = binding.productimage
        val stock: TextView = binding.txtStock2
        val category: TextView = binding.txtCategory
        val sub_category: TextView = binding.txtSubCategory
        val locationval: TextView = binding.txtLocation
        val barcode: TextView = binding.txtBarScanned

        val reordermark: TextView = binding.txtreordmark
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val layout = binding.showproductdetails
        val queues = Volley.newRequestQueue(this.context)
        details.put("barcode", barcoded)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var accessToken = preferences.getString("accessToken", "")
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", details))
        val reqPRODUCTDETAILS = JsonObjectRequest(
            Request.Method.POST, APIURL, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val presponsmsg = resultobj.getString("responseMessage")
                if (presponsmsg == "Products Found") {
                    layout.visibility = View.GONE
                    layout.visibility = View.VISIBLE
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepd = JSONObject(jsondata.toString())
                    val ProductId = jsonrepd.getString("PId")
                    val pname = jsonrepd.getString("PName")
                    val pCategory = jsonrepd.getString("Cat")
                    val pSubCategory = jsonrepd.getString("SCat")
                    val punitypa = jsonrepd.getString("Unit")
                    val Reordermark = jsonrepd.getString("ROM")
                    val bc = jsonrepd.getString("bc")
                    val pprice = ("%.2f".format(jsonrepd.getDouble("Price")))
                    val DefaultStock = jsonrepd.getString("stock")
                    var imagesurl = ""
                    if (jsonrepd.getString("OPath") == null) {
                        imagesurl = jsonrepd.getString("ImageUrl")
                    } else {
                        imagesurl = jsonrepd.getString("OPath")
                    }
                    val location = jsonrepd.getString("Location")
                    produname.text = "$pname"
                    productId!!.text = "$ProductId"
                    unitype.text = "$punitypa"
                    reordermark.text="$Reordermark"
                    price.text = "${pprice}"
                    if ("$location" == "---") {
                        locationval.text = "N/A"
                    } else {
                        locationval.text = "$location"
                    }
                    category.text = "$pCategory"
                    sub_category.text = "$pSubCategory"
                       stock.text = "${DefaultStock}"
                    barcode.text = "" + bc
                    Glide.with(this)
                        .load(imagesurl)
                        .into(imagur)
                    pDialog.dismiss()
                } else {
                    pDialog.dismiss()
                    AppPreferences.playSoundbarcode()
                    layout.visibility = View.GONE
                    val barcodeC: EditText = binding.barcodetype
                    barcode.text = ""
                    barcodeC.text.clear()
                    barcode.text = ""
                    val alertemail = AlertDialog.Builder(this.context)
                    alertemail.setTitle("Barcode")
                    alertemail.setMessage(presponsmsg.toString())
                    alertemail.setPositiveButton("ok")
                    { dialog, which ->
                        dialog.dismiss()
                        barcode.text = ""
                        barcodeC.text.clear()
                        barcode.text = ""
                    }
                    val dialog: AlertDialog = alertemail.create()
                    dialog.show()
                }
            }, Response.ErrorListener { response ->
                pDialog.dismiss()
                Log.e("onError", error(response.toString()))
            })

        reqPRODUCTDETAILS.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(reqPRODUCTDETAILS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun CheckInterNetDailog(){
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.myapplication.R.menu.main_activity2, menu);
        super.onCreateOptionsMenu(menu, inflater)
        var editicone = menu.findItem(com.example.myapplication.R.id.editproduct)

        if(editicone!=null) {
            editicone.setVisible(true)
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            com.example.myapplication.R.id.editproduct -> {
                if(productId!=null) {
                val productdetils = binding.producdetails
                val editlayout = binding.editlayout
                productdetils.visibility = View.GONE
                editlayout.visibility = View.VISIBLE
                    Gridlauyoutstock=binding.stockupdate


                var txtunittype:TextView=binding.txtunitbox
                var txtunitqrty:TextView=binding.txtunitqty
                val Jsonarra = JSONObject()
                val detailstock = JSONObject()
                val JSONObjs = JSONObject()
                val layout = binding.showproductdetails
                    val queues = Volley.newRequestQueue(this.context)
                    detailstock.put("productId", productId!!.text)
                    JSONObjs.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
                    val preferencesaccess = PreferenceManager.getDefaultSharedPreferences(context)
                    var accessTokenS = preferencesaccess.getString("accessToken", "")
                    JSONObjs.put(
                        "requestContainer",
                        Jsonarra.put("accessToken", accessTokenS)
                    )
                    JSONObjs.put("requestContainer", Jsonarra.put("filterkeyword", detailstock))
                    val requpdatestock = JsonObjectRequest(
                        Request.Method.POST, AppPreferences.apiurl + AppPreferences.GET_Packing_details, JSONObjs,
                        Response.Listener { responsesmsg ->
                            val resobjs = (responsesmsg.toString())
                            val responsemsgs = JSONObject(resobjs)

                            Gridlauyoutstock!!.visibility = View.GONE
                            Gridlauyoutstock!!.visibility = View.VISIBLE
                            val resultobjs = JSONObject(responsemsgs.getString("d"))
                            val presponsmsgs = resultobjs.getString("responseCode")
                            if (presponsmsgs == "201") {
                                val jsondatas = resultobjs.getString("responseData")
                                val jsonrepdu = JSONObject(jsondatas.toString())
                                val ProductId = jsonrepdu.getString("PId")

                                val unitlist = jsonrepdu.getJSONArray("UList")
                                for (i in 0 until unitlist.length()) {

                                    var unittype= unitlist.getJSONObject(i).getString("UName")
                                    var Qty= unitlist.getJSONObject(i).getInt("Qty")
                                    txtunittype.text = "$unittype"
                                    txtunitqrty.text= Qty.toString()

                                }
                            }
                            else{
                                Toast.makeText(this.context, "Server Error", Toast.LENGTH_LONG).show()
                            }
                        }, Response.ErrorListener { response ->

                            Log.e("onError", error(response.toString()))
                        })


                    queues.add(requpdatestock)
                }
                else{
                    Toast.makeText(this.context, "Scan Barcode", Toast.LENGTH_LONG).show()
                }



                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    }




