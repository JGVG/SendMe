package com.j_gaby_1997.sendme.data.entity

data class User(
    var email: String,
    var name: String,
    var avatarURL: String,
    var description: String,
    var location: String,
    var dischargeDate: String,
    var webSiteURL: String,
){
    constructor() : this("", "", "","","","","")
}