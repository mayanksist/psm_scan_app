package com.example.myapplication.com.example.whm

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var scancode: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_main)
        scancode = findViewById<EditText>(com.example.myapplication.R.id.scancodeu)
        var AppVersion = findViewById<TextView>(com.example.myapplication.R.id.txtAppVersion)
        AppVersion.text = "version : " + AppPreferences.AppVersion
        scancode.requestFocus()
        scancode.setOnKeyListener OnKeyListener@{ v_, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                try {
                    checkinternet(scancode.text as Editable)
                } catch(e:IOException){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
                    return@OnKeyListener true

                return@OnKeyListener true
            }
            false
        }

    }
    val APIURL: String = AppPreferences.apiurl+"wpackerlogin.asmx/login"
    fun login(email: Editable) {
        val Jsonarra=JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val queues = Volley.newRequestQueue(this@MainActivity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("email", email.trim().toString())
        editor.apply()
        JSONObj.put("userName",email)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        val req=JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,
    Response.Listener {
            response ->
        val resobj = JSONObj.put("response", response.toString())
        val responsemsg = JSONObject(resobj.getString("response"))
        val responmsg = JSONObject(responsemsg.getString("d"))
        val msg = responmsg.getString("response")
        val resmsg = responmsg.getString("responseMessage")
            if (msg == "success") {
                val jsondata = responmsg.getJSONArray("responseData")
                for (i in 0 until jsondata.length()) {

                    val empid=jsondata.getJSONObject(i).getString("AutoId")
                    val emptype = jsondata.getJSONObject(i).getString("EmpTypeNo")
                    val empname=jsondata.getJSONObject(i).getString("EmpType")
                    val Name = jsondata.getJSONObject(i).getString("Name")
                    val LName=jsondata.getJSONObject(i).getString("LName")
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = preferences.edit()
                    editor.putString("EmpAutoId", empid.toString())
                    editor.putString("EmpTypeNo",  emptype.toString())
                    editor.putString("Empname", empname.toString())
                    editor.putString("Username", Name.toString())
                    editor.putString("LName", LName)
                    editor.putString("accessToken", jsondata.getJSONObject(i).getString("accessToken"))
                    editor.putBoolean("EnabledPickallBoxes", jsondata.getJSONObject(i).getBoolean("EnabledPickallBoxes"))
                    editor.apply()
                    Toast.makeText(
                        this,
                        "Login Successful ",
                        Toast.LENGTH_SHORT
                    ).show()
                    val i = Intent(
                        applicationContext,
                        MainActivity2::class.java
                    )
                    startActivity(i)
                }
            }
            else {
                val alertemail=AlertDialog.Builder(this)
                alertemail.setTitle("User")
                alertemail.setMessage(resmsg.toString())
                alertemail.setPositiveButton("ok")
                { dialog, which -> dialog.dismiss()
                    scancode.text = ""
                }
                val dialog:AlertDialog=alertemail.create()
                dialog.show()
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
            }

            },Response.ErrorListener {
                    response ->
                Log.e("onError", error(response.toString()))
            })
        try {
            req.retryPolicy = DefaultRetryPolicy(
                1000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            queues.add(req)
        }
        catch (e:IOException){
            Toast.makeText(this.applicationContext, "Server Error", Toast.LENGTH_LONG).show()
        }
    }
    private fun checkinternet(scansecurity: Editable) {
        var context = this
        var connectivity : ConnectivityManager? = null
        var info : NetworkInfo? = null
        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE)  as ConnectivityManager
        info = connectivity.activeNetworkInfo
        if ( connectivity != null )
        {
            if (info != null && info.isConnected == true) {
                var scansecuritykey=scansecurity
                   login(scansecuritykey)
            } else {
                showCustomAlert()
                scancode.text = ""
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
            }
        }
    }
    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.dailog_log, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        val btDismiss = dialogView.findViewById<Button>(R.id.btDismissCustomDialog)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
            val launchActivity1 = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(launchActivity1)

        }
        customDialog.show()
    }

    override fun onKeyDown(key_code: Int, key_event: KeyEvent?): Boolean {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event)
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Close")
                .setContentText("You want to close app ?")
                .setConfirmText("Yes")
                .setConfirmClickListener {
                    moveTaskToBack(true)
                    finish()
                }
                .setCancelButton(
                    "No"
                ) {
                        sDialog -> sDialog.dismissWithAnimation()
                }
                .show()
        }
        return false
    }
}