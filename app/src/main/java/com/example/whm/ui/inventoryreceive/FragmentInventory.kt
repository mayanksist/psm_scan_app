package com.example.whm.ui.inventoryreceive
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentInventoryFragmentBinding
import android.widget.Button
import java.util.*

import android.widget.EditText
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.text.Editable

import java.sql.Time
import android.widget.DatePicker
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