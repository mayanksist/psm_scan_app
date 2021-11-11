package com.example.myapplication.com.example.whm.ui.home

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.com.example.whm.AppPreferences
import com.example.myapplication.com.example.whm.ui.gallery.GalleryFragment

import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.product.ProductList
import com.google.android.material.navigation.NavigationView
import android.R





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
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val btnInvCheck = mView.findViewById<Button>(com.example.myapplication.R.id.btnInvCheck)
        val btnDriLoad = mView.findViewById<Button>(com.example.myapplication.R.id.btnDriLoad)

        var Usertype = preferences.getString("EmpTypeNo", "")
        if (Usertype.toString() != "2"){
            btnInvCheck.visibility=   View.INVISIBLE
        }



        btnInvCheck.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_product)
        }

        btnDriLoad.setOnClickListener {
            this.findNavController().navigate(com.example.myapplication.R.id.nav_productlist)
        }


        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


