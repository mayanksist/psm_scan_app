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
import android.widget.Toast
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

class FragmentInventory : Fragment(){
    private var _binding: FragmentInventoryFragmentBinding?=null
    private val binding get() = _binding!!

    lateinit var mView: View
    private lateinit var viewModel: FragmentInventoryViewModel
    lateinit var textView: TextView
    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView= inflater.inflate(R.layout.fragment_inventory_fragment, container, false)
        _binding = FragmentInventoryFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(
            R.layout.fragment_inventory_fragment,
            container,
            false
        )

//        val Buttonpicker = view.findViewById<Button>(R.id.btndatepick)
//
//        Buttonpicker.setOnClickListener {
//            val now=Calendar.getInstance()
//            val pickerdate=DatePickerDialog(this@FragmentInventory,DatePickerDialog.OnDateSetListener{
//                view, year, monthOfYear, dayOfMonth ->
//            },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
//            pickerdate.show()
//
//        }


        return mView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentInventoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}