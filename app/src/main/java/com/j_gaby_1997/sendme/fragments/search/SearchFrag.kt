package com.j_gaby_1997.sendme.fragments.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.j_gaby_1997.sendme.ChatActivity
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentSearchBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class SearchFrag: Fragment(R.layout.fragment_search) {

    private var _b: FragmentSearchBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(com.j_gaby_1997.sendme.fragments.search.ARG_USER_EMAIL, "null")
    }
    private val viewModel: SearchViewModel by viewModels()
    private val listAdapter: SearchAdapter = SearchAdapter().apply {
        setOnItemClickListenerToChat {
            navigateToChatScreen(getItem(it).email)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentSearchBinding.bind(requireView())
        setupViews()
        observeUsers()
        setUpRecycledView()
    }

    // - SETUPS -
    private fun setupViews() {
        b.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { viewModel.search(b.edtSearch.text.toString(), USEREMAIL) }
            override fun afterTextChanged(s: Editable) {}
        })
        b.buttonBack.setOnClickListener { navigateToContactScreen() }
    }
    private fun setUpRecycledView() {
        b.lstContacts.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = listAdapter
        }
    }
    private fun observeUsers() {
        viewModel.searchResult.observe(viewLifecycleOwner){
            listAdapter.submitList( it )

            if(it.size == 0){
                b.imageAdd.visibility = View.VISIBLE
                b.textAdd.visibility = View.VISIBLE
            }else{
                b.imageAdd.visibility = View.GONE
                b.textAdd.visibility = View.GONE
            }
        }
    }

    // - NAVIGATE -
    private fun navigateToChatScreen( email: String ) {
        val appIntent = Intent(requireActivity().applicationContext, ChatActivity::class.java).apply{
            putExtra("email", email)
        }
        requireActivity().finish()
        startActivity(appIntent)
    }
    private fun navigateToContactScreen() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object {
        fun newInstance(USEREMAIL: String): SearchFrag = SearchFrag().apply { arguments = bundleOf(
            ARG_USER_EMAIL to USEREMAIL) }
    }
}