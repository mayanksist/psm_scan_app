package com.example.myapplication.com.example.whm.ui.slideshow
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myapplication.R
import com.example.myapplication.com.example.whm.MainActivity
import com.example.myapplication.databinding.FragmentSlideshowBinding

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
        var Logoutalert =SweetAlertDialog(this.context, SweetAlertDialog.WARNING_TYPE)
        Logoutalert.titleText = "Are you sure?"
        Logoutalert.contentText = "You want to logout."
        Logoutalert.confirmText = "Logout"
        Logoutalert.setConfirmClickListener {
                val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
                requireActivity().startActivity(Intent(this.context, MainActivity::class.java))
                requireActivity().finish()

            }
        Logoutalert.setCancelButton(
                "Cancel"
            ) {
                    sDialog -> sDialog.dismissWithAnimation()
                this.findNavController().navigate(R.id.nav_home)
            }
        Logoutalert.setCanceledOnTouchOutside(false)
        Logoutalert.show()
        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}