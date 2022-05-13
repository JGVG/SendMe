package com.j_gaby_1997.sendme.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.CurrentUserDao
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.services.*

@RequiresApi(Build.VERSION_CODES.O)
class LocalRepository(private val currentUserDao: CurrentUserDao) : Repository {
    override fun queryCurrentUser(): LiveData<CurrentUser> = currentUserDao.queryCurrentUser()
    override fun addCurrentUser(currentUser: CurrentUser) = currentUserDao.insertCurrentUser(currentUser)
    override fun deleteCurrentUser() = currentUserDao.deleteCurrentUser()

}