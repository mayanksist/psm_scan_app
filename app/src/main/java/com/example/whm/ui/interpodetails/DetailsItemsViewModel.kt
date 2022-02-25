package com.example.whm.ui.interpodetails



    class DetailsItemsViewModel(
        PID: Int,
        PNAME: String,
        UNITTYPE: String,
        POQTY: Int,
        Totalpieces: Int,
        PoAutoidtID: Int,
        ISfree: Int,
        IsexchaNGe: Int,
        Is_VerifyQty: Int,
        UnitAutoid:Int
    ) {

        private lateinit var PA_NEME: String
        private var P_ID: Int = 0
        private lateinit var UNIT_TYPE: String
        private var PO_QTY: Int=0
        private var Total_Pieces: Int=0
        private var POAutoid_ID: Int=0
        private var IS_free:Int = 0
        private var Is_exchaNGe:Int = 0
        private var Is_VerifyQty:Int = 0
        private var Unit_Autoid:Int = 0
        init {
            this.P_ID = PID
            this.PA_NEME = PNAME
            this.UNIT_TYPE = UNITTYPE
            this.PO_QTY = POQTY
            this.Total_Pieces = Totalpieces
            this.POAutoid_ID = PoAutoidtID
            this.IS_free = ISfree
            this.Is_exchaNGe = IsexchaNGe
            this.Is_VerifyQty = Is_VerifyQty
            this.Unit_Autoid = UnitAutoid
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


        fun getPackedPOQTY(): Int? {
            return PO_QTY
        }
        fun setIs_VerifyQty(VPOQTY: Int?) {
            this.Is_VerifyQty=VPOQTY!!
        }
        fun getTotalPieces():Int?{
            return Total_Pieces
        }


        fun getPoAutoidID():Int?{
            return POAutoid_ID
        }
        fun getISfree(): Int? {
            return IS_free
        }
        fun setIs_free(isfree: Int?) {
            this.IS_free=isfree!!
        }
        fun getIsexchaNGe(): Int? {
            return Is_exchaNGe
        }
        fun setIs_exchangey(exchange: Int?) {
            this.Is_exchaNGe=exchange!!
        }
        fun getIs_VerifyQty(): Int? {
            return Is_VerifyQty
        }

        fun getUnitAutoid():Int?{
        return Unit_Autoid

        }
    }