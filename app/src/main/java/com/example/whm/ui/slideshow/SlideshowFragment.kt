package com.example.myapplication.com.example.whm.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentSlideshowBinding
import cn.pedant.SweetAlert.SweetAlertDialog

import android.content.Intent
import android.preference.PreferenceManager
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.MainActivity
import com.example.myapplication.com.example.whm.MainActivity2
import com.example.myapplication.com.example.whm.ui.home.HomeFragment





class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null
    lateinit var mView: View
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_slideshow, container, false)

        SweetAlertDialog(this.context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("You want to logout.")
            .setConfirmText("Logout")
            .setConfirmClickListener {
                requireActivity().startActivity(Intent(this.context, MainActivity::class.java))
                requireActivity().finish()

            }
            .setCancelButton(
                "Cancel"
            ) {
                    sDialog -> sDialog.dismissWithAnimation()
                this.findNavController().navigate(R.id.nav_home)
            }
            .show()
        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}