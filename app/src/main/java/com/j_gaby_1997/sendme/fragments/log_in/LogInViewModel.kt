package com.j_gaby_1997.sendme.fragments.log_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.repository.Repository
import com.j_gaby_1997.sendme.services.logInUser
import kotlin.concurrent.thread

class LogInViewModel( val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentUser: LiveData<CurrentUser> = repository.queryCurrentUser()

    fun insertCurrentUser(currentUser: CurrentUser) {
        thread {
            repository.deleteCurrentUser()
            repository.addCurrentUser(currentUser)
        }
    }

    fun login(email:String, password: String) = logInUser(email, password)


}