package com.j_gaby_1997.sendme.fragments.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.j_gaby_1997.sendme.data.entity.User
import com.j_gaby_1997.sendme.databinding.ItemSearchBinding

typealias OnItemClickListener = (position: Int) -> Unit

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var onItemClickListenerChat: OnItemClickListener? = null
    private var data: List<User> = emptyList()

    var deleteMode: Boolean = true
    var selectedContacts: MutableList<User> = mutableListOf()

    // - METHODS -
    fun onDeleteModeChange() {
        deleteMode = !deleteMode

        if(deleteMode){
            selectedContacts = mutableListOf()
        }
        notifyDataSetChanged()
    } // Change delete mode, reset the selected contact list and change de UI.
    fun setOnItemClickListenerToChat(onItemClickListener: OnItemClickListener) {
        this.onItemClickListenerChat = onItemClickListener
    }

    // - ADAPTER & VIEW HOLDER -
    fun getItem(position: Int) = data[position]
    fun submitList(newData: List<User>) {
        data = newData
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = data.size
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

        init {
            b.fabNormal.setOnClickListener {
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
                profileImage.load(avatarURL)
                txtContactName.text = name
            }
        }
    }
}