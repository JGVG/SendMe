package com.j_gaby_1997.sendme.data

import android.content.Context
import androidx.room.*
import com.j_gaby_1997.sendme.data.entity.CurrentUser
import com.j_gaby_1997.sendme.data.entity.CurrentUserDao

@Database(entities = [CurrentUser::class], version = 1)
abstract class CurrentUserDatabase : RoomDatabase() {

    abstract val currentUserDao: CurrentUserDao

    companion object {

        @Volatile
        private var INSTANCE: CurrentUserDatabase? = null

        fun getInstance(context: Context): CurrentUserDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CurrentUserDatabase::class.java,
                            "current_user_database",
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}