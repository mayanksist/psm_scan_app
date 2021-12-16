package com.example.myapplication.com.example.whm.ui.inventoryreceive

class ReceiveModel (
    PID: Int, PNAME: String,
    UNITTYPE: String,UnitQTY:Int) {

        private lateinit var PA_NEME: String
        private  var P_ID: Int = 0
    private lateinit var UNIT_TYPE: String
    private  var Unit_QTY: Int=0

        init {
            this.P_ID = PID
            this.PA_NEME = PNAME
            this.UNIT_TYPE = UNITTYPE
            this.Unit_QTY = UnitQTY
        }
        fun getPID(): Int? {
            return P_ID
        }
        fun setPID(PID: Int?) {
            this.P_ID = PID!!
        }

        fun getPNAME(): String {
            return  PA_NEME
        }
        fun setProductName(PNAME: String?){

            if (PNAME != null) {
                this.PA_NEME = PNAME
            }
        }
        fun getUnitType(): String? {
            return UNIT_TYPE
        }
        fun setStoppage(UNITTYPE: String?) {
            this.UNIT_TYPE = UNITTYPE!!
        }
    fun getUnitQTY(): Int? {
        return Unit_QTY
    }
    fun setUNITQTY(UnitQTY: Int?) {
        this.Unit_QTY = UnitQTY!!
    }
}