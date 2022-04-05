package com.j_gaby_1997.sendme.fragments.sign_up

import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.services.signUpUser

class SignUpViewModel: ViewModel() {

    fun signIn(email:String, password: String) = signUpUser(email, password)

}