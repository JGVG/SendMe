package com.j_gaby_1997.sendme.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.HomeFragBinding
import com.j_gaby_1997.sendme.services.signOut

class HomeFrag : Fragment(R.layout.home_frag){
    //private val viewModel: HomeViewModel by viewModels {}
    private var _b: HomeFragBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = HomeFragBinding.bind(requireView())
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        b.floatingActionButton.setOnClickListener {
            signOut()
            navigateToLogIn()
        }
    }

    private fun observeViewModel() {}

    // - NAVIGATE METHODS -
    private fun navigateToLogIn() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}