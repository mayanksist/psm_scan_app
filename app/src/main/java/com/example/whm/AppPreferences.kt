package com.example.myapplication.com.example.whm
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Button
import android.app.Dialog
import android.bluetooth.BluetoothClass
import android.view.Window
import android.os.Build
import android.provider.Settings
import com.example.myapplication.BuildConfig
import java.security.AccessController.getContext


object AppPreferences {
    const  val AppVersion = BuildConfig.VERSION_NAME
    const val BASEURL = "http://api.a1whm.com/AndroidAPI/"
    const val apiurl = "http://api1.a1whm.com/Packerapi/"
    const val GET_ORDERS = "WDriverOrder.asmx/getOrders"
    const val SUBMIT_LOAD_ORDER = "WDriverOrder.asmx/SubmitLoadOrder"
    const val GET_ASSIGN_ORDER = "WDriverOrder.asmx/getDriverOrderList"
    const val GET_ASSIGN_ORDER_LIST = "WDriverOrder.asmx/AssignedOrderList"
    const val GET_Packing_details = "WPackerProductList.asmx/getPackingDetails"
    const val UPDATE_STOCK = apiurl + "WPackerProductList.asmx/UpdateStock"
    const val Bind_VENDER_LIST = BASEURL + "WPOReceive.asmx/getVendor"
    const val SCAND_BARCODE_PADD = BASEURL + "WPOReceive.asmx/getproductbybarcode"
    const val SUBMIT_PO_LIST = BASEURL + "WPOReceive.asmx/submitPO"
    const val DELETE_PO_LIST = BASEURL + "WPOReceive.asmx/deletePOItem"
    const val EDIT_PO_LIST = BASEURL + "WPOReceive.asmx/editPOQty"
    const val DRAFT_PO_LIST = BASEURL + "WPOReceive.asmx/getPOList"
    const val BIND_PRODUCT_IDNAME_BY_SEARCH = BASEURL + "WPOReceive.asmx/getProductSearch"
    const val GET_PRODUCT_DETAILS = BASEURL + "WPOReceive.asmx/getPackingDetails"
    const val ADD_PRODUCT_MANAUL = BASEURL + "WPOReceive.asmx/getproductbyproductid"
    const val Draft_PRODUCT_List = BASEURL + "WPOReceive.asmx/getPODetails"
    const val CHECK_BILL_NO = BASEURL + "WPOReceive.asmx/CheckBillNo"
    const val PRODUCT_MANAUL_DETAILS = apiurl + "WPackerProductList.asmx/getproductbyProductId"
    const val InternalPOLIST = BASEURL+"WinternalPOReceive.asmx/getInternalpopendinglist"
    const val InternalPOLISTDETAILS = BASEURL+"WinternalPOReceive.asmx/getInternalpoDetails"
    const val INTERNAL_PO_PRODUCT_LIST = BASEURL+"WinternalPOReceive.asmx/getpoproducbindbypo"
    const val INTERNAL_PO_SCANBARCODE_PRODUCT = BASEURL+"WinternalPOReceive.asmx/getpoproductbybarcode"
    const val INTERNAL_PO_EDIT_VERIFYQTYPO_LIST_PRODUCT = BASEURL+"WinternalPOReceive.asmx/QtyverifyManual"
    const val INTERNAL_PO_Submit = BASEURL+"WinternalPOReceive.asmx/Submitinternalpo"
    const val INTERNAL_PO_ADD_PRODUCT_MANAUL = BASEURL+"WinternalPOReceive.asmx/getInPOproductbyproductid"
    const val API_ADD_BARCODE = apiurl+"WPackerProductList.asmx/AddBarcode"
    //for devices

    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    fun internetConnectionCheck(context: Context?): Boolean {
        var Connected = false
        val connectivity = context?.applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                Connected = true
            }
        } else {
            showAlertinternetconnection(context)
            Connected = false
        }
        return Connected
    }

    fun showAlertinternetconnection(context: Context?) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(com.example.myapplication.R.layout.dailog_log)
        val btDismiss = dialog?.findViewById<Button>(com.example.myapplication.R.id.btDismissCustomDialog)
        btDismiss?.setOnClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    fun playSoundbarcode() {
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
    fun playSound() {
        var url: String = "https://psmnj.a1whm.com/audio/orderloaded.mp3"
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

    fun playSoundinvalid() {
        var url:String="https://psmnj.a1whm.com/audio/invalidboxscanned.mp3"
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
    fun playSoundinvalidalready() {
        var url:String="https://psmnj.a1whm.com/audio/Boxalreadyscanned.mp3"
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



