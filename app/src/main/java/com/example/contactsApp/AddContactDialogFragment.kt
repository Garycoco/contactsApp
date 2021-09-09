package com.example.contactsApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contactsApp.Adapter.Contacts.Contacts
import com.example.contactsApp.databinding.FragmentAddContactDialogBinding

class AddContactDialogFragment : DialogFragment() {
    private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : ContactViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null){
                getString(R.string.added_contact)
            }
            else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message,Toast.LENGTH_SHORT).show()
            dismiss()
        })

        binding.buttonSave.setOnClickListener {
            val contactName = binding.contactNameTextInput.text.toString().trim()
            val contactNumber = binding.contactNumberTextInput.text.toString().trim()

            if (contactName.isEmpty()){
                binding.contactNameTextInput.error = "Field required"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()){
                binding.contactNumberTextInput.error = "Field required"
                return@setOnClickListener
            }
            val contact = Contacts()
            contact.contactName = contactName
            contact.contactNumber = contactNumber

            viewModel.addContact(contact)
        }
    }

}