package com.j_gaby_1997.sendme.fragments.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.CurrentUserDatabase
import com.j_gaby_1997.sendme.data.repository.LocalRepository
import com.j_gaby_1997.sendme.databinding.FragmentChatBinding

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class ChatFrag: Fragment(R.layout.fragment_chat){

    private var _b: FragmentChatBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }
    private val viewModel: ChatViewModel by viewModels()
    private val listAdapter: ChatAdapter = ChatAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentChatBinding.bind(requireView())
        setupViews()
        observeMessages()
        setUpRecycledView()
    }

    // - SETUPS -
    private fun setupViews() {
        //Firebase setup
        viewModel.instance(Firebase.auth.currentUser?.email.toString(), USEREMAIL)
        viewModel.update(Firebase.auth.currentUser?.email.toString(), USEREMAIL)

        b.buttonBack.setOnClickListener { navigateToContactScreen() }
        b.buttonInfo.setOnClickListener { navigateToProfileScreen(USEREMAIL) }
        b.textFieldMessage.setEndIconOnClickListener { send(b.edtMessage.text.toString(),Firebase.auth.currentUser?.email.toString(), USEREMAIL) }
        FirebaseFirestore.getInstance().collection("USUARIOS").document(USEREMAIL).get().addOnSuccessListener {
            if(it != null){
                b.txtName.text = it.data?.get("nombre") as String
            }

        }
    }
    private fun observeMessages(){
        viewModel.chat.observe(viewLifecycleOwner){
            listAdapter.submitList(it)
        }
    }
    private fun setUpRecycledView(){
        b.lstChat.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = false
            }
            itemAnimator = DefaultItemAnimator()
            adapter = listAdapter
        }
    }

    // - NAVIGATE -
    private fun navigateToContactScreen() {
        requireActivity().onBackPressed()
    }
    private fun navigateToProfileScreen( email :String ){
        val appIntent = Intent(requireActivity().applicationContext, ProfileActivity::class.java).apply{
            putExtra("email", email)
        }
        startActivity(appIntent)
    }

    // - METHODS -
    private fun send(messageText: String, senderEmail:String, receiverEmail: String){
        viewModel.sendMessage(messageText,senderEmail, receiverEmail)
        b.edtMessage.text = Editable.Factory.getInstance().newEditable("")
    }

    companion object{
        fun newInstance(USEREMAIL: String): ChatFrag = ChatFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}