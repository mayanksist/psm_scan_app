package com.example.myapplication.com.example.whm.ui.gallery

import android.R
import android.R.id
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
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.ui.product.setSupportActionBar
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.app.Dialog
import android.text.SpannableString
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.example.myapplication.com.example.whm.MainActivity
import android.graphics.Typeface

import android.text.style.StyleSpan
import android.util.TypedValue


class GalleryFragment : Fragment() {
    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    var productId:TextView?=null
    var Gridlauyoutstock:RelativeLayout? = null
    var Layoutbindunit:LinearLayout? = null
    var Layoutdefault:LinearLayout? = null
    var editlayout:ConstraintLayout?= null
    var showproductdetails:ConstraintLayout?=null
    var producdetails:ConstraintLayout?=null
    var menu: MenuItem? = null
    var   backinvetory: MenuItem? = null
    var txtunitpu: EditText? = null
    var txtunitbu: EditText? = null
    var txtunitCu: EditText? = null
    var  txtunitqtyC: EditText? = null
    var  txtunitqtyB: EditText? = null
    var  txtunitqtyP: EditText? = null
    var  txttotalqty: EditText? = null
    var  txtunitqtyPi: Int? = 0
    var  txtunitqtyBox: Int? = 0
    var  txtunitqtyCase: Int? =0
    var  totalunitPqty: Int? =0
    var  totalunitBqty: Int? =0
    var  totalunitCqty: Int? =0
    var  totalunitqty: Int? =0
    var  totalstock: Int? =0
    var  txtdefaultqty: EditText? = null
    var  unitB: Int? =0
    var  unitC: Int? =0
    var  unitP: Int? =0
    var TxtRemark: EditText?=null
    var barcode: EditText?=null
    var TotalStockQTY: EditText?=null
    var ProductID_S: TextView?=null
    var TxttotalStock: TextView?=null
    var UnitChengeBox: EditText?=null
    var UnitChengeP: EditText?=null
    var UnitChengease: EditText?=null
    var  barcodeenter:String?=""
    var DUnit:Int=0
    var txtunitP: TextView?=null
    var txtunitB: TextView?=null
    var txtunitC: TextView?=null

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
             barcode = binding.barcodetype
            val imm =
                (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view?.windowToken,0)
            val btnupdatestock: Button = binding.btnupdatestock
             UnitChengeBox = binding.txtunitB
             UnitChengeP = binding.txtunitP
             UnitChengease = binding.txtunitC
            showproductdetails = binding.showproductdetails
            editlayout = binding.editlayout
            producdetails = binding.producdetails
             TxtRemark = binding.txtreamrk
             TotalStockQTY = binding.txttotalqrty
             ProductID_S = binding.StxtProductid
            galleryViewModel.text.observe(viewLifecycleOwner, Observer {
                barcode!!.requestFocus()
                barcode!!.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->
                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                        if (AppPreferences.internetConnectionCheck(this.context)) {
                             barcodeenter = barcode!!.text.toString()
                            try {
                                if (barcodeenter!!.trim().isEmpty()) {
                                    val alertemail = AlertDialog.Builder(this.context)
                                    alertemail.setCancelable(false)
                                    alertemail.setMessage("Scan Barcode")
                                    alertemail.setPositiveButton("ok")
                                    { dialog, _ ->
                                        dialog.dismiss()
                                        barcode!!.text.clear()
                                        barcode!!.setText("")
                                        barcodeenter = ""
                                    }
                                    val dialog: AlertDialog = alertemail.create()
                                    dialog.show()
                                } else {
                                    bindproductdetails(barcodeenter!!)
                                    barcode!!.text.clear()
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

                    btnupdatestock.setOnClickListener(
                        View.OnClickListener {

                            if (TxtRemark!!.text.trim().length.toString() == "0" ) {
                                RemarkMessage()
                            }
                            else if (TotalStockQTY!!.text.trim().toString()=="" || TotalStockQTY!!.text.trim().toString()=="0") {
                                Totalstockqtycheck()
                            }
                            else {
                                updatestock(ProductID_S, TotalStockQTY!!, TxtRemark!!)

                            }
                        }
                    )

                UnitChengeBox!!.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int,
                                               before: Int, count: Int) {

                        calculationtotalunitqty()
                    }
                })
                UnitChengeP!!.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int,
                                               before: Int, count: Int) {

                        calculationtotalunitqty()
                    }
                })
                UnitChengease!!.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence, start: Int,
                                               before: Int, count: Int) {
                        calculationtotalunitqty()
                    }
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
        val stock: TextView = binding.txtStockfeild1
        val stockfeild2: TextView = binding.txtStockfeild2
        val category: TextView = binding.txtCategory
        val sub_category: TextView = binding.txtSubCategory
        val locationval: TextView = binding.txtLocation
        val barcode = binding.txtBarScanned
        val reordermark: TextView = binding.txtreordmark
        stockfeild2.visibility=View.GONE
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(this.context)
        editlayout?.visibility = View.GONE
        details.put("barcode", barcoded)
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID",AppPreferences.Device_ID))
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        var accessToken = preferences.getString("accessToken", "")
        var EmpTypeNo = preferences.getString("EmpTypeNo", "")
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
                    producdetails?.visibility = View.VISIBLE
                    showproductdetails?.visibility = View.VISIBLE
                    if(EmpTypeNo!="2") {
                        menu?.isVisible = true
                    }
//                    Toast.makeText(this.context,barcodeenter.toString(),Toast.LENGTH_LONG).show()
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
                    val DefaultStock2 = jsonrepd.getString("SPiece")
                     DUnit = jsonrepd.getInt("Dunit")
                    var imagesurl = ""
                    if (jsonrepd.getString("OPath") == null) {
                        imagesurl = jsonrepd.getString("ImageUrl")
                    } else {
                        imagesurl = jsonrepd.getString("OPath")
                    }
                    val location = jsonrepd.getString("Location")
                    produname.text = "$pname"
                    productId!!.text = "$ProductId"

                    val unitypycolor =   punitypa.trim().split("(").toMutableList()
                    val spannableString1 = SpannableString( punitypa.trim())
                    val red1 = ForegroundColorSpan(Color.parseColor("#E60606"))
                    val boldSpan = StyleSpan(Typeface.BOLD)
                    spannableString1.setSpan(boldSpan, 0, unitypycolor[0].trim().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        spannableString1.setSpan(
                            red1, 0,  unitypycolor[0].trim().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        unitype.text = spannableString1
                    reordermark.text=Reordermark.toString()
                    price.text = "${pprice}"
                    if ("$location" == "---") {
                        locationval.text = "N/A"
                    } else {
                        locationval.text = "$location"
                    }
                    category.text = "$pCategory"
                    sub_category.text = "$pSubCategory"
                    if(DUnit !=3){
                        stockfeild2.text = "(${DefaultStock2})"
                        stockfeild2.visibility=View.VISIBLE
                    }
                    stock.text = "${DefaultStock}"
                    barcode.text = "" + bc
                    Glide.with(this)
                        .load(imagesurl)
                        .into(imagur)
                    pDialog.dismiss()
                } else {
                    pDialog.dismiss()

                    showproductdetails?.visibility = View.GONE
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
                        menu?.isVisible = false
                    }
                    AppPreferences.playSoundbarcode()
                    alertemail.setCancelable(false)
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

    override fun onCreateOptionsMenu(x: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.myapplication.R.menu.main_activity2, x)
        super.onCreateOptionsMenu(x, inflater)
        menu= x.findItem(com.example.myapplication.R.id.editproduct)
        backinvetory = x.findItem(com.example.myapplication.R.id.backinvetory)

        if(menu!=null) {
            menu?.isVisible = false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.example.myapplication.R.id.editproduct -> {
                if (AppPreferences.internetConnectionCheck(this.context)) {
                    if (productId != null) {
                        var toolbar: Toolbar? = null
                        toolbar = view?.findViewById(com.example.myapplication.R.id.toolbar)
                        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
                        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Stock Update"
                        menu?.isVisible = false
                        backinvetory?.isVisible = true
                        val productdetils = binding.producdetails
                        val editlayout = binding.editlayout
                        productdetils.visibility = View.GONE
                        editlayout.visibility = View.VISIBLE
                        Gridlauyoutstock = binding.stockupdate
                         txtunitB = binding.txtBunit
                        txtunitqtyB = binding.txtBunitqty
                         txtunitC = binding.txtCunit
                        txtunitqtyC = binding.txtCunitqty
                         txtunitP= binding.txtPunit
                        var ProductName: TextView = binding.txtproductname
                            TxttotalStock=binding.txttotal
                        var ProductID = binding.StxtProductid
                        var defaultstock = binding.defaultstock
                         TxtRemark=binding.txtreamrk
                        var TxtRemarkNotw:TextView=binding.txtremarnote
                        Layoutdefault=binding.Layoutdefault
                        Layoutdefault!!.visibility=View.GONE
                        txtunitqtyP = binding.txtPunitqty
                        val Jsonarra = JSONObject()
                        val detailstock = JSONObject()
                        val JSONObjs = JSONObject()
                        val queues = Volley.newRequestQueue(this.context)
                        detailstock.put("productId", productId!!.text)
                        JSONObjs.put(
                            "requestContainer",
                            Jsonarra.put("appVersion", AppPreferences.AppVersion)
                        )
                        JSONObjs.put("requestContainer",Jsonarra.put("deviceID",AppPreferences.Device_ID))
                        val preferencesaccess =
                            PreferenceManager.getDefaultSharedPreferences(context)
                        var accessTokenS = preferencesaccess.getString("accessToken", "")

                        JSONObjs.put(
                            "requestContainer",
                            Jsonarra.put("accessToken", accessTokenS)
                        )

                        JSONObjs.put("requestContainer", Jsonarra.put("filterkeyword", detailstock))
                        val requpdatestock = JsonObjectRequest(
                            Request.Method.POST,
                            AppPreferences.apiurl + AppPreferences.GET_Packing_details,
                            JSONObjs,
                            { responsesmsg ->
                                val resobjs = (responsesmsg.toString())
                                val responsemsgs = JSONObject(resobjs)
                                Gridlauyoutstock!!.visibility = View.GONE
                                Gridlauyoutstock!!.visibility = View.VISIBLE
                                showproductdetails?.visibility = View.GONE
                                editlayout.visibility = View.VISIBLE
                                producdetails?.visibility = View.GONE
                                val resultobjs = JSONObject(responsemsgs.getString("d"))
                                val presponsmsgs = resultobjs.getString("responseCode")
                                val resmsg = resultobjs.getString("responseMessage")
                                if (presponsmsgs == "201") {
                                    val jsondatas = resultobjs.getString("responseData")
                                    val jsonrepdu = JSONObject(jsondatas.toString())
                                    val ProductId = jsonrepdu.getString("PId")
                                    var Productname = jsonrepdu.getString("PName")
                                    var StockRemark = jsonrepdu.getString("StockRemark")
                                    var StockNote = jsonrepdu.getString("StockNote")
                                     DUnit = jsonrepdu.getInt("DUnit")
                                    TxtRemark!!.setText(StockRemark)
                                    TxtRemarkNotw.text=StockNote
                                    val unitlist = jsonrepdu.getJSONArray("UList")
                                    for (i in 0 until unitlist.length()) {
                                        var unittype = unitlist.getJSONObject(i).getString("UName")
                                        var Qty = unitlist.getJSONObject(i).getInt("Qty")
                                        var UnitType = unitlist.getJSONObject(i).getInt("UnitType")
                                        ProductID.text = ProductId.toString()
                                        ProductName.text = Productname
                                            if (UnitType == 1) {
                                                txtunitC!!.text = if(DUnit!=1){
                                                    "$unittype"
                                                }else {
                                                    "$unittype " + "*"
                                                }
                                                txtunitqtyC!!.setText(Qty.toString())
                                                Layoutbindunit = binding.LayoutCase
                                                Layoutbindunit!!.visibility = View.VISIBLE
                                                val spannableString = SpannableString(txtunitC!!.text)
                                                val red = ForegroundColorSpan(Color.RED)
                                                if(txtunitC!!.text.length==6) {
                                                    spannableString.setSpan(
                                                        red,
                                                        4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                                    )
                                                    txtunitC!!.text = spannableString
                                                }
                                            }
                                            if (UnitType == 2) {
                                                txtunitB!!.text =  if(DUnit!=2){
                                                    "$unittype"
                                                }else {
                                                    "$unittype " + "*"
                                                }
                                                txtunitqtyB!!.setText(Qty.toString())
                                                Layoutbindunit = binding.layoutbox
                                                Layoutbindunit!!.visibility = View.VISIBLE
                                                val spannableString = SpannableString(txtunitB!!.text)
                                                val green = ForegroundColorSpan(Color.RED)
                                                if(txtunitB!!.text.length==5) {
                                                    spannableString.setSpan(
                                                        green,
                                                        3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                                    )
                                                    txtunitB!!.text = spannableString
                                                }
                                            }
                                            if (UnitType == 3) {
                                                txtunitP!!.text =  if(DUnit!=3){
                                                    "$unittype"
                                                }else{
                                                    "$unittype " + "*"

                                                }
                                                txtunitqtyP!!.setText(Qty.toString())
                                                Layoutbindunit = binding.LayoutPieace
                                                Layoutbindunit!!.visibility = View.VISIBLE
                                                val spannableString = SpannableString(txtunitP!!.text)
                                                val red = ForegroundColorSpan(Color.RED)
                                                if(txtunitP!!.text.length==7) {
                                                    spannableString.setSpan(
                                                        red,
                                                        5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                                    )
                                                    txtunitP!!.text = spannableString
                                                }
                                            }
                                        if(DUnit==1){
                                             Layoutdefault=binding.Layoutdefault
                                            Layoutdefault!!.visibility=View.VISIBLE
                                            defaultstock.text = "Stock in Case"
                                        }
                                        if(DUnit==2){
                                             Layoutdefault=binding.Layoutdefault
                                            Layoutdefault!!.visibility=View.VISIBLE
                                            defaultstock.text = "Stock in Box"
                                        }
                                    }
                                    Layoutbindunit = binding.Layoutqty
                                    Layoutbindunit!!.visibility = View.VISIBLE
                                } else {
                                    Toast.makeText(this.context, resmsg, Toast.LENGTH_LONG).show()
                                }
                            },
                            { response ->

                                Log.e("onError", error(response.toString()))
                            })
                        queues.add(requpdatestock)
                    } else {
                        Toast.makeText(this.context, "Scan Barcode", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    CheckInterNetDailog()
                }
                true
            }

            com.example.myapplication.R.id.backinvetory  -> {
                if (AppPreferences.internetConnectionCheck(this.context)) {
                    if (TotalStockQTY!!.text.toString()!="" && TotalStockQTY!!.text.toString().toInt()>0) {
                      var backbtn=  SweetAlertDialog(this.context, SweetAlertDialog.WARNING_TYPE)
                        backbtn.setContentText("Discard stock changes?")
                        backbtn.setCancelButtonBackgroundColor(Color.parseColor("#4cae4c"))
                        backbtn.setCancelButton( "Yes")
                            { sDialog -> sDialog.dismissWithAnimation() }
                        backbtn.setConfirmText("No")
                        backbtn.setConfirmButtonBackgroundColor(Color.parseColor("#E60606"))
                        backbtn.setCancelClickListener {
                                    sDialog ->
                                sDialog.dismissWithAnimation()
                                showproductdetails?.visibility = View.VISIBLE
                                producdetails?.visibility = View.VISIBLE
                                editlayout?.visibility = View.GONE
                                backinvetory?.isVisible = false
                                menu?.isVisible = true
                                clear()
                            }
                        backbtn.setConfirmClickListener {
                                    sDialog -> sDialog.dismissWithAnimation()
                            }
                        backbtn.setCanceledOnTouchOutside(false)
                        backbtn.show()

                    }
                    else{
                        showproductdetails?.visibility = View.VISIBLE
                        producdetails?.visibility = View.VISIBLE
                        editlayout?.visibility = View.GONE
                        backinvetory?.isVisible = false
                        menu?.isVisible = true
                        clear()
                    }
                }
                else{
                    CheckInterNetDailog()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

     fun calculationtotalunitqty() {
         if (AppPreferences.internetConnectionCheck(this.context)) {
             txttotalqty = binding.txttotalqrty
             txtunitbu = binding.txtunitB
             txtunitpu = binding.txtunitP
             txtunitCu = binding.txtunitC
             txtdefaultqty = binding.txtdefaultqty

             var DefaultStock:Int=0
             unitB = if (txtunitbu!!.text.toString() != "") {
                 txtunitbu!!.text.toString().toInt()
             } else{
                 0
             }
             unitP = if (txtunitpu!!.text.toString() != "") {
                 txtunitpu!!.text.toString().toInt()
             } else{
                 0
             }
             unitC = if (txtunitCu!!.text.toString() != "") {
                 txtunitCu!!.text.toString().toInt()
             } else{
                 0
             }
             if (txtunitqtyP!!.text.toString() != "") {
                 txtunitqtyPi = txtunitqtyP!!.text.toString().toInt()
             }
             if (txtunitqtyB!!.text.toString() != "") {
                 txtunitqtyBox = txtunitqtyB!!.text.toString().toInt()
             }
             if (txtunitqtyC!!.text.toString() != "") {
                 txtunitqtyCase = txtunitqtyC!!.text.toString().toInt()
             }
             totalunitPqty = unitP!! * txtunitqtyPi!!
             totalunitBqty = unitB!! * txtunitqtyBox!!
             totalunitCqty = unitC!! * txtunitqtyCase!!
             totalunitqty = totalunitPqty!! + totalunitBqty!! + totalunitCqty!!
             txttotalqty!!.setText(totalunitqty.toString())
             if (DUnit==1){
                 Layoutdefault!!.visibility=View.VISIBLE
                 DefaultStock= totalunitqty!! / txtunitqtyCase!!
                 txtdefaultqty!!.setText(DefaultStock.toString())
             }
             if (DUnit==2){
                 Layoutdefault!!.visibility=View.VISIBLE
                 DefaultStock= totalunitqty!! / txtunitqtyBox!!
                 txtdefaultqty!!.setText(DefaultStock.toString())
             }
              totalstock=TotalStockQTY!!.text.toString().toInt()
             if(totalstock!! >1){
                 TxttotalStock!!.text="Stock in Pieces"
             }
             else
             {
                 TxttotalStock!!.text="Stock in Piece"
             }

         }
         else{
             CheckInterNetDailog()
         }
    }
    private fun updatestock(_ProductID: TextView?, StrockQty: EditText, Remark: EditText){
        if (AppPreferences.internetConnectionCheck(this.context)) {
            val Jsonarra = JSONObject()
            val Jsonarrastock = JSONObject()
            val JSONObj = JSONObject()
            val queues = Volley.newRequestQueue(this.context)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
            val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
            var accessToken = preferences.getString("accessToken", "")
            var empautoid = preferences.getString("EmpAutoId", "")
            var remark = Remark.text.toString()

                if (UnitChengeP!!.text.toString() != "" && UnitChengeP!!.text.toString() != "0") {
                    remark =
                        remark + "</br>" + txtunitP!!.text + ": " + UnitChengeP!!.text.toString()
                }
                if (UnitChengeBox!!.text.toString() != "" && UnitChengeBox!!.text.toString() != "0") {
                    remark =
                        remark + "</br>" + txtunitB!!.text + ": " + UnitChengeBox!!.text.toString()
                }
                if (UnitChengease!!.text.toString() != "" && UnitChengease!!.text.toString() != "0") {
                    remark =
                        remark + "</br>" + txtunitC!!.text + ": " + UnitChengease!!.text.toString()
                }

            JSONObj.put(
                "requestContainer", Jsonarra.put(
                    "accessTok"+"en", accessToken
                )
            )
            if (empautoid != null) {
                JSONObj.put(
                    "requestContainer", Jsonarra.put(
                        "UserAutoId" , empautoid.toInt()
                    )
                )
            }
            JSONObj.put("pObj", Jsonarrastock.put("productId", _ProductID!!.text))
            if (StrockQty.text.toString() != "") {
                JSONObj.put("pObj", Jsonarrastock.put("StockQty", StrockQty.text))
            }
            if (Remark.text.toString() != "") {
                JSONObj.put("pObj", Jsonarrastock.put("Remark",remark))
            }
            val reqStockUpdate = JsonObjectRequest(
                Request.Method.POST, AppPreferences.UPDATE_STOCK, JSONObj,
                Response.Listener { response ->
                    val resobj = (response.toString())
                    val responsemsg = JSONObject(resobj)
                    val resultobj = JSONObject(responsemsg.getString("d"))
                    val presponscode = resultobj.getString("responseCode")
                    val resmsg = resultobj.getString("responseMessage")
                    if (presponscode == "200") {
                        var updates=   SweetAlertDialog(this.context, SweetAlertDialog.SUCCESS_TYPE)
                        updates.setContentText(resmsg.toString())
                        updates.setConfirmText("ok")
                        updates.setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
                                backinvetory?.isVisible = false
                                barcodeenter = binding.txtBarScanned.text as String?
                                bindproductdetails(barcodeenter as String)
                                showproductdetails?.visibility = View.VISIBLE
                                editlayout?.visibility = View.GONE
                                producdetails?.visibility = View.GONE
                                clear()
                            }
                        updates.setCanceledOnTouchOutside(false)
                        updates.show()

                    } else {
                      var update=  SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE).setContentText(
                            resmsg.toString()
                        )
                          update.setCanceledOnTouchOutside(false)
                          update.show()

                    }
                }, Response.ErrorListener { response ->

                    Log.e("onError", error(response.toString()))
                })
            reqStockUpdate.retryPolicy = DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queues.add(reqStockUpdate)
        }
        else{
            CheckInterNetDailog()
        }
    }
    fun RemarkMessage() {


        SweetAlertDialog(this.context,SweetAlertDialog.ERROR_TYPE)
        .setContentText("Remark length should be 10 character").show()



    }

    fun Totalstockqtycheck()
    {
        var Totalstockqtycheck = SweetAlertDialog(this.context, SweetAlertDialog.WARNING_TYPE)
        Totalstockqtycheck.setTitleText("Are you sure?")
        Totalstockqtycheck.setContentText("You want to make out of stock.")
        Totalstockqtycheck.setCancelButtonBackgroundColor(Color.parseColor("#4cae4c"))
        Totalstockqtycheck.setCancelButton( "Yes")
            { sDialog -> sDialog.dismissWithAnimation() }
        Totalstockqtycheck.setConfirmText("No")
        Totalstockqtycheck.setConfirmButtonBackgroundColor(Color.parseColor("#E60606"))
        Totalstockqtycheck.setCancelClickListener {
                    sDialog ->
                sDialog.dismissWithAnimation()
                updatestock(ProductID_S, TotalStockQTY!!, TxtRemark!!)
            }
        Totalstockqtycheck.setConfirmClickListener {
                    sDialog -> sDialog.dismissWithAnimation()
            }
        Totalstockqtycheck.setCanceledOnTouchOutside(false)
        Totalstockqtycheck.show()
    }
    fun clear(){
        TotalStockQTY!!.text=null
        TxtRemark!!.text=null
        UnitChengeBox!!.text=null
        UnitChengeP!!.text=null
        UnitChengease!!.text=null
        txttotalqty!!.text=null
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


