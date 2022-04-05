package com.j_gaby_1997.sendme.fragments.contacts

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.j_gaby_1997.sendme.R
import com.j_gaby_1997.sendme.data.entity.Contact
import com.j_gaby_1997.sendme.databinding.ContactItemBinding
import java.time.format.DateTimeFormatter

typealias OnItemClickListener = (position: Int) -> Unit
typealias OnItemLongClickListener = () -> Unit

class ContactsAdapter() : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private var onItemClickListenerChat: OnItemClickListener? = null
    private var onItemClickListenerDetail: OnItemClickListener? = null
    private var onItemLongClickListenerDelete: OnItemLongClickListener? = null
    private var data: List<Contact> = emptyList()


    var deleteMode: Boolean = true
    var selectedContacts: MutableList<Contact> = mutableListOf()

    fun getItem(position: Int) = data[position]

    fun submitList(newData: List<Contact>) {
        data = newData
        notifyDataSetChanged()
    }

    fun setOnItemClickListenerToChat(onItemClickListener: OnItemClickListener) {
        this.onItemClickListenerChat = onItemClickListener
    }

    fun setOnItemLongClickListenerToDeleteMode(onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListenerDelete = onItemLongClickListener
    }

    fun setOnItemClickListenerToDetail(onItemClickListener: OnItemClickListener) {
        this.onItemClickListenerDetail = onItemClickListener
    }

    override fun getItemCount(): Int = data.size

    //I create the viewHolder according to the layout associated to the list elements.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val b = ContactItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.isLongClickable = true

        holder.checkBox.setOnClickListener{
            if(holder.checkBox.isChecked){
                selectedContacts.add(getItem(position))
            }else{
                selectedContacts.remove(getItem(position))
            }
        }
    }

    fun onDeleteModeChange() {
        deleteMode = !deleteMode

        if(deleteMode){
            selectedContacts = mutableListOf()
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val b: ContactItemBinding) : RecyclerView.ViewHolder(b.root) {

        private val profileImage: ImageView = b.profileImage
        private val statusImage: ImageView = b.statusImage
        private val txtContactName: TextView = b.txtContactName
        private val txtLastMessageChat: TextView = b.txtLastMessageChat
        private val txtLastMessageTime: TextView = b.txtLastMessageTime
        val checkBox: CheckBox = b.checkBox

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListenerChat?.invoke(position)
                }
            }

            itemView.setOnLongClickListener {
                onItemLongClickListenerDelete?.invoke()
                true
            }

            b.profileImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListenerDetail?.invoke(position)
                }
            }
        }

        //Configure each item in the list corresponding to the attributes of each contact.
        @SuppressLint("NewApi")
        fun bind(contact:Contact){
            contact.run{
                checkBox.isChecked = false

                profileImage.load(avatarURL)
                txtContactName.text = name
                txtLastMessageChat.text = lastMessage
                txtLastMessageTime.text =  lastMessageTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString()

                if(deleteMode){
                    checkBox.visibility = View.GONE
                }else{
                    checkBox.visibility = View.VISIBLE
                }

                if(haveNewMessage){
                    txtLastMessageTime.setTextColor(Color.parseColor("#000000"))
                    txtLastMessageChat.setTextColor(Color.parseColor("#000000"))
                    //Style
                    txtLastMessageTime.setTypeface(null, Typeface.BOLD)
                    txtLastMessageChat.setTypeface(null, Typeface.BOLD)
                }else{
                    txtLastMessageTime.setTextColor(Color.parseColor("#b8b0b0"))
                    txtLastMessageChat.setTextColor(Color.parseColor("#b8b0b0"))
                    //Style
                    txtLastMessageChat.setTypeface(null, Typeface.NORMAL)
                    txtLastMessageTime.setTypeface(null, Typeface.NORMAL)
                }

                if(isOnline){
                    statusImage.setImageResource(R.drawable.on_line)
                }else{
                    statusImage.setImageResource(R.drawable.of_line)
                }
            }
        }
    }
}