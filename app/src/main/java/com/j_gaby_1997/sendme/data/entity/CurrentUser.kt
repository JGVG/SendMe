package com.j_gaby_1997.sendme.data.entity

import androidx.room.*

@Entity(
    tableName = "CURRENTUSER",
)

data class CurrentUser (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "USER_EMAIL")
    var email: String,

    @ColumnInfo(name = "USER_PASSWORD")
    var password: String,

    @ColumnInfo(name = "USER_REMEMBER")
    var remember: Boolean = false,
)