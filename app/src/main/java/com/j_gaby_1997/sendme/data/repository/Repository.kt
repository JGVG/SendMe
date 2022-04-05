package com.j_gaby_1997.sendme.data.repository

import androidx.lifecycle.LiveData
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.entity.CurrentUser

interface Repository {

    fun queryAllContacts(): LiveData<List<Contact>>
    fun queryCurrentUser(): LiveData<CurrentUser>
    fun addCurrentUser(student: CurrentUser)
    fun deleteCurrentUser()

}