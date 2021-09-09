package com.example.contactsApp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsApp.Adapter.Adapter
import com.example.contactsApp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding:FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel:ContactViewModel

    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.floatingButton.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager,"")
        }

        viewModel.contact.observe(viewLifecycleOwner, Observer {
            adapter.addContact(it)
        })
        viewModel.getContacts()

        val touchHelper = ItemTouchHelper(callBack)
        touchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private var  callBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val currentContact = adapter.contacts[position]

            when(direction){
                ItemTouchHelper.RIGHT ->{
                    UpdateContactDialogFragment(currentContact).show(childFragmentManager,"")
                }
                ItemTouchHelper.LEFT ->{
                    AlertDialog.Builder(requireContext()).also {
                        it.setTitle("Are you sure that you want to delete this contact?")
                        it.setPositiveButton("Yes"){ dialog, which ->
                            viewModel.deleteContact(currentContact)
                            binding.recyclerView.adapter?.notifyItemRemoved(position)
                            Toast.makeText(context, "Contact successfully deleted", Toast.LENGTH_SHORT).show()
                        }
                    }.create().show()
                }
            }
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}