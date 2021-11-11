package com.example.myapplication.com.example.whm
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
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
        if (usertype != "2") {
            nav_Menu.findItem(R.id.nav_product).isVisible = false

        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val hview = navView.getHeaderView(0)

        val textViewName = hview.findViewById(R.id.emptype) as TextView
        val usename = hview.findViewById(R.id.username) as TextView
        val AppVersion = hview.findViewById(R.id.txtVersion) as TextView
        AppVersion.text = "version : " + AppPreferences.AppVersion
        textViewName.text = preferences.getString("Empname","")
        usename.text =  preferences.getString("Username","")

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

}


