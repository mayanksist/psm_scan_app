package com.example.myapplication.com.example.whm.ui.inventoryreceive

import android.widget.Toast

class ReceiveModel (
    PID: Int, PNAME: String,
    UNITTYPE: String,UnitQTY:Int,POQTY:Int,Totalpieces:Int,DraftID:Int,qtyperunit:Int) {

    private lateinit var PA_NEME: String
    private var P_ID: Int = 0
    private lateinit var UNIT_TYPE: String
    private var Unit_QTY: Int = 0
    private var PO_QTY: Int=0
    private var Total_Pieces: Int=0
    private var DRAFT_ID: Int=0
    private var qtyper_unit: Int=0

    init {
        this.P_ID = PID
        this.PA_NEME = PNAME
        this.UNIT_TYPE = UNITTYPE
        this.Unit_QTY = UnitQTY
        this.PO_QTY = POQTY
        this.Total_Pieces = Totalpieces
        this.DRAFT_ID = DraftID
        this.qtyper_unit = qtyperunit
    }

    fun getPID(): Int? {
        return P_ID
    }
    fun getPNAME(): String {
        return PA_NEME
    }

    fun getUnitType(): String? {
        return UNIT_TYPE
    }
    fun getUnitQTY(): Int? {
        return Unit_QTY
    }

    fun getPOQTY(): Int? {

        return PO_QTY
    }
    fun setPOQTY(POQTY: Int?) {
         this.PO_QTY=POQTY!!
    }
    fun getTotalPieces():Int?{
       return Total_Pieces
    }
    fun setTotalPieces(newpoqty:Int?) {
        if (newpoqty != null) {
            this.Total_Pieces= newpoqty*Unit_QTY
        }
    }
    fun getasperunitqty(): Int? {
        return qtyper_unit
    }
    fun setasperunitqty(newpoqty1:Int?){
        this.qtyper_unit=newpoqty1!!
    }
    fun getTotalPiece(QTYPerUint:Int?, POQTY:Int? ) {
        if (QTYPerUint != null) {
            this.Total_Pieces=QTYPerUint*POQTY!!
        }
    }
    fun getDraftID():Int?{
        return DRAFT_ID
    }

}

