package com.j_gaby_1997.sendme.fragments.log_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.databinding.LogInFragBinding
import com.j_gaby_1997.sendme.fragments.sign_up.Sign_Up_Frag

class Log_In_Frag : Fragment(R.layout.log_in_frag){

    //private val viewModel: Log_In_ViewModel by viewModels {}
    private var _b: LogInFragBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = LogInFragBinding.bind(requireView())
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        b.textNoHaveAccount.setOnClickListener { navigateToSignIn() }
        b.edtUsername.addTextChangedListener(object : TextWatcher {
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
    }

    /*
         - Enable/Disable login button (for textWatchers methods)
     */
    private fun enableLogin() {
        b.floatingActionButton.isEnabled = b.edtUsername.text.toString().isNotEmpty() && b.edtPassword.text.toString().isNotEmpty()
    }

    private fun navigateToSignIn() {
        requireActivity().supportFragmentManager.commit{
            setReorderingAllowed(true)
            replace(R.id.fcContent, Sign_Up_Frag::class.java, null)
            addToBackStack(null)
        }
    }

    private fun observeViewModel() {

    }

    private fun navigateToHome() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}