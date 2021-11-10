package com.example.myapplication.ui.gallery

import android.R
import android.app.AlertDialog
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.apisettings
import com.example.myapplication.databinding.FragmentGalleryBinding
import org.json.JSONObject
import java.io.IOException


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    var mediaPlayer: MediaPlayer? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val view=inflater.inflate(com.example.myapplication.R.layout.fragment_gallery,container,false)

        val barcode: EditText = binding.barcodetype

        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            //val barcodeenter=barcode.getText().toString();
            barcode.requestFocus()
            barcode.setOnKeyListener(View.OnKeyListener { v_, keyCode, event ->

                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {

                    val barcodeenter = barcode.text.toString()
                    // Toast.makeText(activityObj, barcodeenter, Toast.LENGTH_SHORT).show()

                    //barcode .clearFocus()
                    //  barcode.isCursorVisible = false
                    try {
                        bindproductdetails(barcodeenter)
                        barcode.text.clear()


                    }
                    catch (e:IOException){
                        Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()

                    }
                    return@OnKeyListener true

                }
                false

            })
        })
        return root


    }

    val APIURL: String =apisettings().apiurl+"WPackerProductList.asmx/getProductsList"

    fun bindproductdetails(barcoded: String) {
        //Toast.makeText(this.context, barcoded, Toast.LENGTH_SHORT).show()
        val produname: TextView = binding.productname
        val productId: TextView = binding.txtProductId
        val unitype: TextView = binding.txtunitype
        val price: TextView = binding.priductprise
        val imagur: ImageView = binding.productimage
        val stock: TextView = binding.txtStock2
        val def_stock: TextView = binding.txtdefaultStock
        val category: TextView = binding.txtCategory
        val sub_category: TextView = binding.txtSubCategory
        val locationval: TextView = binding.txtLocation
        val Jsonarra = JSONObject()
        val details = JSONObject()
        val JSONObj = JSONObject()
        val detailsobj = JSONObject()
        val appversion = "1.1.0.25"
        val layout = binding.showproductdetails
        val queues = Volley.newRequestQueue(this.context)

        details.put("barcode", barcoded)

        JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))

        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", "a2d8fjhsdkfhsbddeveloper@psmgxzn3d8xy7jewbc7x")
        )
        JSONObj.put("requestContainer", Jsonarra.put("filterkeyword", details))
        val reqPRODUCTDETAILS = JsonObjectRequest(
            Request.Method.POST, APIURL, JSONObj,

            Response.Listener {
                    response ->
                val resobj = (response.toString())

                val responsemsg = JSONObject(resobj.toString())

                val resultobj = JSONObject(responsemsg.getString("d"))

                val responsmsg = resultobj.getString("response")
                val presponsmsg = resultobj.getString("responseMessage")

                if (presponsmsg == "Products Found") {

                    layout.visibility = View.GONE
                    layout.visibility = View.VISIBLE

                    val jsondata = resultobj.getString("responseData")

                    //Toast.makeText( this.context, jsondata.toString(), Toast.LENGTH_LONG).show()

                    val jsonrepd = JSONObject(jsondata.toString())
                    val pname = jsonrepd.getString("PName")
                    val pCategory = jsonrepd.getString("Cat")
                    val pSubCategory = jsonrepd.getString("SCat")
                    val ProductId = jsonrepd.getString("PId")
                    val punitypa = jsonrepd.getString("UnitType")
                    val pqty = jsonrepd.getInt("Qty")
                    val pprice = jsonrepd.getDouble("Price")
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
                    if (punitypa.toString() == "Piece") {
                        unitype.text = "Pieces"
                    } else {
                        unitype.text = "$punitypa" + " (" + "${pqty.toInt()}" + " Pieces)"
                    }

                    val rounded = String.format("%.2f", pprice)
                    price.text = "${"$" + rounded.toDouble()}"
                    locationval.text = "$location"
                    category.text = "$pCategory"
                    sub_category.text = "$pSubCategory"
                    stock.text = "${cstock.toInt()}" + " ($punitypa)"
                    def_stock.text = "${DefaultStock.toInt()}" + " ($punitypa)"

//                    Glide.with(this@GalleryFragment)
//                        .load(imagesurl)
//                        .placeholder(R.drawable.ic_delete) // any placeholder to load at start
//                        .error(R.drawable.ic_dialog_alert)
//                        .into(binding.productimage)

//                    Picasso.get().load(imagesurl).resize(100, 100).centerCrop().into(binding.productimage)

                    Glide.with(this)
                        .load(imagesurl) // image url
                        .placeholder(R.drawable.ic_menu_report_image) // any placeholder to load at start
                        .error(R.drawable.ic_menu_report_image)  // any image in case of error
                        .override(600, 600) // resizing
                        .centerCrop()
                        .into(imagur)
                }
                else{
                    playSound()
                    layout.visibility = View.GONE
                    val alertemail= AlertDialog.Builder(this.context)
                    alertemail.setTitle("Barcode")

                    alertemail.setMessage("Barcode does not exist")
                    alertemail.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alertemail.create()
                    dialog.show()
                }

            }, Response.ErrorListener { response ->
                Log.e("onError", error(response.toString()))
            })
        reqPRODUCTDETAILS.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(reqPRODUCTDETAILS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    var mMediaPlayer: MediaPlayer? = null

    fun playSound() {
        var url:String="https://psmnj.a1whm.com/Audio/NOExists.mp3"
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare()
            start()
        }
    }
}


