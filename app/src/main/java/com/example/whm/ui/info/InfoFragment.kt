package com.example.whm.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.AppPreferences

class InfoFragment : Fragment() {
    lateinit var mView: View
    var Appversion: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_info, container, false)
        Appversion = mView.findViewById(R.id.AppV)
        Appversion!!.text = "App version : "+AppPreferences.AppVersion
        return  mView
    }

}