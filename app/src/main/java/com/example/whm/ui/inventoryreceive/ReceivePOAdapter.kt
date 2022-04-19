package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.whm.AppPreferences
import org.json.JSONObject
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.load_order_page.setSupportActionBar

class ReceivePOAdapter1(
    var ReceiveModelList: ArrayList<ReceiveModel>, var activity: Context?
) :
    RecyclerView.Adapter<ReceivePOAdapter1.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ProducyListPO: CardView =
            view.findViewById(com.example.myapplication.R.id.ProducyListPO)
        var PID: TextView = view.findViewById(com.example.myapplication.R.id.txtproductidl)
        var PEODUCTNAME: TextView =
            view.findViewById(com.example.myapplication.R.id.txtproductnamePO)
        var UNITYPW: TextView = view.findViewById(com.example.myapplication.R.id.txtunittype)
        var txttotalpieceqty: TextView =
            view.findViewById(com.example.myapplication.R.id.txttotalpieceqty)
        var POQTY: TextView = view.findViewById(com.example.myapplication.R.id.txtpo_qty)
        var deletepolist: ImageView = view.findViewById(com.example.myapplication.R.id.actiondelete)
        var actionedit: ImageView = view.findViewById(com.example.myapplication.R.id.actionedit)
        val draftAutoIdTV: TextView = view.findViewById(com.example.myapplication.R.id.podraftAutoId)
        val qtyperunit: TextView = view.findViewById(com.example.myapplication.R.id.qtyperunit)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        //  var qtyperunit:Int=0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.myapplication.R.layout.receivepolist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var productList = ReceiveModelList[position]
        (activity as? AppCompatActivity)?.setSupportActionBar(holder.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val prefs: SharedPreferences? = activity?.getSharedPreferences("CheckShared", MODE_PRIVATE)
        val value = prefs?.getString("header", "")
        if(ReceiveModelList.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = value+"("+ReceiveModelList.size+")"
        }
        else{
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = value
        }
        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.txttotalpieceqty.text = productList.getTotalPieces().toString()
        holder.POQTY.text = productList.getPOQTY().toString()
        holder.qtyperunit.text = productList.getasperunitqty().toString()
        holder.draftAutoIdTV.text = productList.getDraftID().toString()
        holder.deletepolist.setOnClickListener(View.OnClickListener { view ->
            var alertbox = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
            alertbox.titleText = "Are you sure?"
            alertbox.setContentText("You want to delete " + holder.PID.text + "-" + holder.PEODUCTNAME.text + ".").contentTextSize =
                20
            alertbox.cancelButtonBackgroundColor = Color.parseColor("#4cae4c")
            alertbox.setCancelButton("Yes")
            { sDialog ->
                sDialog.dismissWithAnimation()
            }
            alertbox.confirmText = "No"
            alertbox.confirmButtonBackgroundColor = Color.parseColor("#E60606")
            alertbox.setCancelClickListener { sDialog ->
                Deletepolist(
                    holder.PID.text.toString().toInt(),
                    holder.draftAutoIdTV.text.toString().toInt(),
                    position
                )
                notifyDataSetChanged()
                ReceiveModelList.removeAt(position)
                sDialog.dismissWithAnimation()
                notifyDataSetChanged()

            }
            alertbox.setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            alertbox.setCanceledOnTouchOutside(false)
            alertbox.show()

        })
        holder.actionedit.setOnClickListener {
            showpopupedit(
                holder.POQTY.text.toString().toInt(),
                holder.PID.text.toString().toInt(),
                holder.PEODUCTNAME.text.toString(),
                holder.UNITYPW.text.toString(),
                holder.draftAutoIdTV.text.toString().toInt(),
                position
            )
        }
    }
    override fun getItemCount(): Int {
        return ReceiveModelList.size
        notifyDataSetChanged()
    }
    fun Deletepolist(PID: Int, draftid: Int, position: Int) {
        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(activity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        var DAutoid = preferences.getInt("DAutoid", 0)
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)))

        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        JSONObj.put("cObj", Jsonarrabarcode.put("ProductId", PID))
        if (DAutoid != null && DAutoid != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoid))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftid))
        }
        val DELETE_PO_LIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.DELETE_PO_LIST, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {

                    ReceiveModelList.size
                    notifyDataSetChanged()
                    val prefs: SharedPreferences? = activity?.getSharedPreferences("CheckShared", MODE_PRIVATE)
                    val value = prefs?.getString("header", "")
                    if(ReceiveModelList.size!=0){
                        (activity as AppCompatActivity?)!!.supportActionBar!!.title = value+"("+ReceiveModelList.size+")"
                    }
                    else{
                        (activity as AppCompatActivity?)!!.supportActionBar!!.title = value
                    }
                    var alertbox = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                    alertbox.contentText = responseMessage
                    alertbox.confirmText = "ok"

                    alertbox.setConfirmClickListener { sDialog ->
                        alertbox.dismiss()
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

                } else {
                    var update =
                        SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setContentText(
                            responseMessage.toString()
                        )
                    update.setCanceledOnTouchOutside(false)
                    update.show()
                }
            }, Response.ErrorListener { response ->
                Log.e("onError", error(response.toString()))
            })
        DELETE_PO_LIST.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(DELETE_PO_LIST)
    }

    fun poqtyupdate(PID: Int, POQTYu: Int, draftid: Int, position: Int) {

        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(activity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        var DAutoidE = preferences.getInt("DAutoid", 0)
        if (DAutoidE != null && DAutoidE != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", DAutoidE))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftid))
        }
        JSONObj.put("cObj", Jsonarrabarcode.put("ProductId", PID))
        JSONObj.put("cObj", Jsonarrabarcode.put("pOQty", POQTYu))
        val DELETE_PO_LIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.EDIT_PO_LIST, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var updatepoqty =
                        SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setContentText(
                            responseMessage.toString()
                        )
                    var productList = ReceiveModelList[position]
                    var to: Int? = 0
                    to = productList.getasperunitqty()
                    ReceiveModelList.get(position).setPOQTY(POQTYu)
                    //ReceiveModelList.get(position).setasperunitqty(qtyperunit);
                    ReceiveModelList.get(position).getTotalPiece(to, POQTYu)
                    notifyDataSetChanged()
                    updatepoqty.setCanceledOnTouchOutside(false)
                    updatepoqty.show()
                } else {
                    var updatepoqty =
                        SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setContentText(
                            responseMessage.toString()
                        )
                    updatepoqty.setCanceledOnTouchOutside(false)
                    updatepoqty.show()
                }

            }, Response.ErrorListener { response ->

                Log.e("onError", error(response.toString()))
            })
        DELETE_PO_LIST.retryPolicy = DefaultRetryPolicy(
            1000000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queues.add(DELETE_PO_LIST)
    }

    var editpoqty: TextView? = null

    fun showpopupedit(
        POQTY: Int,
        PID: Int,
        ProductName: String,
        UNITYPW: String,
        draftAutoIdTV: Int,
        position: Int
    ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(com.example.myapplication.R.layout.editqtylayout, null)
        val POProductname: TextView =
            view.findViewById(com.example.myapplication.R.id.txtpoproductname)
        val PIPID: TextView = view.findViewById(com.example.myapplication.R.id.txtpid)
        val PInStockType: TextView = view.findViewById(com.example.myapplication.R.id.PInStockType)
        editpoqty = view.findViewById(com.example.myapplication.R.id.txteditpoqty)
        val btnpoqty: Button = view.findViewById(com.example.myapplication.R.id.btnaddbarcode)
        val btncancel: Button = view.findViewById(com.example.myapplication.R.id.btncancel)
        val plus = view.findViewById<ImageView>(com.example.myapplication.R.id.increase)
        val minusbtn = view.findViewById<ImageView>(com.example.myapplication.R.id.decrease)
        POProductname.text = ProductName.toString()
//        editpoqty!!.setEnabled(false);

        PIPID.text = PID.toString()
        PInStockType.text = "(In "+UNITYPW+")"
        editpoqty!!.text = POQTY.toString()
        plus.setOnClickListener {
            if(editpoqty!!.text.toString()!="") {
                if (editpoqty!!.text.toString().toInt() >= 0) {
                    totalqty(editpoqty!!.text.toString().toInt() + 1)
                }
            }else{
                editpoqty!!.text = "1"
            }
        }
        minusbtn!!.setOnClickListener {
            if (editpoqty!!.text.toString() != "") {
                if (editpoqty!!.text.toString().toInt() >0) {
                    totalqty(editpoqty!!.text.toString().toInt() - 1)
                }
            }
            else {
                editpoqty!!.text = "0"
            }

        }
        btnpoqty.setOnClickListener(View.OnClickListener {
            if (!(editpoqty!!.text.toString().isEmpty() || editpoqty!!.text.toString()
                    .toInt() == 0)
            ) {
                poqtyupdate(
                    PID.toString().toInt(),
                    editpoqty!!.text.toString().toInt(),
                    draftAutoIdTV.toString().toInt(),
                    position
                )
                dialog?.dismiss()
            } else {
                var alertbox = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                alertbox.contentText = "Quantity Required"
                alertbox.confirmText = "ok"
                alertbox.setConfirmClickListener { sDialog ->
                    alertbox.dismiss()
                }
                alertbox.setCanceledOnTouchOutside(false)
                alertbox.show()
            }
        })
        btncancel.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun totalqty(number: Int) {
        editpoqty!!.text = "$number"
    }
}





