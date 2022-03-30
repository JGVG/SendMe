package com.j_gaby_1997.sendme.fragments.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.SignUpFragBinding
import com.j_gaby_1997.sendme.services.signUpUser
import es.iessaladillo.pedrojoya.profile.utils.isValidEmail
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpFrag : Fragment(R.layout.sign_up_frag){

    //private val viewModel: SignUpViewModel by viewModels {}
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
        b.floatingActionButton.setOnClickListener {
            signUpUser(b.edtEmail.text.toString(), b.edtPassword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    navigateToLogIn()
                }else{
                    Toast.makeText(requireActivity().application, R.string.error_message_sig_up_auth, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeViewModel() {}

    //- Enable/Disable login button (for textWatchers methods) -
    private fun enableLogin() {
        b.floatingActionButton.isEnabled = b.edtUsername.text.toString().isNotEmpty() && b.edtPassword.text.toString().isNotEmpty() && b.edtEmail.toString().isNotEmpty() && b.edtEmail.text.toString().isValidEmail()
    }

    // - NAVIGATE METHODS -
    private fun navigateToLogIn() {
        requireActivity().onBackPressed()
    }
    private fun navigateToHome(){}

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}