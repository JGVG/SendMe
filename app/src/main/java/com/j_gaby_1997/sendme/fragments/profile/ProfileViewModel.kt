package com.j_gaby_1997.sendme.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.Repository
import com.j_gaby_1997.sendme.services.*
import kotlin.concurrent.thread

class ProfileViewModel(private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentUser: LiveData<CurrentUser> = repository.queryCurrentUser()

    private fun delete() {
        thread {
            repository.deleteCurrentUser()
        }
    } // Delete current user data.
    fun signOutUser() {
        signOut()
        delete()
    }

}
