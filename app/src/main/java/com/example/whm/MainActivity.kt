package com.example.myapplication.com.example.whm

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.apisettings
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnlogin = findViewById<Button>(R.id.login)
        var AppVersion = findViewById<TextView>(R.id.txtAppVersion)
        AppVersion.text = "version : " + AppPreferences.AppVersion
        btnlogin.setOnClickListener {
            checkinternet()
        }
    }
    var volleyRequestQueue: RequestQueue? = null
    val APIURL: String = apisettings().apiurl + "wpackerlogin.asmx/login"
    fun login(email: String, password: String) {
        val Jsonarra=JSONObject()
        val JSONObj = JSONObject()
        val appversion = AppPreferences.AppVersion
        val queues = Volley.newRequestQueue(this@MainActivity)
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
            alertemail.setPositiveButton("ok", null)
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
                    var intent = Intent(this, MainActivity2::class.java)
                    intent.putExtra("Name", Name.toString())
                    intent.putExtra("EmpTypeNo", emptype.toString())
                    intent.putExtra("empname", empname.toString())
                    intent.putExtra("empid", empid.toString())
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
                alertemail.setPositiveButton("ok",null)
                val dialog:AlertDialog=alertemail.create()
                dialog.show()

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
    private fun checkinternet(){
        var context = this
        var connectivity : ConnectivityManager? = null
        var info : NetworkInfo? = null
        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE)  as ConnectivityManager
        info = connectivity.activeNetworkInfo
        if ( connectivity != null )
        {
            if (info != null && info.isConnected == true) {
                var email = findViewById<EditText>(R.id.email)
                var upassword = findViewById<EditText>(R.id.password)
                val useremail = email.text.toString()
                val password = upassword.text.toString()
                val alertemail = AlertDialog.Builder(this)
                if (useremail.trim().isEmpty()) {
                    alertemail.setMessage("Enter User Id")
                    alertemail.setPositiveButton("ok", null)
                    val dialog: AlertDialog = alertemail.create()
                    dialog.show()
                }
                else if (password.trim().isEmpty()) {
                    alertemail.setMessage("Enter Password")
                    alertemail.setPositiveButton("ok",null)
                    val dialog:AlertDialog=alertemail.create()
                    dialog.show()
                }
                else{
                    try{
                        login(useremail, password)
                    }
                    catch (e:IOException){
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val alertnet = AlertDialog.Builder(this)
                alertnet.setTitle("Connetction")
                alertnet.setMessage("Check your internet connection")
                alertnet.setPositiveButton("ok", null)
                val dialog: AlertDialog = alertnet.create()
                dialog.show()
            }
        }
    }
}