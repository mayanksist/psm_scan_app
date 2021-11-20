package com.example.myapplication.com.example.whm

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.text.Editable


object AppPreferences {
    const val AppVersion = "2.0.0.3"
    const val BASEURL = "http://api.a1whm.com/AndroidAPI/"
    const val GET_ORDERS = "WDriverOrder.asmx/getOrders"
    const val SUBMIT_LOAD_ORDER = "WDriverOrder.asmx/SubmitLoadOrder"
    const val GET_ASSIGN_ORDER = "WDriverOrder.asmx/getDriverOrderList"
    const val GET_ASSIGN_ORDER_LIST = "WDriverOrder.asmx/AssignedOrderList"
}

