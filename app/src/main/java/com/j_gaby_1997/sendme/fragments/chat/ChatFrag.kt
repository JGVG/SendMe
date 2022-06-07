package com.j_gaby_1997.sendme.fragments.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.j_gaby_1997.sendme.ProfileActivity
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.databinding.FragmentChatBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@RequiresApi(Build.VERSION_CODES.O)
class ChatFrag: Fragment(R.layout.fragment_chat){

    private var _b: FragmentChatBinding? = null
    private val b get() = _b!!
    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }
    private val listAdapter: ChatAdapter = ChatAdapter()
    private lateinit var registration: ListenerRegistration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentChatBinding.bind(requireView())

    }
    override fun onStart() {
        super.onStart()
        setupViews()
        setUpRecycledView()
        observeMessages()
    }

    // - SETUPS -
    private fun setupViews() {
        b.buttonBack.setOnClickListener { navigateToContactScreen() }
        b.buttonInfo.setOnClickListener { navigateToProfileScreen(USEREMAIL) }
        b.textFieldMessage.setStartIconOnClickListener {  }
        b.textFieldMessage.setEndIconOnClickListener { send(b.edtMessage.text.toString(),Firebase.auth.currentUser?.email.toString(), USEREMAIL) }
        FirebaseFirestore.getInstance().collection("USUARIOS").document(USEREMAIL).get().addOnSuccessListener {
            if(it != null){
                b.txtName.text = it.data?.get("nombre") as String
            }

        }
    }
    private fun observeMessages(){
        val senderEmail = Firebase.auth.currentUser?.email.toString()
        val receiverEmail = USEREMAIL
        val senderMessagesRef = FirebaseFirestore.getInstance().collection("USUARIOS").document(senderEmail).collection("CONTACTS").document(receiverEmail).collection("MENSAJES").orderBy("fecha")

        registration = senderMessagesRef.addSnapshotListener { messages, _ ->
            val messageList: MutableList<Message> = mutableListOf()

            for (message in messages!!) {
                messageList.add(
                    Message(
                        message.data["email"].toString(),
                        message.data["mensaje"].toString(),
                        message.data["estado"].toString().toBoolean(),
                        message.data["fecha"] as Timestamp
                    )
                )
            }
            listAdapter.submitList(messageList)
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
        requireActivity().finish()
        startActivity(appIntent)
    }

    // - METHODS -
    private fun send(messageText: String, senderEmail:String, receiverEmail: String){
        if(messageText != ""){
            val message = Message(senderEmail, messageText, false, Timestamp.now())
            val senderContactsRef = FirebaseFirestore.getInstance().collection("USUARIOS").document(senderEmail).collection("CONTACTS")
            val receiverContactsRef = FirebaseFirestore.getInstance().collection("USUARIOS").document(receiverEmail).collection("CONTACTS")

            senderContactsRef.document(receiverEmail).get().addOnSuccessListener { contact ->
                //Create a contacts.
                senderContactsRef.document(receiverEmail).set(hashMapOf("receptor_email" to receiverEmail))
                //Create a message.
                senderContactsRef.document(receiverEmail).collection("MENSAJES").add(hashMapOf("mensaje" to message.message_text, "estado" to message.state, "fecha" to message.date, "email" to message.email))
                //Set the editText to empty.
                b.edtMessage.text = Editable.Factory.getInstance().newEditable("")
            }
            receiverContactsRef.document(senderEmail).get().addOnSuccessListener { contact ->
                //Create a contacts.
                receiverContactsRef.document(senderEmail).set(hashMapOf("receptor_email" to senderEmail))
                //Create a message.
                receiverContactsRef.document(senderEmail).collection("MENSAJES").add(hashMapOf("mensaje" to message.message_text, "estado" to message.state, "fecha" to message.date, "email" to message.email))
            }
        }
    } // Send a message and set the editText to empty.

    override fun onPause() {
        super.onPause()
        registration.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    companion object{
        fun newInstance(USEREMAIL: String): ChatFrag = ChatFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
    }
}