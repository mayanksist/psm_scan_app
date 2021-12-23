package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.com.example.whm.AppPreferences
import org.json.JSONObject




class ReceivePOAdapter1(var ReceiveModelList: ArrayList<ReceiveModel>,var activity: Context?):

    RecyclerView.Adapter<ReceivePOAdapter1.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){


        var ProducyListPO: CardView = view.findViewById(com.example.myapplication.R.id.ProducyListPO)
        var PID: TextView = view.findViewById(com.example.myapplication.R.id.txtproductidl)
        var PEODUCTNAME: TextView = view.findViewById(com.example.myapplication.R.id.txtproductnamePO)
        var UNITYPW: TextView = view.findViewById(com.example.myapplication.R.id.txtunittype)
        var txttotalpieceqty: TextView = view.findViewById(com.example.myapplication.R.id.txttotalpieceqty)
        var POQTY: TextView = view.findViewById(com.example.myapplication.R.id.txtpo_qty)
        var deletepolist: ImageView = view.findViewById(com.example.myapplication.R.id.actiondelete)
        var actionedit: ImageView = view.findViewById(com.example.myapplication.R.id.actionedit)
        val draftAutoIdTV: TextView = view.findViewById(com.example.myapplication.R.id.podraftAutoId)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceivePOAdapter1.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.myapplication.R.layout.receivepolist, parent, false)
        return ReceivePOAdapter1.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceivePOAdapter1.MyViewHolder, position: Int) {
        var productList=ReceiveModelList[position]
       // var model = ReceiveModelList.get(position)
        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.txttotalpieceqty.text =productList.getTotalPieces().toString()
        holder.POQTY.text=productList.getPOQTY().toString()
        holder.draftAutoIdTV.text=productList.getDraftID().toString()

        holder.deletepolist.setOnClickListener(View.OnClickListener {
            Deletepolist(holder.PID.text.toString().toInt(),holder.draftAutoIdTV.text.toString().toInt(),position)
            notifyDataSetChanged()
        })
        holder.actionedit.setOnClickListener {
            showpopupedit(holder.POQTY.text.toString().toInt(),holder.PID.text.toString().toInt(),holder.PEODUCTNAME.text.toString(),holder.draftAutoIdTV.text.toString().toInt(),position)

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
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        JSONObj.put("cObj", Jsonarrabarcode.put("ProductId", PID))
        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId",draftid ))

        val DELETE_PO_LIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.DELETE_PO_LIST, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    var alertbox = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                    alertbox.setTitleText("Are you sure?")
                    alertbox.setContentText("You want to delete this product.")
                    alertbox.setCancelButtonBackgroundColor(Color.parseColor("#4cae4c"))
                    alertbox.setCancelButton( "Yes")
                    { sDialog -> sDialog.dismissWithAnimation()


                    }
                    alertbox.setConfirmText("No")
                    alertbox.setConfirmButtonBackgroundColor(Color.parseColor("#E60606"))
                    alertbox.setCancelClickListener {
                            sDialog ->
                        var update=  SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setContentText(
                            responseMessage.toString()
                        )
                        ReceiveModelList.removeAt(position)
                        sDialog.dismissWithAnimation()
                        notifyDataSetChanged()
                        update.setCanceledOnTouchOutside(false)
                        update.show()

                    }
                    alertbox.setConfirmClickListener {
                            sDialog -> sDialog.dismissWithAnimation()
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()
                } else {
                    var update=  SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setContentText(
                        responseMessage.toString()
                    )
                    update.setCanceledOnTouchOutside(false)
                    update.show()
                  //  Toast.makeText(activity, responseMessage, Toast.LENGTH_SHORT).show()
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
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("accessToken", accessToken)
        )
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put(
            "requestContainer",
            Jsonarra.put("UserAutoId", EmpAutoId)
        )
        JSONObj.put("cObj", Jsonarrabarcode.put("draftAutoId", draftid))
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
                    var updatepoqty=  SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setContentText(
                        responseMessage.toString()
                    )
                    ReceiveModelList.get(position).setPOQTY(POQTYu);
                    notifyDataSetChanged()
                    updatepoqty.setCanceledOnTouchOutside(false)
                    updatepoqty.show()
                } else {
                    var updatepoqty=  SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE).setContentText(
                        responseMessage.toString()
                    )
                    updatepoqty.setCanceledOnTouchOutside(false)
                    updatepoqty.show()                }

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
    fun showpopupedit(POQTY: Int, PID: Int, ProductName: String, draftAutoIdTV: Int, position: Int) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(com.example.myapplication.R.layout.editqtylayout, null)
        val POProductname: TextView = view.findViewById(com.example.myapplication.R.id.txtpoproductname)
        val PIPID: TextView = view.findViewById(com.example.myapplication.R.id.txtpid)
        val editpoqty: TextView = view.findViewById(com.example.myapplication.R.id.txteditpoqty)
        val btnpoqty: Button = view.findViewById(com.example.myapplication.R.id.btnsaevpoqty)

        POProductname.setText(ProductName.toString())
        PIPID.setText(PID.toString()+" - ")
       // tvTitle.setTextColor(Color.RED);

        editpoqty.setText(POQTY.toString())

        btnpoqty.setOnClickListener(View.OnClickListener {
            poqtyupdate(PID.toString().toInt(),editpoqty.text.toString().toInt(),draftAutoIdTV.toString().toInt(),position)
            dialog?.dismiss()
        })
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}




