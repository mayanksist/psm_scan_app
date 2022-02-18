package com.example.myapplication.com.example.whm.ui.assignorder

class OrderModel( CustomerName: String, Ono: String,  Od: String,  SP: String, PackedBoxes: String,
                  Stoppage: String,  PayableAmount: String,ST:String) {
    private var CustomerName: String
    private var Ono: String
    private var Od: String
    private var SP: String
    private var PackedBoxes: String
    private var Stoppage: String
    private var PayableAmount: String
    private var ST: String

    init {
        this.CustomerName = CustomerName
        this.Ono = Ono
        this.Od = Od
        this.SP = SP
        this.PackedBoxes = PackedBoxes
        this.Stoppage = Stoppage
        this.PayableAmount = PayableAmount
        this.ST = ST
    }
    fun getCustomerName(): String? {
        return CustomerName
    }
    fun setCustomerName(Custname: String?) {
        CustomerName = Custname!!
    }
    fun getOno(): String? {
        return Ono
    }
    fun setOno(Ono: String?) {
        this.Ono = Ono!!
    }
    fun getOd(): String? {
        return Od
    }
    fun setOd(Od: String?) {
        this.Od = Od!!
    }

    fun getSP(): String? {
        return SP
    }
    fun setSP(SalesPerson: String?) {
        CustomerName = SalesPerson!!
    }
    fun getPackedBoxes(): String? {
        return PackedBoxes
    }
    fun setPackedBoxes(PackedBoxes: String?) {
        this.PackedBoxes = PackedBoxes!!
    }
    fun getStoppage(): String? {
        return Stoppage
    }
    fun setStoppage(Stoppage: String?) {
        this.Stoppage = Stoppage!!
    }
    fun getPayableAmount(): String? {
        return PayableAmount
    }
    fun setPayableAmount(PayableAmount: String?) {
        this.PayableAmount = PayableAmount!!
    }
    fun getST(): String? {
        return ST
    }

}