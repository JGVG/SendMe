package com.j_gaby_1997.sendme.fragments.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.data.repository.Repository
import com.j_gaby_1997.sendme.services.createMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ChatViewModel (private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var _chat: MutableLiveData<MutableList<Message>> = MutableLiveData<MutableList<Message>>()
    val chat: LiveData<MutableList<Message>> get() = _chat
    var currentUser = repository.queryCurrentUser().value

    fun sendMessage(messageText: String, receiverEmail: String){
        val currentUserEmail = Firebase.auth.currentUser?.email.toString()

        FirebaseFirestore.getInstance().collection("CHAT").document("${currentUserEmail}_${receiverEmail}").get().addOnSuccessListener { doc ->
            if(doc.exists()){
                if(messageText != ""){
                    createMessage(
                        currentUserEmail,
                        receiverEmail,
                        Message(
                            currentUserEmail,
                            messageText,
                            false,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).toString(),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                        )
                    )

                    update(Firebase.auth.currentUser?.email.toString(), receiverEmail)
                }
            }else{
                if(messageText != ""){
                    createMessage(
                        receiverEmail,
                        currentUserEmail,
                        Message(
                            currentUserEmail,
                            messageText,
                            false,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).toString(),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                        )
                    )

                    update(Firebase.auth.currentUser?.email.toString(), receiverEmail)
                }
            }
        }
    }

    private fun update(senderEmail: String, receiverEmail: String){
        FirebaseFirestore.getInstance().collection("CHAT").document("${senderEmail}_${receiverEmail}").get().addOnSuccessListener { doc ->
            if(doc.exists()){
                db.collection("CHAT").document("${senderEmail}_${receiverEmail}").collection("MENSAJES")
                    .orderBy("fecha")
                    .addSnapshotListener { value, error ->
                        

                    }

            }else{
                db.collection("CHAT").document("${receiverEmail}_${senderEmail}").collection("MENSAJES")
                    .orderBy("fecha")
                    .get()
                    .addOnSuccessListener { results ->
                        val messageList: MutableList<Message> = mutableListOf()

                        for (result in results){
                            messageList.add(
                                Message(
                                    result.data["email"].toString(),
                                    result.data["mensaje"].toString(),
                                    result.data["estado"].toString().toBoolean(),
                                    result.data["fecha"].toString(),
                                    result.data["fecha_mini"].toString()
                                )
                            )
                        }
                        _chat.value = messageList
                    }
            }
        }
    }
}