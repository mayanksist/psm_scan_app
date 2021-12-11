package com.example.whm.ui.inventoryreceive
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.R
import android.app.PendingIntent.getActivity
import android.view.Menu
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import android.content.Intent
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.home.HomeFragment







class ReceivePO : AppCompatActivity() {
     var backBTN: ImageView?=null
    var layoutId: FrameLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_receive_po)
        backBTN = findViewById(com.example.myapplication.R.id.back)
//        layoutId = findViewById(com.example.myapplication.R.id.layoutpo)
//        val toolbar: Toolbar
//        toolbar = findViewById(com.example.myapplication.R.id.toolbara)
//        setSupportActionBar(toolbar)
//        toolbar.setNavigationIcon(com.example.myapplication.R.drawable.ic_baseline_arrow_back_24)
//        toolbar?.title = Html.fromHtml("<font color='#e0e6e4'>PO Receive </font>")
//        supportActionBar?.setDisplayShowTitleEnabled(false);

        backBTN?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                var intent = Intent(applicationContext, FragmentInventory::class.java)
                startActivity(intent)
//                val ft = supportFragmentManager.beginTransaction()
//                ft.replace(com.example.myapplication.R.layout.activity_receive_po, FragmentInventory())
//                ft.addToBackStack(null)
//                ft.commit()

            }


        })



    }

}



