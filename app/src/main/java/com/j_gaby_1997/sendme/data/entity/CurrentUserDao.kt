package com.j_gaby_1997.sendme.data.entity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrentUserDao {
    @Insert
    fun insertCurrentUser(currentUser: CurrentUser)

    @Query("DELETE FROM CURRENTUSER")
    fun deleteCurrentUser()

    @Query("SELECT * FROM CURRENTUSER")
    fun queryCurrentUser(): LiveData<CurrentUser>
}