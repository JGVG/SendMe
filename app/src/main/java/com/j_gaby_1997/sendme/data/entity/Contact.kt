package com.j_gaby_1997.sendme.data.entity

import java.time.LocalDateTime

data class Contact (
    var email: String,
    var avatarURL: String,
    var name: String,
    var lastMessage: String,
    var lastMessageTime: String,
    var isOnline: Boolean,
    var haveNewMessage: Boolean,
){
    constructor() : this("", "", "","","",false,false)
}