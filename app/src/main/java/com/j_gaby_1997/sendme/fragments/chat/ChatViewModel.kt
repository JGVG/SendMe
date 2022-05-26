package com.j_gaby_1997.sendme.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.services.checkChatDoc
import com.j_gaby_1997.sendme.services.createMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var _chat: MutableLiveData<MutableList<Message>> = MutableLiveData<MutableList<Message>>()
    val chat: LiveData<MutableList<Message>> get() = _chat

    fun sendMessage(messageText: String, senderEmail:String, receiverEmail: String){

        if(messageText != ""){
            createMessage(
                senderEmail,
                receiverEmail,
                Message(senderEmail, messageText, false, Timestamp.now())
            )
        }
    }
    fun update(senderEmail: String, receiverEmail: String){

        db.collection("CHAT").document("${senderEmail}_${receiverEmail}").collection("MENSAJES")
            .addSnapshotListener { messages, _ ->
                val messageList: MutableList<Message> = mutableListOf()

                if (messages != null) {
                    for (message in messages) {
                        messageList.add(
                            Message(
                                message.data["email"].toString(),
                                message.data["mensaje"].toString(),
                                message.data["estado"].toString().toBoolean(),
                                message.data["fecha"] as Timestamp
                            )
                        )
                    }
                    messageList.sortBy {
                        it.date
                    }
                    _chat.value = messageList
                }
            }
    }
    fun instance(senderEmail: String, receiverEmail: String){
        checkChatDoc(senderEmail,receiverEmail)
    }

}