package com.example.myapplication.com.example.whm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.com.example.whm.ui.gallery.GalleryFragment

import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.product.ProductList


class HomeFragment : Fragment() {

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

        val btnInvCheck = mView.findViewById<Button>(com.example.myapplication.R.id.btnInvCheck)
        val btnDriLoad = mView.findViewById<Button>(com.example.myapplication.R.id.btnDriLoad)

        btnInvCheck.setOnClickListener {

            childFragmentManager.beginTransaction()
                .replace(com.example.myapplication.R.id.mainDashboard, GalleryFragment())
                .commit()
        }

        btnDriLoad.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(com.example.myapplication.R.id.mainDashboard, ProductList())
                .commit()
        }


        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


