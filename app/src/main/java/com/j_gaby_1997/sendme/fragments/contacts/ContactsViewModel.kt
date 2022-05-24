package com.j_gaby_1997.sendme.fragments.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.data.repository.Repository

class ContactsViewModel : ViewModel() {

    private var _contacts: MutableLiveData<MutableList<Contact>> = MutableLiveData<MutableList<Contact>>()
    val contacts: LiveData<MutableList<Contact>> get() = _contacts

    fun getContacts(senderEmail: String){
        val db = FirebaseFirestore.getInstance()

        db.collection("CHAT")
            .whereEqualTo("emisor", senderEmail)
            .whereEqualTo("esContacto", true)
            .get()
            .addOnSuccessListener { results ->
                val searchList: MutableList<Contact> = mutableListOf()

                if (results != null) {
                    for (result in results){
                        val contactData = Contact()
                        val receiverEmail = result.data["receptor"].toString()

                        db.collection("USUARIOS").document(receiverEmail)
                            .get()
                            .addOnSuccessListener { user ->
                                contactData.email = user.data!!["email"].toString()
                                contactData.avatarURL = user.data!!["avatar_url"].toString()
                                contactData.name = user.data!!["nombre"].toString()
                                contactData.isOnline = user.data!!["estado_online"].toString().toBoolean()

                                db.collection("CHAT").document("${senderEmail}_${receiverEmail}")
                                    .collection("MENSAJES")
                                    .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                                    .get()
                                    .addOnSuccessListener { messages ->
                                        for (message in messages){
                                            contactData.lastMessage = message.data["mensaje"].toString()
                                            contactData.lastMessageTime = message.data["fecha_mini"].toString()
                                            contactData.haveNewMessage = message.data["estado"].toString().toBoolean()
                                        }
                                        searchList.add(contactData)
                                        _contacts.value = searchList
                                    }
                            }
                    }

                }

            }

    }


    fun deleteContacts(selecteds: MutableList<Contact>, email:String) {
        //firebase.deleteContact(selecteds, email)
    }

}