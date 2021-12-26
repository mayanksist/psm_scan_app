package com.example.myapplication.com.example.whm.ui.draftpolist

class DraftModel(
    BillNo: String, Billdate: String,
    VendorName: String,Status:String,NoofProduct:String) {

    private lateinit var Bill_NO: String
    private lateinit var Bill_date: String
    private lateinit var Vendor_Name: String
    private lateinit var Status_po: String
    private lateinit var No_ofProduct: String

    init {
        this.Bill_NO = BillNo
        this.Bill_date = Billdate
        this.Vendor_Name = VendorName
        this.Status_po = Status
        this.No_ofProduct = NoofProduct
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

    fun getStatus(): String? {
        return Status_po
    }
    fun getnoofproducts(): String? {
        return No_ofProduct
    }
}