package com.example.whm.ui.submitpolist

import androidx.lifecycle.ViewModel


class SubmitDetailsItemViewModel (
    PID: Int, PNAME: String,
    UNITTYPE: String,UnitQTY:Int,POQTY:Int,Totalpieces:Int,DraftID:Int) {

    private lateinit var PA_NEME: String
    private var P_ID: Int = 0
    private lateinit var UNIT_TYPE: String
    private var Unit_QTY: Int = 0
    private var PO_QTY: Int=0
    private var Total_Pieces: Int=0
    private var DRAFT_ID: Int=0

    init {
        this.P_ID = PID
        this.PA_NEME = PNAME
        this.UNIT_TYPE = UNITTYPE
        this.Unit_QTY = UnitQTY
        this.PO_QTY = POQTY
        this.Total_Pieces = Totalpieces
        this.DRAFT_ID = DraftID
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


    fun getDraftID():Int?{
        return DRAFT_ID
    }


}

