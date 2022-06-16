package com.j_gaby_1997.sendme.fragments.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.entity.Message
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.databinding.ItemChatBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi", "SimpleDateFormat")
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private var _data: MutableList<Message> = mutableListOf()

    // - ADAPTER & VIEW HOLDER -
    fun getItem(position: Int) = _data[position]
    fun submitList(newData: MutableList<Message>) {
        _data = newData
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = _data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val b = ItemChatBinding.inflate(layoutInflater, parent, false)
        val currentUserEmail = Firebase.auth.currentUser?.email

        return ViewHolder(b, currentUserEmail)
    }
    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val b: ItemChatBinding, currentUserEmail: String?) : RecyclerView.ViewHolder(b.root) {

        private val messageLeft: TextView = b.messageLeft
        private val dateLeft: TextView = b.dateLeft
        private val messageRight: TextView = b.messageRight
        private val dateRight: TextView = b.dateRight
        private val currentUser = currentUserEmail
        private val profilePhoto = b.buttonInfo

        //Configure each item in the list corresponding to the attributes of each contact.
        fun bind(message: Message) {

            messageLeft.visibility = View.INVISIBLE
            dateLeft.visibility = View.INVISIBLE
            messageRight.visibility = View.INVISIBLE
            dateRight.visibility = View.INVISIBLE
            profilePhoto.visibility = View.INVISIBLE

            if (message.email == currentUser) {
                messageRight.text = message.message_text
                dateRight.text = SimpleDateFormat("HH:mm").format(message.date.toDate())

                messageRight.visibility = View.VISIBLE
                dateRight.visibility = View.VISIBLE
            } else {
                messageLeft.text = message.message_text
                dateLeft.text = SimpleDateFormat("HH:mm").format(message.date.toDate())

                FirebaseFirestore.getInstance().collection("USUARIOS").document(message.email).get()
                    .addOnSuccessListener {
                        val url = it.data!!["avatar_url"].toString()

                        Firebase.storage.reference.child("avatars/${message.email}/${url.toUri().lastPathSegment}").downloadUrl.addOnSuccessListener { avatar ->
                            Glide.with(b.buttonInfo)
                                .load(avatar)
                                .placeholder(R.drawable.default_avatar)
                                .error(R.drawable.default_avatar)
                                .into(b.buttonInfo)
                        }
                    }

                messageLeft.visibility = View.VISIBLE
                dateLeft.visibility = View.VISIBLE
                profilePhoto.visibility = View.VISIBLE

            }

        }
    }
}