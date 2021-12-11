package com.example.whm.ui.inventoryreceive
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.R
import android.view.Menu
import androidx.fragment.app.FragmentTransaction

class ReceivePO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)

        val toolbar: Toolbar
        toolbar = findViewById(com.example.myapplication.R.id.toolbara)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(com.example.myapplication.R.drawable.ic_baseline_arrow_back_24)
        toolbar?.title = Html.fromHtml("<font color='#e0e6e4'>PO Receive </font>")
        supportActionBar?.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener {
            val activity = this as AppCompatActivity
            super.onBackPressed()
        }


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(com.example.myapplication.R.menu.main_activity2, menu)
        return true
    }
}


