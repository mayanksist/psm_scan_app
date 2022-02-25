package com.example.myapplication.com.example.whm.ui.interpodetails

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.load_order_page.setSupportActionBar
import com.example.whm.ui.interpodetails.DetailsItemsViewModel
import org.json.JSONObject
import kotlin.math.PI


class Detailsadapter(var ReceiveModelList: ArrayList<DetailsItemsViewModel>, var activity: Context?):

    RecyclerView.Adapter<Detailsadapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){


        var ProducyListPO: CardView = view.findViewById(com.example.myapplication.R.id.ProducyListPO)
        var PID: TextView = view.findViewById(com.example.myapplication.R.id.txtproductidl)
        var PEODUCTNAME: TextView = view.findViewById(com.example.myapplication.R.id.txtproductnamePO)
        var UNITYPW: TextView = view.findViewById(com.example.myapplication.R.id.txtunittype)
        var txttotalpieceqty: TextView = view.findViewById(com.example.myapplication.R.id.txttotalpieceqty)
        var POQTY: TextView = view.findViewById(com.example.myapplication.R.id.txtpo_qty)
        var txtisfree: TextView = view.findViewById(com.example.myapplication.R.id.txtisfree)
        var txtisexchange: TextView = view.findViewById(com.example.myapplication.R.id.txtisexchange)
        val POAutoIdTV: TextView = view.findViewById(com.example.myapplication.R.id.podraftAutoId)
        var actionedit: ImageView = view.findViewById(com.example.myapplication.R.id.actionedit)
        var free: ConstraintLayout = view.findViewById(com.example.myapplication.R.id.free)
        var exchange: ConstraintLayout = view.findViewById(com.example.myapplication.R.id.exchange)
        var txtveriqtypo: TextView = view.findViewById(com.example.myapplication.R.id.txtveriqtypo)
        var txtunitautoid: TextView = view.findViewById(com.example.myapplication.R.id.txtunitautoid)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarAction)
        var getisfree:Int=0
        var getisexchange:Int=0


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Detailsadapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.myapplication.R.layout.details__items_fragment, parent, false)
        return Detailsadapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Detailsadapter.MyViewHolder, position: Int) {
        var productList=ReceiveModelList[position]

        (activity as? AppCompatActivity)?.setSupportActionBar(holder.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.show()

        if(ReceiveModelList.size!=0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Internal PO List ("+ReceiveModelList.size+")"

        }

        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.txttotalpieceqty.text =productList.getTotalPieces().toString()
        holder.POQTY.text=productList.getPackedPOQTY().toString()
        holder.POAutoIdTV.text=productList.getPoAutoidID().toString()
        holder.txtisfree.text=productList.getISfree().toString()
        holder.getisfree=productList.getISfree().toString().toInt()
        holder.getisexchange=productList.getIsexchaNGe().toString().toInt()
        holder.txtisexchange.text=productList.getIsexchaNGe().toString()
        holder.txtveriqtypo.text=productList.getIs_VerifyQty().toString()
        holder.txtunitautoid.text=productList.getUnitAutoid().toString()


        if(holder.getisexchange==1){
            holder.txtisexchange.text="Exchange"
            holder.txtisexchange.visibility=View.VISIBLE
        }

        else{
            holder.txtisexchange.text="0"
            holder.txtisexchange.visibility=View.GONE

        }
        if(holder.getisfree==1){
            holder.txtisfree.text="Free"
            holder.txtisfree.visibility=View.VISIBLE
        }
        else{
            holder.txtisfree.text="0"
            holder.txtisfree.visibility=View.GONE

        }
        holder.actionedit.setOnClickListener {
            showpopupedit(
                holder.POQTY.text.toString().toInt(),
                holder.PID.text.toString().toInt(),
                holder.PEODUCTNAME.text.toString(),
                holder.UNITYPW.text.toString(),
                holder.txtveriqtypo.text.toString().toInt(),
                holder.POAutoIdTV.text.toString().toInt(),
                position,
                holder.getisfree,
                holder.getisexchange,
                holder.txtunitautoid.text.toString().toInt()
            )

        }



    }

    var editpoqty: TextView? = null

    fun showpopupedit(
        POQTY: Int,
        PID: Int,
        ProductName: String,
        UNITYPW: String,
        VerifyQty: Int,
        POAutoIdTV: Int,
        position: Int,
        ISFree: Int,
        ISExchange: Int,
        UnitAutoid:Int
    ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(com.example.myapplication.R.layout.verifyeditqtylayout, null)
        val POProductname: TextView =
            view.findViewById(com.example.myapplication.R.id.txtpoproductname)
        val PIPID: TextView = view.findViewById(com.example.myapplication.R.id.txtpid)
        val PInStockType: TextView = view.findViewById(com.example.myapplication.R.id.PInStockType)
        val packedqty: TextView = view.findViewById(com.example.myapplication.R.id.packedqty)
        val txisfreeverify: TextView = view.findViewById(com.example.myapplication.R.id.txisfreeverify)
        val txisExchangeverify: TextView = view.findViewById(com.example.myapplication.R.id.txisExchangeverify)
        val txthidenunitautoid: TextView = view.findViewById(com.example.myapplication.R.id.txthidenunitautoid)
        editpoqty = view.findViewById(com.example.myapplication.R.id.txteditpoqty)
        val btnpoqty: Button = view.findViewById(com.example.myapplication.R.id.btnsaevpoqty)
        val btncancel: Button = view.findViewById(com.example.myapplication.R.id.btncancel)
        val plus = view.findViewById<ImageView>(com.example.myapplication.R.id.increase)
        val minusbtn = view.findViewById<ImageView>(com.example.myapplication.R.id.decrease)
        POProductname.text = ProductName.toString()
        //editpoqty!!.setEnabled(true);

        if (ISFree==1){
            txisfreeverify.visibility=View.VISIBLE

        }
        if (ISExchange==1){
            txisExchangeverify.visibility=View.VISIBLE

        }

        PIPID.text = PID.toString()
        txthidenunitautoid.text = UnitAutoid.toString()
        packedqty.text = "PO Qty : "+POQTY.toString()
        PInStockType.text = "(In "+UNITYPW+")"
        editpoqty!!.text = VerifyQty.toString()
        plus.setOnClickListener {
            if(editpoqty!!.text.toString().trim()!="") {
                if (POQTY != editpoqty!!.text.toString().trim().toInt()) {

                if (editpoqty!!.text.toString().trim().toInt() >= 0) {
                    totalqty(editpoqty!!.text.toString().trim().toInt() + 1)
                } else {
                    editpoqty!!.text = "1"
                }
            }
                else{
                    var alertbox = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                    alertbox.contentText = "Product ID -"+PIPID.text.toString()+" </b><br/> Verify quantity "+editpoqty!!.text.toString().toInt()+" can not be more than  PO Qty "+POQTY.toString()+""
                    alertbox.confirmText = "ok"
                    alertbox.setConfirmClickListener { sDialog ->
                        alertbox.dismiss()
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()

                }

            }
            else {
                editpoqty!!.text = "1"

            }
        }
        minusbtn!!.setOnClickListener {
            if (editpoqty!!.text.toString().trim() != "") {
                if (editpoqty!!.text.toString().toInt() >0) {
                    totalqty(editpoqty!!.text.toString().trim().toInt() - 1)
                }
            }
            else {
                editpoqty!!.text = "0"

            }

        }
        btnpoqty.setOnClickListener(View.OnClickListener {
            if (!(editpoqty!!.text.toString().trim().isEmpty() || editpoqty!!.text.toString().trim()
                    .toInt() == 0))
            {
                if (editpoqty!!.text.toString().trim().toInt()<=POQTY) {
                    poqtyupdate(
                        PID.toString().toInt(),
                        editpoqty!!.text.toString().toInt(),
                        POAutoIdTV.toString().toInt(),
                        position,
                        ISFree ,
                        ISExchange,
                        txthidenunitautoid.text.toString().toInt()
                    )
                    dialog?.dismiss()
                }
                else{
                    var alertbox = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                    alertbox.contentText = "Product ID -"+PIPID.text.toString()+" </b><br/> Verify quantity "+editpoqty!!.text.toString().trim().toInt()+" can not be more than  PO Qty "+POQTY.toString()+""
                    alertbox.confirmText = "ok"
                    alertbox.setConfirmClickListener { sDialog ->
                        alertbox.dismiss()
                    }
                    alertbox.setCanceledOnTouchOutside(false)
                    alertbox.show()
                }
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
    override fun getItemCount(): Int {
        return ReceiveModelList.size
        notifyDataSetChanged()
    }

    fun poqtyupdate(
        PID: Int,
        verifyqty: Int,
        POid: Int,
        position: Int,
        ISFree:Int,
        ISExchange:Int,
        UnitAutoid: Int

    ) {


        val Jsonarra = JSONObject()
        val Jsonarrabarcode = JSONObject()
        val JSONObj = JSONObject()
        val queues = Volley.newRequestQueue(activity)
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var accessToken = preferences.getString("accessToken", "")
        var EmpAutoId = preferences.getString("EmpAutoId", "")
        JSONObj.put("requestContainer", Jsonarra.put("accessToken", accessToken))
        JSONObj.put("requestContainer", Jsonarra.put("appVersion", AppPreferences.AppVersion))
        JSONObj.put("requestContainer",Jsonarra.put("deviceID", Settings.Secure.getString(activity?.getContentResolver(), Settings.Secure.ANDROID_ID)))
        JSONObj.put("requestContainer", Jsonarra.put("UserAutoId", EmpAutoId))
        var POAutoidE = preferences.getInt("POAutoId", 0)
        if (POAutoidE != null && POAutoidE != 0) {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POAutoidE))
        } else {
            JSONObj.put("cObj", Jsonarrabarcode.put("POAutoId", POid))
        }
        JSONObj.put("cObj", Jsonarrabarcode.put("ProductId", PID))

            JSONObj.put("cObj", Jsonarrabarcode.put("IsFree", ISFree))
            JSONObj.put("cObj", Jsonarrabarcode.put("IsExchange", ISExchange))
        JSONObj.put("cObj", Jsonarrabarcode.put("UnitAutoId", UnitAutoid))

        JSONObj.put("cObj", Jsonarrabarcode.put("VerifiedQty", verifyqty))
        val DELETE_PO_LIST = JsonObjectRequest(
            Request.Method.POST, AppPreferences.INTERNAL_PO_EDIT_VERIFYQTYPO_LIST_PRODUCT, JSONObj,
            Response.Listener { response ->
                val resobj = (response.toString())
                val responsemsg = JSONObject(resobj)
                val resultobj = JSONObject(responsemsg.getString("d"))
                val responseCode = resultobj.getString("responseCode")
                val responseMessage = resultobj.getString("responseMessage")
                if (responseCode == "201") {
                    val jsondata = resultobj.getString("responseData")
                    val jsonrepdu = JSONObject(jsondata.toString())
                    val success = jsonrepdu.getString("success")
                    var updatepoqty =
                        SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE).setContentText(
                            success.toString()

                        )
                    ReceiveModelList.get(position).setIs_VerifyQty(verifyqty)

                    notifyDataSetChanged()
                    ReceiveModelList.get(position).setIs_exchangey(ISExchange)
                    ReceiveModelList.get(position).setIs_free(ISFree)
                   // notifyItemChanged(ReceiveModelList)
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

}


