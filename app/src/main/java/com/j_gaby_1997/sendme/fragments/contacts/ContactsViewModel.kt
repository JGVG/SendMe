package com.j_gaby_1997.sendme.fragments.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.repository.Repository

private const val STATE_CONTACT_INDEX = "STATE_CONTACT_INDEX"

class ContactsViewModel(private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _contacts: MutableLiveData<List<Contact>> = savedStateHandle.getLiveData(STATE_CONTACT_INDEX, repository.queryAllContacts().value)
    val contacts: LiveData<List<Contact>> get() = _contacts

    fun deleteContacts(selecteds: MutableList<Contact>, email:String) {
        //firebase.deleteContact(selecteds, email)
    }

}