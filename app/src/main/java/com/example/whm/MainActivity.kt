package com.example.myapplication.com.example.whm

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.apisettings
import org.json.JSONObject
import java.io.IOException
import android.view.View

import android.widget.RelativeLayout
import androidx.navigation.fragment.findNavController


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
    //                return@OnKeyListener true

                return@OnKeyListener true
            }
            false
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if(preferences.getString("email","") != "" && preferences.getString("password","") != "" ){
            val mLayout = findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
            mLayout.visibility = View.GONE
            val mhiddenLayout = findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
            mhiddenLayout.visibility = View.VISIBLE
            preferences.getString("email","")?.let { Autologin(it,
                preferences.getString("password","")!!
            ) }
        }

    }
    var volleyRequestQueue: RequestQueue? = null
    val APIURL: String = apisettings().apiurl + "wpackerlogin.asmx/login"
    fun login(email: String, password: String) {
        val Jsonarra=JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val queues = Volley.newRequestQueue(this@MainActivity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("email", email.trim())
        editor.putString("password", password.trim())
        editor.apply()
        JSONObj.put("userName",email)
        JSONObj.put("password",password)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        // Toast.makeText(this@MainActivity, JSONObj.toString(), Toast.LENGTH_LONG).show()
        val req=JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,

    Response.Listener {
            response ->
        val resobj = JSONObj.put("response", response.toString())
        val responsemsg = JSONObject(resobj.getString("response"))
        val responmsg = JSONObject(responsemsg.getString("d"))
        val msg = responmsg.getString("response")
        val resmsg = responmsg.getString("responseMessage")
        if (msg == "failed") {
            val alertemail = AlertDialog.Builder(this)
            alertemail.setTitle("User")
            alertemail.setMessage(resmsg.toString())
            alertemail.setPositiveButton("ok")
            { dialog, which -> dialog.dismiss()
                scancode.text = ""
            }
            val dialog: AlertDialog = alertemail.create()
            dialog.show()
        }
        else {
            if (msg == "success") {
                val jsondata = responmsg.getJSONArray("responseData")
                for (i in 0 until jsondata.length()) {

                    val Name = jsondata.getJSONObject(i).getString("Name")
                    val emptype = jsondata.getJSONObject(i).getString("EmpTypeNo")
                    val empname=jsondata.getJSONObject(i).getString("EmpType")
                    val empid=jsondata.getJSONObject(i).getString("AutoId")
                    val LName=jsondata.getJSONObject(i).getString("LName")
                    var intent = Intent(this, MainActivity2::class.java)
                    intent.putExtra("Name", Name.toString())
                    intent.putExtra("EmpTypeNo", emptype.toString())
                    intent.putExtra("empname", empname.toString())
                    intent.putExtra("empid", empid.toString())
                    editor.putString("LName", LName)
                    editor.putString("accessToken", jsondata.getJSONObject(i).getString("accessToken"))
                    editor.apply()

                    val mLayout = findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
                    mLayout.visibility = View.GONE
                    val mhiddenLayout = findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
                    mhiddenLayout.visibility = View.VISIBLE

                    startActivity(intent)

                    Toast.makeText(
                        this,
                        "Login Successful ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else {
                val alertemail=AlertDialog.Builder(this)
                alertemail.setTitle("User")
                alertemail.setMessage(msg.toString())
                alertemail.setPositiveButton("ok",)
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
        }
            },Response.ErrorListener {
                    response ->
                Log.e("onError", error(response.toString()))
            })
        try {
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
                if(scansecuritykey.contains("_") ){
                    val getkey = scansecuritykey.split("_")
                    val userid = getkey[0]
                    val  upasswords = getkey[1]
                    var useremail = userid
                    var password = upasswords
                    useremail=userid
                    password=upasswords
                            login(useremail, password)

                }
                else{
                    val alertemail = AlertDialog.Builder(this)
                    alertemail.setMessage("Invalid credentials")
                    alertemail.setPositiveButton("ok", )
                    { dialog, which -> dialog.dismiss()
                        scancode.text = ""
                    }
                    val dialog: AlertDialog = alertemail.create()
                    dialog.show()
                }
            } else {
                val alertnet = AlertDialog.Builder(this)
                alertnet.setTitle("Connection")
                alertnet.setMessage("Check your internet connection")
                alertnet.setPositiveButton("ok")
                { dialog, which -> dialog.dismiss()
                    scancode.text = ""
                }
                val dialog: AlertDialog = alertnet.create()
                dialog.show()
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
            }
        }
    }
    fun Autologin(email: String, password: String) {
        val Jsonarra=JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val queues = Volley.newRequestQueue(this@MainActivity)
        if(internetConnectionCheck(this)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("email", email)
            editor.putString("password", password)
            editor.apply()
            JSONObj.put("userName", email)
            JSONObj.put("password", password)
            JSONObj.put("requestContainer", Jsonarra.put("appVersion", appversion))
            // Toast.makeText(this@MainActivity, JSONObj.toString(), Toast.LENGTH_LONG).show()
            val req = JsonObjectRequest(Request.Method.POST, APIURL, JSONObj,

                Response.Listener { response ->
                    val resobj = JSONObj.put("response", response.toString())
                    val responsemsg = JSONObject(resobj.getString("response"))
                    val responmsg = JSONObject(responsemsg.getString("d"))
                    val msg = responmsg.getString("response")
                    val resmsg = responmsg.getString("responseMessage")
                    if (msg == "failed") {
                        val mLayout =
                            findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
                        mLayout.visibility = View.VISIBLE
                        val mhiddenLayout =
                            findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
                        mhiddenLayout.visibility = View.GONE
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
                                var intent = Intent(this, MainActivity2::class.java)
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
                                val mLayout =
                                    findViewById<View>(com.example.myapplication.R.id.MainActivity) as RelativeLayout
                                mLayout.visibility = View.GONE
                                val mhiddenLayout =
                                    findViewById<View>(com.example.myapplication.R.id.MainHiddenActivity) as RelativeLayout
                                mhiddenLayout.visibility = View.VISIBLE
                                startActivity(intent)

                            }
                        } else {
                            val alertemail = AlertDialog.Builder(this)
                            alertemail.setTitle("User")
                            alertemail.setMessage(msg.toString())
                            alertemail.setPositiveButton("ok", null)
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
                queues.add(req)
            } catch (e: IOException) {
                Toast.makeText(this.applicationContext, "Server Error", Toast.LENGTH_LONG).show()
            }
        }
        else{
            val alertnet = AlertDialog.Builder(this)
            alertnet.setTitle("Connection")
            alertnet.setMessage("Please check your internet connection")
            alertnet.setPositiveButton("ok")
            { dialog, which -> dialog.dismiss()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            val dialog: AlertDialog = alertnet.create()
            dialog.show()

        }
    }
    fun internetConnectionCheck(activity: Context?): Boolean {
        var Connected = false
        val connectivity = activity?.applicationContext
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {

                Connected = true
            }
            else {
            }
        } else {
            val alertnet = AlertDialog.Builder(activity)
            alertnet.setTitle("Connection")
            alertnet.setMessage("Please check your internet connection")
            alertnet.setPositiveButton("ok")
            { dialog, which -> dialog.dismiss()

            }
            val dialog: AlertDialog = alertnet.create()
            dialog.show()
            Connected = false
        }
        return Connected
    }
}