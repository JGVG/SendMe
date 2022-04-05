package com.j_gaby_1997.sendme.fragments.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.repository.Repository
import kotlin.concurrent.thread

private const val STATE_CONTACT_INDEX = "STATE_CONTACT_INDEX"

class ContactsViewModel(private val repository: Repository, savedStateHandle: SavedStateHandle) : ViewModel() {

    val contacts: LiveData<List<Contact>> = savedStateHandle.getLiveData(STATE_CONTACT_INDEX, repository.queryAllContacts().value)

    fun delete() {
        thread {
            repository.deleteCurrentUser()
        }
    }

}