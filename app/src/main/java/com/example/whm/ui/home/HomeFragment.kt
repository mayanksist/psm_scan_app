package com.example.myapplication.com.example.whm.ui.home

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.myapplication.databinding.FragmentHomeBinding



class HomeFragment : Fragment()  {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var mView: View
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    mView = inflater.inflate(com.example.myapplication.R.layout.fragment_home, container, false)
    val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
    val btnInvCheck = mView.findViewById<Button>(com.example.myapplication.R.id.btnInvCheck)
    val btnDriLoad = mView.findViewById<Button>(com.example.myapplication.R.id.btnDriLoad)
    val btnUnLoadList = mView.findViewById<Button>(com.example.myapplication.R.id.btnunloadOrderlist)
    val btnOrderList = mView.findViewById<Button>(com.example.myapplication.R.id.btnOrderList)

    var Usertype = preferences.getString("EmpTypeNo", "")
    if (Usertype.toString() != "2"){
        btnInvCheck.visibility=   View.GONE
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


        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


