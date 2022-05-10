package com.j_gaby_1997.sendme.fragments.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.DlgLoadingBinding

class LoadingDlg : DialogFragment(R.layout.dlg_loading) {

    private var _b: DlgLoadingBinding? = null
    private val b get() = _b!!
    private lateinit var rotate: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = DlgLoadingBinding.bind(requireView())
        setupViews()
    }

    // - SETUPS
    private fun setupViews(){
        rotate = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        b.view.startAnimation(rotate)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}