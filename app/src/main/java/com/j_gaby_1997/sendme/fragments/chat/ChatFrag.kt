package com.j_gaby_1997.sendme.fragments.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.EmojiTheming
import com.vanniktech.emoji.google.GoogleEmojiProvider
import com.vanniktech.emoji.recent.NoRecentEmoji
import com.vanniktech.emoji.search.NoSearchEmoji


private const val ARG_USER_EMAIL = "ARG_USER_EMAIL"

@SuppressLint("ResourceAsColor")
@RequiresApi(Build.VERSION_CODES.O)
class ChatFrag: Fragment(R.layout.fragment_chat){
    private var _b: FragmentChatBinding? = null
    private val b get() = _b!!
    private var isKeyboardShowing = false
    private val listAdapter: ChatAdapter = ChatAdapter()
    private lateinit var registration: ListenerRegistration
    private val viewModel: ChatViewModel by viewModels()
    private val USEREMAIL: String by lazy {
        requireArguments().getString(ARG_USER_EMAIL, "null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentChatBinding.bind(requireView())
        EmojiManager.install(GoogleEmojiProvider())
        setupViews()
        setUpRecycledView()
        observeMessages()
    }

    // - SETUPS -
    private fun setupViews() {
        val emojiPopup = EmojiPopup(b.chat, b.edtMessage, theming = EmojiTheming(null, R.color.black), recentEmoji = NoRecentEmoji, searchEmoji = NoSearchEmoji)

        b.buttonBack.setOnClickListener { navigateToContactScreen() }
        b.buttonInfo.setOnClickListener { navigateToProfileScreen(USEREMAIL) }
        b.textFieldMessage.setEndIconOnClickListener {
            send(
                b.edtMessage.text.toString(),
                Firebase.auth.currentUser?.email.toString(),
                USEREMAIL
            )
        }
        b.textFieldMessage.setStartIconOnClickListener {
            emojiPopup.toggle()
            viewModel.changeIcon()

            if (!viewModel.iconFlag) {
                b.textFieldMessage.setStartIconDrawable(R.drawable.baseline_keyboard_black_24dp)
            } else {
                b.textFieldMessage.setStartIconDrawable(R.drawable.baseline_sentiment_satisfied_alt_black_24dp)
            }
        }
        b.edtMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                send(
                    b.edtMessage.text.toString(),
                    Firebase.auth.currentUser?.email.toString(),
                    USEREMAIL
                )
            }
            true
        }
        b.chat.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            b.chat.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = b.chat.rootView.height

            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true
                    onKeyboardVisibilityChanged(true)
                }
            } else {
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                    onKeyboardVisibilityChanged(false)
                }
            }
        }
        FirebaseFirestore.getInstance().collection("USUARIOS").document(USEREMAIL).get().addOnSuccessListener {
                if (it != null) {
                    b.txtName.text = it.data?.get("nombre") as String
                }
            }
    }
    private fun observeMessages() {
            val senderEmail = Firebase.auth.currentUser?.email.toString()
            val receiverEmail = USEREMAIL
            val senderMessagesRef =
                FirebaseFirestore.getInstance().collection("USUARIOS").document(senderEmail)
                    .collection("CONTACTS").document(receiverEmail).collection("MENSAJES")
                    .orderBy("fecha")

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
    private fun setUpRecycledView() {
b.lstChat.run {
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
    private fun navigateToProfileScreen(email: String) {
val appIntent =
    Intent(requireActivity().applicationContext, ProfileActivity::class.java).apply {
        putExtra("email", email)
    }
requireActivity().finish()
startActivity(appIntent)
}

    // - METHODS -
    private fun send(messageText: String, senderEmail: String, receiverEmail: String) {
if (messageText != "") {
    val message = Message(senderEmail, messageText, false, Timestamp.now())
    val senderContactsRef =
        FirebaseFirestore.getInstance().collection("USUARIOS").document(senderEmail)
            .collection("CONTACTS")
    val receiverContactsRef =
        FirebaseFirestore.getInstance().collection("USUARIOS").document(receiverEmail)
            .collection("CONTACTS")

    senderContactsRef.document(receiverEmail).get().addOnSuccessListener { contact ->
        //Create a contacts.
        senderContactsRef.document(receiverEmail)
            .set(hashMapOf("receptor_email" to receiverEmail))
        //Create a message.
        senderContactsRef.document(receiverEmail).collection("MENSAJES").add(
            hashMapOf(
                "mensaje" to message.message_text,
                "estado" to message.state,
                "fecha" to message.date,
                "email" to message.email
            )
        )
        //Set the editText to empty.
        b.edtMessage.text = Editable.Factory.getInstance().newEditable("")
    }
    receiverContactsRef.document(senderEmail).get().addOnSuccessListener { contact ->
        //Create a contacts.
        receiverContactsRef.document(senderEmail)
            .set(hashMapOf("receptor_email" to senderEmail))
        //Create a message.
        receiverContactsRef.document(senderEmail).collection("MENSAJES").add(
            hashMapOf(
                "mensaje" to message.message_text,
                "estado" to message.state,
                "fecha" to message.date,
                "email" to message.email
            )
        )
    }
}
} // Send a message and set the editText to empty.
    private fun onKeyboardVisibilityChanged(opened: Boolean) {
        if(!opened){
            viewModel.iconFlag = true
            b.textFieldMessage.setStartIconDrawable(R.drawable.baseline_sentiment_satisfied_alt_black_24dp)
        }
    } // Change start icon in editText when closing the keyboard.

    override fun onPause() {
super.onPause()
registration.remove()
}

    override fun onDestroyView() {
super.onDestroyView()
_b = null
}

    companion object {
fun newInstance(USEREMAIL: String): ChatFrag =
    ChatFrag().apply { arguments = bundleOf(ARG_USER_EMAIL to USEREMAIL) }
}
}