package com.j_gaby_1997.sendme.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.User

interface Repository {

    // - REPOSITORY METHODS -
    fun queryCurrentUser(): LiveData<CurrentUser>
    fun addCurrentUser(currentUser: CurrentUser)
    fun deleteCurrentUser()
}