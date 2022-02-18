package com.example.myapplication.com.example.whm.ui.revertpolist

import kotlin.properties.Delegates

class RevertModel(
    BillNo: String, Billdate: String,
    VendorName: String,Status:Int,NoofProduct:Int,DAutoId:Int) {

    private lateinit var Bill_NO: String
    private lateinit var Bill_date: String
    private lateinit var Vendor_Name: String
    private var Status_po by Delegates.notNull<Int>()
    private var No_ofProduct by Delegates.notNull<Int>()
    private var DAutoId:Int = 0

    init {
        this.Bill_NO = BillNo
        this.Bill_date = Billdate
        this.Vendor_Name = VendorName
        this.Status_po = Status
        this.No_ofProduct = NoofProduct
        this.DAutoId = DAutoId
    }

    fun getBillNo(): String? {
        return Bill_NO
    }
    fun getBill_date(): String {
        return Bill_date
    }
    fun getVendorname(): String? {
        return Vendor_Name
    }

    fun getStatus(): Int? {
        return Status_po
    }
    fun getnoofproducts(): Int? {
        return No_ofProduct
    }
    fun getDAutoid(): Int? {
        return DAutoId
    }
}