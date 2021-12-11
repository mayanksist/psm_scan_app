package com.example.whm.ui.inventoryreceive
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentInventoryFragmentBinding
import java.util.*

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.text.Editable
import android.widget.*

import java.sql.Time
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import java.text.SimpleDateFormat


class FragmentInventory  : Fragment(R.layout.fragment_inventory_fragment){
    private var _binding: FragmentInventoryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInventoryFragmentBinding.bind(view)

        binding.apply {
            txtbildate.setOnClickListener {
                // create new instance of DatePickerFragment
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        txtbildate.text =  date
                    }
                }

                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}