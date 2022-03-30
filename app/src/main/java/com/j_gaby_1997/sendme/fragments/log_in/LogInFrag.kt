package com.j_gaby_1997.sendme.fragments.log_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.LogInFragBinding
import com.j_gaby_1997.sendme.fragments.home.HomeFrag
import com.j_gaby_1997.sendme.fragments.sign_up.SignUpFrag
import com.j_gaby_1997.sendme.services.logInUser
import com.j_gaby_1997.sendme.services.signUpUser
import es.iessaladillo.pedrojoya.profile.utils.isValidEmail
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LogInFrag : Fragment(R.layout.log_in_frag){

    //private val viewModel: LogInViewModel by viewModels {}
    private var _b: LogInFragBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = LogInFragBinding.bind(requireView())
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        b.textNoHaveAccount.setOnClickListener { navigateToSignUp() }
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
            logInUser(b.edtEmail.text.toString(), b.edtPassword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    navigateToHome()
                }else{
                    Toast.makeText(requireActivity().application, R.string.error_message_log_in_auth, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeViewModel() {}

    // - Enable/Disable login button (for textWatchers methods) -
    private fun enableLogin() {
        b.floatingActionButton.isEnabled = b.edtPassword.text.toString().isNotEmpty() && b.edtEmail.toString().isNotEmpty() && b.edtEmail.text.toString().isValidEmail()
    }

    // - NAVIGATE METHODS -
    private fun navigateToSignUp() {
        requireActivity().supportFragmentManager.commit{
            setReorderingAllowed(true)
            replace(R.id.fcContent, SignUpFrag::class.java, null)
            addToBackStack(null)
        }
    }
    private fun navigateToHome() {
        requireActivity().supportFragmentManager.commit{
            setReorderingAllowed(true)
            replace(R.id.fcContent, HomeFrag::class.java, null)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}