package com.j_gaby_1997.sendme.fragments.log_in

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.j_gaby_1997.sendme.ContactsActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.LogInFragBinding
import com.j_gaby_1997.sendme.fragments.sign_up.SignUpFrag
import es.iessaladillo.pedrojoya.profile.utils.isValidEmail

class LogInFrag : Fragment(R.layout.log_in_frag){

    private val viewModel: LogInViewModel by viewModels {
        LogInViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(requireContext()).currentUserDao), this)
    }
    private var _b: LogInFragBinding? = null
    private val b get() = _b!!
    private var returnedEmail = ""
    private var returnedPassword = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = LogInFragBinding.bind(requireView())

        //If you get a response from the 'sign in' fragment, it collects the data to log in.
        setFragmentResultListener("requestKey") { _, bundle ->
            b.edtEmail.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundleEmail"))
            b.edtPassword.text = Editable.Factory.getInstance().newEditable(bundle.getString("bundlePassword"))
        }
        setupViews()
    }

    private fun setupViews() {
        b.edtEmail.text = Editable.Factory.getInstance().newEditable(returnedEmail)
        b.edtPassword.text = Editable.Factory.getInstance().newEditable(returnedPassword)

        b.textNoHaveAccount.setOnClickListener { navigateToSignUpScreen() }
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
        b.floatingActionButton.setOnClickListener { login(b.edtEmail.text.toString(), b.edtPassword.text.toString()) }
    }


    private fun enableLogin() {
        b.floatingActionButton.isEnabled = b.edtPassword.text.toString().isNotEmpty() && b.edtEmail.toString().isNotEmpty() && b.edtEmail.text.toString().isValidEmail()
    }// Enable/Disable login button (for textWatchers methods)

    // - AUTH METHOD -
    private fun login(email:String, password: String) {
        viewModel.login(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                if(b.checkBox.isChecked){
                    viewModel.insertCurrentUser(CurrentUser(email,password))
                }else{
                    navigateToAppScreen(it.result?.user?.email?: "") //It will never be null
                }
            }else{
                Toast.makeText(requireActivity().application, R.string.error_message_log_in_auth, Toast.LENGTH_LONG).show()
            }
        }
    }

    // - NAVIGATE -
    private fun navigateToSignUpScreen() {
        requireActivity().supportFragmentManager.commit{
            setReorderingAllowed(true)
            replace(R.id.fcContent, SignUpFrag::class.java, null)
            addToBackStack(null)
        }
    }
    private fun navigateToAppScreen(email: String) {
        val appIntent = Intent(requireActivity().applicationContext, ContactsActivity::class.java).apply{
            putExtra("email", email)
        }
        startActivity(appIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

}