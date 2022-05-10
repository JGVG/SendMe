package com.j_gaby_1997.sendme.fragments.contacts

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.SearchActivity
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentContactsBinding
import com.j_gaby_1997.sendme.fragments.detail.ContactDetailDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class ContactsFrag : Fragment(R.layout.fragment_contacts) {

    private var _b: FragmentContactsBinding? = null
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
                navigateToChatScreen(getItem(it).email)
            }
        }
        setOnItemLongClickListenerToDeleteMode {
            onDeleteModeChange()
            checkDeleteMode()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentContactsBinding.bind(requireView())
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
        viewModel.contacts.observe(viewLifecycleOwner){
            listAdapter.submitList( it )
        }
    }

    // - NAVIGATE -
    private fun navigateToChatScreen( email: String ) {
        Toast.makeText(requireActivity().application, "To chat with: $email", Toast.LENGTH_LONG).show()
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
        viewModel.deleteContacts(selecteds, USEREMAIL)

    } //Gets a list of the contacts selected for deletion.

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance(USEREMAIL: String): ContactsFrag = ContactsFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}