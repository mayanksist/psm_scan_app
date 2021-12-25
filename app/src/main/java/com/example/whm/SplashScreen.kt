package com.example.whm

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.MainActivity
import com.example.myapplication.com.example.whm.MainActivity2
import org.json.JSONObject
import java.io.IOException

class SplashScreen : AppCompatActivity() {

    val APIURL: String = AppPreferences.apiurl+"wpackerlogin.asmx/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            if(preferences.getString("email","") != "" ){
                preferences.getString("email","")?.let { Autologin(it
                ) }
            }
            else{
                val i = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                startActivity(i)
            }
        }, 2000)
    }

    fun Autologin(email: String) {
        val Jsonarra= JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val queues = Volley.newRequestQueue(this)
        if(AppPreferences.internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("email", email)
            editor.apply()
            JSONObj.put("userName", email)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
            val req = JsonObjectRequest(Request.Method.POST, APIURL, JSONObj,
                Response.Listener { response ->
                    val resobj = JSONObj.put("response", response.toString())
                    val responsemsg = JSONObject(resobj.getString("response"))
                    val responmsg = JSONObject(responsemsg.getString("d"))
                    val msg = responmsg.getString("response")
                    val resmsg = responmsg.getString("responseMessage")
                    if (msg == "failed") {
//                        val mLayout =
//                            findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
//                        mLayout.visibility = View.VISIBLE
//                        val mhiddenLayout =
//                            findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
//                        mhiddenLayout.visibility = View.GONE
                        editor.clear()
                        editor.apply()
                    } else {
                        if (msg == "success") {
                            val jsondata = responmsg.getJSONArray("responseData")
                            for (i in 0 until jsondata.length()) {

                                val Name = jsondata.getJSONObject(i).getString("Name")
                                val emptype = jsondata.getJSONObject(i).getString("EmpTypeNo")
                                val empname = jsondata.getJSONObject(i).getString("EmpType")
                                val empid = jsondata.getJSONObject(i).getString("AutoId")
                                val LName = jsondata.getJSONObject(i).getString("LName")


                                intent.putExtra("Name", Name.toString())
                                intent.putExtra("EmpTypeNo", emptype.toString())
                                intent.putExtra("empname", empname.toString())
                                intent.putExtra("empid", empid.toString())
                                editor.putString(
                                    "accessToken",
                                    jsondata.getJSONObject(i).getString("accessToken")
                                )
                                editor.putString("LName", LName)
                                editor.apply()

                                val i = Intent(
                                    applicationContext,
                                    MainActivity2::class.java
                                )
                                startActivity(i)
//                                val mLayout =
//                                    findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
//                                mLayout.visibility = View.GONE
//                                val mhiddenLayout =
//                                    findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
//                                mhiddenLayout.visibility = View.VISIBLE
                            }
                        } else {
                            val alertemail = AlertDialog.Builder(this)
                            alertemail.setTitle("User")
                            alertemail.setMessage(msg.toString())
                            alertemail.setPositiveButton("ok")
                            { dialog, which -> dialog.dismiss()
//                                scancode.text = ""
                            }
                            val dialog: AlertDialog = alertemail.create()
                            dialog.show()
                            editor.clear()
                            editor.apply()
                        }
                    }
                }, Response.ErrorListener { response ->
                    Log.e("onError", error(response.toString()))
                })
            try {
                req.retryPolicy = DefaultRetryPolicy(
                    1000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
                queues.add(req)
            } catch (e: IOException) {
                Toast.makeText(this.applicationContext, "Server Error", Toast.LENGTH_LONG).show()
            }
        }
        else{
            showCustomAlert()
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
            val launchActivity1 = Intent(this, MainActivity::class.java)
            startActivity(launchActivity1)

        }
        customDialog.show()
    }
}