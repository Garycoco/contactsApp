package com.example.contactsApp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsApp.Adapter.Contacts.Contacts
import com.example.contactsApp.databinding.RecyclerViewContactsBinding

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder>() {
    var contacts = mutableListOf<Contacts>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contactName.text = contacts[position].contactName
        holder.binding.conactNumber.text = contacts[position].contactNumber
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
    fun addContact(contact:Contacts){
        if (!contacts.contains(contact)){
            contacts.add(contact)
        }
        else{
            val index = contacts.indexOf(contact)
            if (contact.isDeleted){
                contacts.removeAt(index)
            }
            else{
                contacts[index] = contact
            }

        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RecyclerViewContactsBinding):RecyclerView.ViewHolder(binding.root){

    }
}