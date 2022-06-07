package com.j_gaby_1997.sendme.fragments.contacts

import android.os.Build
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

    fun deleteContacts(selecteds: MutableList<String>, senderEmail:String) {
        deleteContact(selecteds, senderEmail)
    }

}