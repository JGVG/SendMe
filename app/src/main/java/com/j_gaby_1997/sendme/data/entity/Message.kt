package com.j_gaby_1997.sendme.data.entity

import com.google.firebase.Timestamp

data class Message (
    var email: String,
    var message_text: String,
    var state: Boolean,
    var date: Timestamp,
)