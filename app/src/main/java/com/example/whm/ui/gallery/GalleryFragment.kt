package com.example.myapplication.com.example.whm.ui.gallery

import android.R
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.myapplication.apisettings
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.databinding.FragmentGalleryBinding
import org.json.JSONObject
import java.io.IOException


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
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
        root.requestFocus();
        root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
            }
            true
        })
        if(AppPreferences.internetConnectionCheck(this.context)) {
            val barcode: EditText = binding.barcodetype
            galleryViewModel.text.observe(viewLifecycleOwner, Observer {
                barcode.requestFocus()
                barcode.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        var barcodeenter = barcode.text.toString()
                        try {
                            if (barcodeenter.trim().isEmpty()) {
                                val alertemail = AlertDialog.Builder(this.context)
                                alertemail.setMessage("Scan Barcode")
                                alertemail.setPositiveButton("ok")
                                { dialog, which -> dialog.dismiss()
                                    barcode.text.clear()
                                    barcode.setText("")
                                    barcodeenter=""
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
                    false
                })
            })
        }
        else{
             val alertnet = AlertDialog.Builder(activity)
             alertnet.setTitle("Connection")
             alertnet.setMessage("Please check your internet connection")
             alertnet.setPositiveButton("ok")
             { dialog, which -> dialog.dismiss()
                 this.findNavController().navigate(com.example.myapplication.R.id.nav_home)
             }
             val dialog: AlertDialog = alertnet.create()
             dialog.show()
        }
        return root
    }
    val APIURL: String =apisettings().apiurl+"WPackerProductList.asmx/getProductsList"
    fun bindproductdetails(barcoded: String) {
        val pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Fetching ..."
        pDialog.setCancelable(true)
        pDialog.show()
        val produname: TextView = binding.productname
        val productId: TextView = binding.txtProductId
        val unitype: TextView = binding.txtunitype
        val price: TextView = binding.priductprise
        val imagur: ImageView = binding.productimage
        val stock: TextView = binding.txtStock2
        val category: TextView = binding.txtCategory
        val sub_category: TextView = binding.txtSubCategory
        val locationval: TextView = binding.txtLocation
        val barcode: TextView = binding.txtBarScanned
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
            Response.Listener {
                    response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj.toString())
                val resultobj = JSONObject(responsemsg.getString("d"))
                val presponsmsg = resultobj.getString("responseMessage")
                if (presponsmsg == "Products Found") {
                    layout.visibility = View.GONE
                    layout.visibility = View.VISIBLE
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepd = JSONObject(jsondata.toString())
                    val pname = jsonrepd.getString("PName")
                    val pCategory = jsonrepd.getString("Cat")
                    val pSubCategory = jsonrepd.getString("SCat")
                    val ProductId = jsonrepd.getString("PId")
                    val punitypa = jsonrepd.getString("UnitType")
                    val uAutoId = jsonrepd.getString("UAutoId")
                    val pqty = jsonrepd.getInt("Qty")
                    val pprice = ("%.2f".format(jsonrepd.getDouble("Price")))
                    val cstock = jsonrepd.getInt("CurrentStock")
                    val DefaultStock = jsonrepd.getInt("Stock")
                    var imagesurl = ""
                    if (jsonrepd.getString("OPath") == null) {
                        imagesurl = jsonrepd.getString("ImageUrl")
                    } else {
                        imagesurl = jsonrepd.getString("OPath")
                    }
                    val location = jsonrepd.getString("Location")
                    produname.text = "$pname"
                    productId.text = "$ProductId"
                    if (uAutoId.toString() == "3") {
                        unitype.text = "Pieces"
                    } else {
                        unitype.text = "$punitypa" + " (" + "${pqty}" + " Pieces)"
                    }
                    price.text = "${pprice}"
                    if ("$location" == "---"){
                        locationval.text = "N/A"
                    }else{
                        locationval.text = "$location"
                    }

                    category.text = "$pCategory"
                    sub_category.text = "$pSubCategory"
                    if (uAutoId.toString() == "3") {
                        stock.text = "${cstock}" + " $punitypa"
                    } else {
                        stock.text = "${cstock}" + " $punitypa" +" ["+"${DefaultStock}" + " Pieces]"
                    }
                    barcode.text = ""+barcoded
                    Glide.with(this)
                        .load(imagesurl) // image url
//                        .placeholder(R.drawable.ic_menu_report_image) // any placeholder to load at start
//                        .error(R.drawable.ic_menu_report_image)  // any image in case of error
                        .override( 200, 200) // resizing
                        .centerCrop()
                        .into(imagur)
                    pDialog.dismiss()
                }
                else{
                    pDialog.dismiss()
                    AppPreferences.playSoundbarcode()
                    layout.visibility = View.GONE
                    val barcodeC: EditText = binding.barcodetype
                    barcode.text = ""
                    barcodeC.text.clear()
                    barcode.setText("")
                    val alertemail= AlertDialog.Builder(this.context)
                    alertemail.setTitle("Barcode")
                    alertemail.setMessage(presponsmsg.toString())
                    alertemail.setPositiveButton("ok")
                    { dialog, which -> dialog.dismiss()
                        barcode.text = ""
                        barcodeC.text.clear()
                        barcode.setText("")
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

}


