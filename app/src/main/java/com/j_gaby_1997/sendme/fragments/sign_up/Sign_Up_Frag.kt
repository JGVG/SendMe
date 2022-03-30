package com.j_gaby_1997.sendme.fragments.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.SignUpFragBinding
import com.j_gaby_1997.sendme.fragments.log_in.Log_In_Frag
import es.iessaladillo.pedrojoya.profile.utils.isValidEmail

class Sign_Up_Frag : Fragment(R.layout.sign_up_frag){

    //private val viewModel: Sign_Up_ViewModel by viewModels {}
    private var _b: SignUpFragBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = SignUpFragBinding.bind(requireView())
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        b.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableLogin()
            }
        })
        b.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableLogin()
            }
        })
        b.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableLogin()
            }
        })
        b.floatingActionButton.setOnClickListener { navigateToLogIn() }
    }

    private fun observeViewModel() {}

    /*
        - Enable/Disable login button (for textWatchers methods)
    */
    private fun enableLogin() {
        b.floatingActionButton.isEnabled = b.edtUsername.text.toString().isNotEmpty() && b.edtPassword.text.toString().isNotEmpty() && b.edtEmail.toString().isNotEmpty() && b.edtEmail.text.toString().isValidEmail()
    }

    private fun navigateToLogIn() {
        requireActivity().onBackPressed()
    }

    private fun navigateToHome(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}