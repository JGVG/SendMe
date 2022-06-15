package com.j_gaby_1997.sendme.fragments.chat

import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {
    var iconFlag = true

    fun changeIcon(){
        iconFlag = !iconFlag
    }

}
