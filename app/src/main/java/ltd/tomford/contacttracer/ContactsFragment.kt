package ltd.tomford.contacttracer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ltd.tomford.contacttracer.adapters.ContactListAdapter
import ltd.tomford.contacttracer.viewmodels.ContactViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ContactsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactsRV = view.findViewById<RecyclerView>(R.id.contactsRV)
        val adapter = context?.let {
            ContactListAdapter(it, clickListener = { contactId ->
                viewContact(contactId)
            })
        }
        contactsRV.adapter = adapter
        contactsRV.layoutManager = LinearLayoutManager(context)

        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(viewLifecycleOwner, Observer { contacts ->
            contacts?.let { adapter?.setContacts(it) }
        })

        view.findViewById<FloatingActionButton>(R.id.addContactFab).setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }

    private fun viewContact(contactId: Int) {
        val action = ContactsFragmentDirections.actionFirstFragmentToViewContactFragment(contactId)
        findNavController().navigate(action)
    }
}
