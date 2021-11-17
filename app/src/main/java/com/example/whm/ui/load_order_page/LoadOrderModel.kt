package com.example.myapplication.com.example.whm.ui.load_order_page

class LoadOrderModel(Ono: String, PackedBoxes: String,
                      Stoppage: String) {

    private lateinit var Ono: String
    private lateinit var PackedBoxes: String
    private lateinit var Stoppage: String

    init {

        this.Ono = Ono!!
        this.PackedBoxes = PackedBoxes!!
        this.Stoppage = Stoppage!!


    }
    fun getOno(): String? {
        return Ono
    }
    fun setOno(Ono: String?) {
        this.Ono = Ono!!
    }

    fun getPackedBoxes():String?{
        return  PackedBoxes
    }
    fun setPackedBoxes(PackedBoxes: String?){

        this.PackedBoxes = PackedBoxes!!
    }
    fun getStoppage(): String? {
        return Stoppage
    }
    fun setStoppage(Stoppage: String?) {
        this.Stoppage = Stoppage!!
    }

}