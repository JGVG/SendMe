package com.j_gaby_1997.sendme.fragments.sign_up

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.services.*

@RequiresApi(Build.VERSION_CODES.O)
class SignUpViewModel: ViewModel() {

    fun signIn(email:String, password: String) = signUpUser(email, password)
    fun createUser(email: String, name: String){
        createUserDoc(email, name)
    }
}