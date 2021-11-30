package com.example.myapplication.com.example.whm

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMain2Binding
import com.google.android.material.navigation.NavigationView
import android.R
import android.view.MenuItem
import android.view.View


class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
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
        editor.putString("EmpTypeNo", EmpTypeNo)
        editor.putString("Empname", epname)
        editor.putString("Username", nameu)
        editor.apply()
         binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        usertype = EmpTypeNo.toString()
        val nav_Menu: Menu = navView.menu
        nav_Menu.findItem(com.example.myapplication.R.id.nav_loadorder).isVisible = false
        if (usertype != "2") {
            nav_Menu.findItem(com.example.myapplication.R.id.nav_product).isVisible = false

        }

        val navigationView = findViewById<View>(com.example.myapplication.R.id.nav_view) as NavigationView

        val menu = navigationView.menu

        val tools: MenuItem = menu.findItem(com.example.myapplication.R.id.version)
        tools.title = "Version : "+AppPreferences.AppVersion

        if (usertype == "9") {
            nav_Menu.findItem(com.example.myapplication.R.id.nav_productlist).isVisible = false
            nav_Menu.findItem(com.example.myapplication.R.id.nav_orderlist).isVisible = false
            nav_Menu.findItem(com.example.myapplication.R.id.nav_assignorder).isVisible = false
            nav_Menu.findItem(com.example.myapplication.R.id.nav_product).isVisible = true

        }
        val navController = findNavController(com.example.myapplication.R.id.nav_host_fragment_content_main)
        val hview = navView.getHeaderView(0)
        val textViewName = hview.findViewById(com.example.myapplication.R.id.emptype) as TextView
        val usename = hview.findViewById(com.example.myapplication.R.id.username) as TextView
        textViewName.text = preferences.getString("Empname","") +" ["+ preferences.getString("LName","")+"]"
        usename.text =  preferences.getString("Username","")
        appBarConfiguration = AppBarConfiguration(
            setOf(
                com.example.myapplication.R.id.nav_home, com.example.myapplication.R.id.nav_slideshow, com.example.myapplication.R.id.nav_product,
                com.example.myapplication.R.id.nav_productlist,
                com.example.myapplication.R.id.nav_assignorder,
                com.example.myapplication.R.id.nav_orderlist
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.example.myapplication.R.menu.main_activity2, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
            val navController = findNavController(com.example.myapplication.R.id.nav_host_fragment_content_main)
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onKeyDown(key_code: Int, key_event: KeyEvent?): Boolean {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event)
            return false
        }
        return true
    }
}


