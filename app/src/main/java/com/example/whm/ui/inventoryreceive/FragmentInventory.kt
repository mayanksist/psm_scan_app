package com.example.whm.ui.inventoryreceive

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.ui.inventoryreceive.DatePickerFragment
import com.example.myapplication.databinding.FragmentInventoryFragmentBinding
import kotlinx.coroutines.Dispatchers.Main
import java.util.*


class FragmentInventory  : Fragment(R.layout.fragment_inventory_fragment){
    private var _binding: FragmentInventoryFragmentBinding? = null
    private val binding get() = _binding!!
    val types = arrayOf("simple User", "Admin")
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
            autoCompleteTextView.adapter = context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, types) } as SpinnerAdapter
            autoCompleteTextView.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("erreur")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val type = parent?.getItemAtPosition(position).toString()
//                    Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                    println(type)
                }

            }

            btnnext.setOnClickListener {
                val intent = Intent (context, ReceivePO::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}