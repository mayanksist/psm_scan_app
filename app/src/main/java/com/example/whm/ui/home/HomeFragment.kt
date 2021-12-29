package com.example.myapplication.com.example.whm.ui.home

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment()  {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var mView: View
    private var _binding: FragmentHomeBinding? = null
private var StatusD:Int=1
private var StatusR:Int=3
private var StatusS:Int=2
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    mView = inflater.inflate(com.example.myapplication.R.layout.fragment_home, container, false)
        mView.requestFocus()
        mView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    false
                }
                true
            }
            true
        })
    val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
    val btnInvCheck = mView.findViewById<Button>(com.example.myapplication.R.id.btnInvCheck)
    val btnDriLoad = mView.findViewById<Button>(com.example.myapplication.R.id.btnDriLoad)
    val btnUnLoadList = mView.findViewById<Button>(com.example.myapplication.R.id.btnunloadOrderlist)
    val btnOrderList = mView.findViewById<Button>(com.example.myapplication.R.id.btnOrderList)
    val btninventoryreceiv = mView.findViewById<Button>(com.example.myapplication.R.id.btninventoryreceiv)
    val btninventorypodraftlist = mView.findViewById<Button>(com.example.myapplication.R.id.btninventorypodraftlist)
    val btnsubmitpolist = mView.findViewById<Button>(com.example.myapplication.R.id.btnsubmitpolist)
    val btnrevertpolist = mView.findViewById<Button>(com.example.myapplication.R.id.btnrevertpolist)

    var Usertype = preferences.getString("EmpTypeNo", "")
        val editor = preferences.edit()




        if (Usertype.toString() != "2"){
        btnInvCheck.visibility=   View.GONE
    }
        if (Usertype.toString() == "9") {

            btnInvCheck.visibility=   View.VISIBLE
            btnDriLoad.visibility=   View.GONE
            btnUnLoadList.visibility=   View.GONE
            btnOrderList.visibility=   View.GONE

        }
        if (Usertype.toString() == "11") {
         //   btnInvCheck.visibility=   View.VISIBLE
            btnInvCheck.visibility=   View.VISIBLE
            btnDriLoad.visibility=   View.GONE
            btnUnLoadList.visibility=   View.GONE
            btnOrderList.visibility=   View.GONE
            btninventoryreceiv.visibility=   View.VISIBLE
            btninventorypodraftlist.visibility=   View.VISIBLE
            btnsubmitpolist.visibility=   View.VISIBLE
            btnrevertpolist.visibility=   View.VISIBLE

        }

    btnInvCheck.setOnClickListener {

        this.findNavController().navigate(com.example.myapplication.R.id.nav_product)
    }

    btnDriLoad.setOnClickListener {
        this.findNavController().navigate(com.example.myapplication.R.id.nav_productlist)
    }
    btnUnLoadList.setOnClickListener {
        this.findNavController().navigate(com.example.myapplication.R.id.nav_assignorder)
    }
    btnOrderList.setOnClickListener {
        this.findNavController().navigate(com.example.myapplication.R.id.nav_orderlist)
    }
        btninventoryreceiv.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_inventory)
        }
        btninventorypodraftlist.setOnClickListener {
            editor.putInt("StatusD", StatusD)
            editor.apply()
            this.findNavController().navigate(com.example.myapplication.R.id.nav_revertpolist)
        }
        btnsubmitpolist.setOnClickListener {
            editor.putInt("StatusD", StatusS)
            editor.apply()
            this.findNavController().navigate(com.example.myapplication.R.id.nav_revertpolist)
        }
        btnrevertpolist.setOnClickListener {
            editor.putInt("StatusD", StatusR)
            editor.apply()
            this.findNavController().navigate(com.example.myapplication.R.id.nav_revertpolist)
        }
        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


