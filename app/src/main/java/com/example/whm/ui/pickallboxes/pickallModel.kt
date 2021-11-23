package com.example.myapplication.com.example.whm.ui.pickallboxes


class AllpickBoxes(
    Ono: String) {

    private var Ono: String


    init {
        this.Ono = Ono

    }

    fun getOno(): String? {
        return Ono
    }

    fun setOno(Ono: String?) {
        this.Ono = Ono!!
    }


}