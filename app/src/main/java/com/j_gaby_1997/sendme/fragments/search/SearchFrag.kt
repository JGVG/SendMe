package com.j_gaby_1997.sendme.fragments.search

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentSearchBinding

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class SearchFrag: Fragment(R.layout.fragment_search) {

    private var _b: FragmentSearchBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(com.j_gaby_1997.sendme.fragments.search.ARG_USER_EMAIL, "null")
    }
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(LocalRepository(CurrentUserDatabase.getInstance(requireContext()).currentUserDao), this)
    }
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
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var input = b.edtSearch.text.toString()
                viewModel.search(input)
            }
        })
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
        }
    }

    // - NAVIGATE -
    private fun navigateToChatScreen( email: String ) {
        Toast.makeText(requireActivity().application, "To chat with: $email", Toast.LENGTH_LONG).show()
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