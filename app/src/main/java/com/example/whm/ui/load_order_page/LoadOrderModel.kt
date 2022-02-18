package com.example.myapplication.com.example.whm.ui.load_order_page

class LoadOrderModel(
    Ono: String, PackedBoxes: Int,
    Stoppage: String) {

    private  var Ono: String
    private  var PackedBoxes: Int
    private  var Stoppage: String

    init {
        this.Ono = Ono
        this.PackedBoxes = PackedBoxes
        this.Stoppage = Stoppage
    }
    fun getOno(): String? {
        return Ono
    }
    fun setOno(Ono: String?) {
        this.Ono = Ono!!
    }

    fun getPackedBoxes(): Int {
        return  PackedBoxes
    }
    fun setPackedBoxes(PackedBoxes: Int?){

        this.PackedBoxes = PackedBoxes!!
    }
    fun getStoppage(): String? {
        return Stoppage
    }
    fun setStoppage(Stoppage: String?) {
        this.Stoppage = Stoppage!!
    }

}