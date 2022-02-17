package com.example.myapplication.com.example.whm.ui.interpodetails

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.whm.ui.interpodetails.DetailsItemsViewModel


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
        val draftAutoIdTV: TextView = view.findViewById(com.example.myapplication.R.id.podraftAutoId)
        var actionedit: ImageView = view.findViewById(com.example.myapplication.R.id.actionedit)

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
        holder.PID.text = productList.getPID().toString()
        holder.PEODUCTNAME.text = productList.getPNAME()
        holder.UNITYPW.text = productList.getUnitType().toString()
        holder.txttotalpieceqty.text =productList.getTotalPieces().toString()
        holder.POQTY.text=productList.getPOQTY().toString()
        holder.draftAutoIdTV.text=productList.getDraftID().toString()
        holder.txtisfree.text=productList.getISfree().toString()
        holder.txtisexchange.text=productList.getIsexchaNGe().toString()
        holder.txtisfree!!.setEnabled(true);
        holder.txtisexchange!!.setEnabled(true);
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
        val btnpoqty: Button = view.findViewById(com.example.myapplication.R.id.btnsaevpoqty)
        val btncancel: Button = view.findViewById(com.example.myapplication.R.id.btncancel)
        val plus = view.findViewById<ImageView>(com.example.myapplication.R.id.increase)
        val minusbtn = view.findViewById<ImageView>(com.example.myapplication.R.id.decrease)
        POProductname.text = ProductName.toString()
            //editpoqty!!.setEnabled(true);

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
//                poqtyupdate(
//                    PID.toString().toInt(),
//                    editpoqty!!.text.toString().toInt(),
//                    draftAutoIdTV.toString().toInt(),
//                    position
//                )
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
    override fun getItemCount(): Int {
        return ReceiveModelList.size
        notifyDataSetChanged()
    }

}