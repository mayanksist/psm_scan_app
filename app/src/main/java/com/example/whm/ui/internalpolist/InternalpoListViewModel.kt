package com.example.whm.ui.internalpolist

import kotlin.properties.Delegates



class InternalpoListViewModel(
    PONO: String, PODATE: String,
    VendorName: String,Status:String,NoofProduct:Int,POAutoId:Int) {

    private lateinit var PO_NO: String
    private lateinit var PO_date: String
    private lateinit var Vendor_Name: String
    private var Status_po :String
    private var No_ofProduct by Delegates.notNull<Int>()
    private var POAutoId:Int = 0

    init {
        this.PO_NO = PONO
        this.PO_date = PODATE
        this.Vendor_Name = VendorName
        this.Status_po = Status
        this.No_ofProduct = NoofProduct
        this.POAutoId = POAutoId
    }

    fun getBillNo(): String? {
        return PO_NO
    }
    fun getBill_date(): String {
        return PO_date
    }
    fun getVendorname(): String? {
        return Vendor_Name
    }

    fun getStatus(): String? {
        return Status_po
    }
    fun getnoofproducts(): Int? {
        return No_ofProduct
    }
    fun getPOAutoid(): Int? {
        return POAutoId
    }
}