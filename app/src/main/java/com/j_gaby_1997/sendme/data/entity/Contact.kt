package com.j_gaby_1997.sendme.data.entity

import com.google.firebase.Timestamp

data class Contact (
    var email: String,
    var avatarURL: String,
    var name: String,
    var lastMessage: String,
    var messageTime: Timestamp,
    var isOnline: Boolean,
    var haveNewMessage: Boolean,
){
    constructor() : this("", "", "","",Timestamp.now(),false,false)
}