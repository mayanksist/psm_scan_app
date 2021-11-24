package com.example.myapplication.com.example.whm

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.text.Editable


object AppPreferences {
    const val AppVersion = "2.0.0.10"
    const val BASEURL = "http://api.a1whm.com/AndroidAPI/"
    const val BASEURLSU = "https://api.a1whm.com/AndroidAPI/"
    const val GET_ORDERS = "WDriverOrder.asmx/getOrders"
    const val SUBMIT_LOAD_ORDER = "WDriverOrder.asmx/SubmitLoadOrder"
    const val GET_ASSIGN_ORDER = "WDriverOrder.asmx/getDriverOrderList"
    const val GET_ASSIGN_ORDER_LIST = "WDriverOrder.asmx/AssignedOrderList"
    fun internetConnectionCheck(context: Context?): Boolean {
        var Connected = false
        val connectivity = context?.applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) { Connected = true }
            else { }
        } else {
            val alertnet = AlertDialog.Builder(context)
            alertnet.setTitle("Connection")
            alertnet.setMessage("Please check your internet connection")
            alertnet.setPositiveButton("ok")
            { dialog, which -> dialog.dismiss()}
            val dialog: AlertDialog = alertnet.create()
            dialog.show()
            Connected = false
        }
        return Connected
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
}


