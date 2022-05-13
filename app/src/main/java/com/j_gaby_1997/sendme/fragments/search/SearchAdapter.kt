package com.j_gaby_1997.sendme.fragments.search

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.databinding.ItemSearchBinding
import com.j_gaby_1997.sendme.fragments.loading.LoadingDlg

typealias OnItemClickListener = (position: Int) -> Unit

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var onItemClickListenerChat: OnItemClickListener? = null
    private var _data: MutableList<User> = mutableListOf()


    // - METHODS -
    fun setOnItemClickListenerToChat(onItemClickListener: OnItemClickListener) {
        this.onItemClickListenerChat = onItemClickListener
    }

    // - ADAPTER & VIEW HOLDER -
    fun getItem(position: Int) = _data[position]
    fun submitList(newData: MutableList<User>) {
        _data = newData
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = _data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val b = ItemSearchBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(b)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val b: ItemSearchBinding) : RecyclerView.ViewHolder(b.root) {

        private val profileImage: ImageView = b.profileImage
        private val txtContactName: TextView = b.txtContactName
        private val txtDescript: TextView = b.txtContactDescript

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListenerChat?.invoke(position)
                }
            }
        }

        //Configure each item in the list corresponding to the attributes of each contact.
        @SuppressLint("NewApi")
        fun bind(user: User){
            user.run{
                //Load image from Firebase
                Firebase.storage.reference.child("avatars/${user.email}/${user.avatarURL.toUri().lastPathSegment}").downloadUrl.addOnSuccessListener {
                    Glide.with(profileImage)
                        .load(it)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(profileImage)
                }
                txtContactName.text = name
                txtDescript.text = description
            }
        }
    }
}