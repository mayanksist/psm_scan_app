package com.example.myapplication.com.example.whm

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMain2Binding
import com.google.android.material.navigation.NavigationView


class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
   //var usertype: android.R.stringtring()
    var usertype: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val epname = intent.getStringExtra("empname")
        val nameu = intent.getStringExtra("Name")
        val EmpTypeNo = intent.getStringExtra("EmpTypeNo")
        val aotoidemp = intent.getStringExtra("empid")
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putString("EmpAutoId", aotoidemp)
        editor.apply()



        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        usertype = EmpTypeNo.toString()
       //Toast.makeText(applicationContext, aotoidemp, Toast.LENGTH_LONG).show()
        val nav_Menu: Menu = navView.menu
        if (usertype != "2") {
            nav_Menu.findItem(R.id.nav_product).isVisible = false

        }


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val hview = navView.getHeaderView(0)

        val textViewName = hview.findViewById(R.id.emptype) as TextView
        val usename = hview.findViewById(R.id.username) as TextView
        val AppVersion = hview.findViewById(R.id.txtVersion) as TextView
        AppVersion.text = "version : " + AppPreferences.AppVersion
        textViewName.text = epname
        usename.text = nameu

        appBarConfiguration = AppBarConfiguration(

            setOf(

                R.id.nav_home, R.id.nav_slideshow, R.id.nav_product, R.id.nav_productlist
            ), drawerLayout

        )
        setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {


            val navController = findNavController(R.id.nav_host_fragment_content_main)
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        //noinspection SimplifiableIfStatement

        if (id == R.id.nav_home) {

            val intent = Intent(this, MainActivity2::class.java)
            this.startActivity(intent)
            return true
        }



        if (id == R.id.nav_slideshow) {
//
//            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Are you sure?")
//                .setContentText("Won't be able to recover this file!")
//                .setConfirmText("Yes,delete it!")
//                .setConfirmClickListener { sDialog -> // Showing simple toast message to user
//                    Toast.makeText(this, " You Clicked me ", Toast.LENGTH_SHORT).show()
//                    sDialog.dismissWithAnimation()
//                }.show()
//
//
//            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Are you sure?")
//                .setContentText("You want to logout")
//                .setConfirmText("Logout")
//                .setConfirmClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
//        }
//                .setCancelButton(
//                    "Cancel"
//                ) { sDialog -> sDialog.dismissWithAnimation() }
//                .show()
//
//            Toast.makeText(this, "Android Menu is Clicked", Toast.LENGTH_LONG).show()

            return true

        }

        return super.onOptionsItemSelected(item)

    }



}


