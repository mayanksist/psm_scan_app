package com.example.myapplication.com.example.whm.ui.inventoryreceive

class spinnervendorlist(
    VID: Int, VNAME: String)  {

    private lateinit var V_NEME: String
    private var V_ID: Int = 0

    init {
        this.V_ID = VID
        this.V_NEME = VNAME
    }

    fun getVID(): Int? {
        return V_ID
    }
    fun setVID(VID: Int?) {
        this.V_ID=VID!!
    }
    fun getVNAME(): String {
        return V_NEME
    }
    fun setVNAME(VNAME: String?) {
        this.V_NEME=VNAME!!
    }
}