package com.j_gaby_1997.sendme.fragments.contacts

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.services.deleteContact

@RequiresApi(Build.VERSION_CODES.O)
class ContactsViewModel : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    private var _contacts: MutableLiveData<MutableList<Contact>> = MutableLiveData<MutableList<Contact>>()
    val contacts: LiveData<MutableList<Contact>> get() = _contacts

    fun getContacts(senderEmail: String){

        db.collection("CHAT")
            .whereEqualTo("emisor", senderEmail)
            .whereEqualTo("esContacto", true)
            .addSnapshotListener { results, _ ->
                val searchList: MutableList<Contact> = mutableListOf()

                if (results != null) {
                    for (result in results){
                        val receiverEmail = result.data["receptor"].toString()

                        db.collection("USUARIOS").document(receiverEmail)
                            .get()
                            .addOnSuccessListener { user ->
                                val contactData = Contact()

                                contactData.email = user.data!!["email"].toString()
                                contactData.avatarURL = user.data!!["avatar_url"].toString()
                                contactData.name = user.data!!["nombre"].toString()
                                contactData.isOnline = user.data!!["estado_online"].toString().toBoolean()

                                db.collection("CHAT").document("${senderEmail}_${receiverEmail}").collection("MENSAJES")
                                    .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                                    .get()
                                    .addOnSuccessListener { messages ->
                                        for (message in messages){
                                            contactData.lastMessage = message.data["mensaje"].toString()
                                            contactData.haveNewMessage = message.data["estado"].toString().toBoolean()
                                            contactData.messageTime = message.data["fecha"] as Timestamp

                                            searchList.add(contactData)
                                            searchList.sortByDescending {
                                                it.messageTime
                                            }

                                            Log.d("RESPUESTA EN EL VIEWMODEL", searchList.toString())
                                            _contacts.value = searchList

                                        }
                                    }

                            }

                    }

                }
        }

    }



    fun deleteContacts(selecteds: MutableList<String>, senderEmail:String) {
        deleteContact(selecteds, senderEmail)
    }

}