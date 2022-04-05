package com.j_gaby_1997.sendme.fragments.sign_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.SignUpFragBinding
import es.iessaladillo.pedrojoya.profile.utils.isValidEmail

class SignUpFrag : Fragment(R.layout.sign_up_frag){

    private val viewModel: SignUpViewModel by viewModels()
    private var _b: SignUpFragBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = SignUpFragBinding.bind(requireView())
        setupViews()
    }

    private fun setupViews() {

        b.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableSignUp()
            }
        })
        b.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableSignUp()
            }
        })
        b.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                enableSignUp()
            }
        })
        b.floatingActionButton.setOnClickListener { signUp() }
    }


    //- Enable/Disable sign up button (for textWatchers methods) -
    private fun enableSignUp() {
        b.floatingActionButton.isEnabled = b.edtUsername.text.toString().isNotEmpty() && b.edtPassword.text.toString().isNotEmpty() && b.edtEmail.toString().isNotEmpty() && b.edtEmail.text.toString().isValidEmail()
    }

    // - AUTH METHOD -
    @SuppressLint("ResourceType")
    private fun signUp() {
        viewModel.signIn(b.edtEmail.text.toString(), b.edtPassword.text.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                //Send email and password to login screem and set credentials for log in.
                setFragmentResult("requestKey", bundleOf("bundleEmail" to b.edtEmail.text.toString(), "bundlePassword" to b.edtPassword.text.toString()))
                navigateToLogIn()
            }else{
                Toast.makeText(requireActivity().application, R.string.error_message_sig_up_auth, Toast.LENGTH_LONG).show()
            }
        }
    }

    // - NAVIGATE METHODS -
    private fun navigateToLogIn() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}