package com.j_gaby_1997.sendme.fragments.contacts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.ContactsFragBinding
import com.j_gaby_1997.sendme.fragments.contact_detail.ContactDetailDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

class ContactsFrag : Fragment(R.layout.contacts_frag) {

    private var _b: ContactsFragBinding? = null
    private val b get() = _b!!

    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }
    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(requireContext()).currentUserDao), this)
    }
    private val listAdapter: ContactsAdapter = ContactsAdapter().apply {
        setOnItemClickListenerToDetail { showDetailDialog(getItem(it)) }
        setOnItemClickListenerToChat {
            if(deleteMode){
                navigateToChatScreen(getItem(it))
            }
        }
        setOnItemLongClickListenerToDeleteMode {
            onDeleteModeChange()
            checkDeleteMode()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = ContactsFragBinding.bind(requireView())
        setupViews()
        observeContacts()
        setUpRecycledView()
    }

    // - SETUPS -
    private fun setupViews() {
        checkDeleteMode()
        b.fabNormal.setOnClickListener { navidateToSearchScreen() }
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
        viewModel.contacts.observe(viewLifecycleOwner){
            listAdapter.submitList( it )
        }
    }
    private fun checkDeleteMode() {
        if(listAdapter.deleteMode){
            b.fabNormal.visibility = View.VISIBLE
            b.fabDelete.visibility = View.GONE
        }else{
            b.fabNormal.visibility = View.GONE
            b.fabDelete.visibility = View.VISIBLE
        }
    } // Make UI changes for deletion mode.

    // - METHODS -
    private fun deleteSelectedContacts() {
        val selecteds = listAdapter.selectedContacts
        viewModel.deleteContacts(selecteds)

        Toast.makeText(requireActivity().application,"Contacts deleted: "+ selecteds.size, Toast.LENGTH_LONG).show()
    } //Gets a list of the contacts selected for deletion.
    private fun showDetailDialog(contact: Contact) {
        setFragmentResult("requestKey", bundleOf("bundleAvatar" to contact.avatarURL, "bundleName" to contact.name))
        ContactDetailDlg().show(requireActivity().supportFragmentManager, "customDialog")
    } //Display a dialog for the details of the selected contact.

    // - NAVIGATE -
    private fun navigateToChatScreen(contact: Contact) {
        Toast.makeText(requireActivity().application, "To chat with: "+ contact.name, Toast.LENGTH_LONG).show()
    }
    private fun navigateToProfileScreen(USEREMAIL:String){
        // Navigate to profile activity with intents
        Toast.makeText(requireActivity().application, "Go to profile screen\n$USEREMAIL", Toast.LENGTH_LONG).show()
    }
    private fun navidateToSearchScreen(){
        // Navigate to search activity
        Toast.makeText(requireActivity().application, "Go to search screen", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance(USEREMAIL: String): ContactsFrag = ContactsFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}