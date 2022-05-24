package com.j_gaby_1997.sendme.fragments.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.services.checkChatDoc
import com.j_gaby_1997.sendme.services.createMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var _chat: MutableLiveData<MutableList<Message>> = MutableLiveData<MutableList<Message>>()
    val chat: LiveData<MutableList<Message>> get() = _chat

    fun sendMessage(messageText: String, senderEmail:String, receiverEmail: String){
        val date =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")).toString()
        val miniDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString()

        createMessage(
            senderEmail,
            receiverEmail,
            Message(senderEmail, messageText, false, miniDate, date,)
        )
    }

    fun update(senderEmail: String, receiverEmail: String){
        val collection = db.collection("CHAT").document("${senderEmail}_${receiverEmail}")

        collection.addSnapshotListener(MetadataChanges.INCLUDE) { doc, _ ->
            if (doc!!.exists()) {
                collection
                    .collection("MENSAJES")
                    .orderBy("fecha")
                    .get()
                    .addOnSuccessListener { messages ->
                        val messageList: MutableList<Message> = mutableListOf()

                        for (message in messages) {
                                messageList.add(
                                    Message(
                                        message.data["email"].toString(),
                                        message.data["mensaje"].toString(),
                                        message.data["estado"].toString().toBoolean(),
                                        message.data["fecha"].toString(),
                                        message.data["fecha_mini"].toString()
                                    )
                                )
                            }
                        _chat.value = messageList
                    }
            }
        }
    }

    fun instance(senderEmail: String, receiverEmail: String){
        checkChatDoc(senderEmail,receiverEmail)
    }

}