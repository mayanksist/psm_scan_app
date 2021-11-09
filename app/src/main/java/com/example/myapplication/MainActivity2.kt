package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMain2Binding
import kotlinx.coroutines.Dispatchers.Main
import android.content.SharedPreferences

import android.preference.PreferenceManager




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
        val nav_Menu: Menu = navView.getMenu()
        if (usertype != "2") {
        nav_Menu.findItem(R.id.nav_product).setVisible(false);

        }



        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        val hview = navView.getHeaderView(0)

        val textViewName = hview.findViewById(R.id.emptype) as TextView
        val usename = hview.findViewById(R.id.username) as TextView
        //val getdetails = navView.findViewById(R.id.getdetailsc) as TextView
        textViewName.setText(epname)
        usename.setText(nameu)

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
            //Toast.makeText(this, "Android Menu is Clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
            return true

        }

        return super.onOptionsItemSelected(item)

    }



}


