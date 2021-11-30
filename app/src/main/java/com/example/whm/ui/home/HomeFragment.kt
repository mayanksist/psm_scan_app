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

    var Usertype = preferences.getString("EmpTypeNo", "")
    if (Usertype.toString() != "2"){
        btnInvCheck.visibility=   View.GONE
    }
        if (Usertype.toString() == "9") {

            btnInvCheck.visibility=   View.VISIBLE
            btnDriLoad.visibility=   View.GONE
            btnUnLoadList.visibility=   View.GONE
            btnOrderList.visibility=   View.GONE

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


