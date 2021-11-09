package com.example.myapplication

import android.app.AlertDialog
import android.app.DownloadManager
import android.app.Service
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.math.log
import org.json.JSONArray
import com.android.volley.VolleyError
import org.json.JSONException
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkinternet()

        var email = findViewById(R.id.email) as EditText
        var upassword=findViewById(R.id.password) as EditText
        var btnlogin =findViewById(R.id.login) as Button

        btnlogin.setOnClickListener {
            val useremail = email.text.toString();
            val password = upassword.text.toString();
            val alertemail=AlertDialog.Builder(this)


            //check if the EditText have values or not
            if(useremail.trim().isEmpty()) {

                alertemail.setTitle("Email id")
                alertemail.setMessage("Enter Email id")
                alertemail.setPositiveButton("ok",null)
                val dialog:AlertDialog=alertemail.create()
                dialog.show()

                /*Toast.makeText(applicationContext, "User Name Required ", Toast.LENGTH_SHORT).show()*/
            }
            else if (password.trim().isEmpty()) {

                alertemail.setTitle("Password")
                alertemail.setMessage("Enter Password")
                alertemail.setPositiveButton("ok",null)
                val dialog:AlertDialog=alertemail.create()
                dialog.show()
                //upassword.error = "Required"
                // Toast.makeText(applicationContext, "Password Required ", Toast.LENGTH_SHORT).show()
            }
            else{
                try{

                        login(useremail, password);

                }
                catch (e:IOException){

                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                }

                // Toast.makeText(applicationContext, "Login Successful ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var volleyRequestQueue: RequestQueue? = null

    val APIURL: String =apisettings().apiurl+"wpackerlogin.asmx/login"
    val TAG = "WHM"

    fun login(email: String, password: String) {
        val Jsonarra=JSONObject()
        val JSONObj=JSONObject()
        val appversion="1.1.0.16"
        val queues =Volley.newRequestQueue(this@MainActivity)
        JSONObj.put("userName",email)
        JSONObj.put("password",password)
        JSONObj.put("requestContainer",Jsonarra.put("appVersion",appversion))
        // Toast.makeText(this@MainActivity, JSONObj.toString(), Toast.LENGTH_LONG).show()
        val req=JsonObjectRequest(Request.Method.POST,APIURL,JSONObj,

    Response.Listener {
            response -> val resobj=JSONObj.put("response",response.toString())

        val  responsemsg = JSONObject(resobj.getString("response"));
        val  responmsg = JSONObject(responsemsg.getString("d"));
        val  msg = responmsg.getString("response");
       // Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
        // val  datan = JSONArray(responmsg.getString("responseData"));
        if(msg=="failed") {
            val alertemail=AlertDialog.Builder(this)
            alertemail.setTitle("User")
            alertemail.setMessage("Wrong user id password")
            alertemail.setPositiveButton("ok",null)
            val dialog:AlertDialog=alertemail.create()
            dialog.show()
            // Toast.makeText(this@MainActivity, "", Toast.LENGTH_LONG).show()
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
                Log.e("onError", error(response.toString()));
            })
        try {
            queues.add(req);
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


        if ( connectivity != null)
        {

            if (null!=info)
            {
                if (info.type == ConnectivityManager.TYPE_WIFI)
                {
                    //Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG).show()
                }
                else if(info.type==ConnectivityManager.TYPE_MOBILE)
                {

                    //Toast.makeText(context, "Connected Mobile", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                val alertnet=AlertDialog.Builder(this)
                alertnet.setTitle("Connetction")
                alertnet.setMessage("Check your internet connection")
                alertnet.setPositiveButton("ok",null)
                val dialog:AlertDialog=alertnet.create()
                dialog.show()
               // Toast.makeText(context, "NOT CONNECTED", Toast.LENGTH_LONG).show()
            }
        }
    }

}