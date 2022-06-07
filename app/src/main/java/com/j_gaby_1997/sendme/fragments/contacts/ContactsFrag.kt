package com.j_gaby_1997.sendme.fragments.contacts

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.j_gaby_1997.sendme.ChatActivity
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.SearchActivity
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentContactsBinding
import com.j_gaby_1997.sendme.fragments.detail.ContactDetailDlg
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class ContactsFrag : Fragment(R.layout.fragment_contacts) {

    private var _b: FragmentContactsBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }
    private val viewModel: ContactsViewModel by viewModels()
    private val listAdapter: ContactsAdapter = ContactsAdapter().apply {
        setOnItemClickListenerToDetail { showDetailDialog(getItem(it)) }
        setOnItemClickListenerToChat {
            if(deleteMode){
                navigateToChatScreen(getItem(it).email)
            }
        }
        setOnItemLongClickListenerToDeleteMode {
            onDeleteModeChange()
            checkDeleteMode()
        }
    }
    private lateinit var registration: ListenerRegistration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentContactsBinding.bind(requireView())
    }
    override fun onStart() {
        super.onStart()
        setupViews()
        observeContacts()
        setUpRecycledView()
    }

    // - SETUPS -
    private fun setupViews() {

        checkDeleteMode()
        b.fabNormal.setOnClickListener { navidateToSearchScreen(USEREMAIL) }
        b.fabDelete.setOnClickListener { deleteSelectedContacts() }
        b.buttonProfile.setOnClickListener { navigateToProfileScreen(USEREMAIL) }
    }
    private fun setUpRecycledView() {
        b.lstContacts.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = listAdapter
        }
    }
    private fun observeContacts() {
        val usersRef = FirebaseFirestore.getInstance().collection("USUARIOS")
        val contactRef = usersRef.document(USEREMAIL).collection("CONTACTS")

        registration = contactRef.addSnapshotListener { contacts, _ ->
            val searchList: MutableList<Contact> = mutableListOf()

            if (contacts != null) {
                if (contacts.isEmpty){
                    listAdapter.submitList(searchList)

                }else{
                    for (contact in contacts){
                        val receiverEmail = contact.id
                        val contactData = Contact()

                        //Build a contact (user info)
                        usersRef.document(receiverEmail)
                            .get()
                            .addOnSuccessListener { user ->

                                contactData.email = user.data!!["email"].toString()
                                contactData.avatarURL = user.data!!["avatar_url"].toString()
                                contactData.name = user.data!!["nombre"].toString()
                                contactData.isOnline = user.data!!["estado_online"].toString().toBoolean()

                                //Build a contact (chat info)
                                contactRef.document(receiverEmail).collection("MENSAJES")
                                    .orderBy("fecha", Query.Direction.DESCENDING).limit(1)
                                    .get()
                                    .addOnSuccessListener { messages ->
                                        for (message in messages){
                                            contactData.lastMessage = message.data["mensaje"].toString()
                                            contactData.haveNewMessage = message.data["estado"].toString().toBoolean()
                                            contactData.messageTime = message.data["fecha"] as Timestamp

                                            searchList.add(contactData)
                                        }

                                        searchList.sortByDescending {
                                            it.messageTime
                                        }

                                        //Set contacts in liveData
                                        listAdapter.submitList(searchList)
                                    }
                            }
                    }
                }
            }
        }
    }

    // - NAVIGATE -
    private fun navigateToChatScreen( email: String ) {
        val appIntent = Intent(requireActivity().applicationContext, ChatActivity::class.java).apply{
            putExtra("email", email)
        }
        startActivity(appIntent)
    }
    private fun navigateToProfileScreen( email :String ){
        val appIntent = Intent(requireActivity().applicationContext, ProfileActivity::class.java).apply{
            putExtra("email", email)
        }
        startActivity(appIntent)
    }
    private fun navidateToSearchScreen(email :String){
        val appIntent = Intent(requireActivity().applicationContext, SearchActivity::class.java).apply{
            putExtra("email", email)
        }
        startActivity(appIntent)
    }
    private fun showDetailDialog( contact: Contact ) {
        setFragmentResult("requestKey", bundleOf(
            "bundleAvatar" to contact.avatarURL,
            "bundleName" to contact.name,
            "bundleEmail" to contact.email
        ))
        ContactDetailDlg().show(requireActivity().supportFragmentManager, "customDialog")
    } //Display a dialog for the details of the selected contact.

    // - METHODS -
    private fun checkDeleteMode() {
        if(listAdapter.deleteMode){
            b.fabNormal.visibility = View.VISIBLE
            b.fabDelete.visibility = View.GONE
        }else{
            b.fabNormal.visibility = View.GONE
            b.fabDelete.visibility = View.VISIBLE
        }
    } // Make UI changes for deletion mode.
    private fun deleteSelectedContacts() {
        val selecteds = listAdapter.selectedContacts
        listAdapter.onDeleteModeChange()
        checkDeleteMode()

        viewModel.deleteContacts(selecteds, USEREMAIL)
    } //Gets a list of the contacts selected for deletion.

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
        registration.remove()
    }

    companion object {
        fun newInstance(USEREMAIL: String): ContactsFrag = ContactsFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}