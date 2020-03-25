package ltd.tomford.contacttracer.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ltd.tomford.contacttracer.R
import ltd.tomford.contacttracer.adapters.ContactListAdapter
import ltd.tomford.contacttracer.models.Contact
import ltd.tomford.contacttracer.viewmodels.ContactViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ContactsFragment : Fragment(), FilterBottomSheet.FilterBottomSheetListener {

    private lateinit var adapter: ContactListAdapter
    lateinit var filterModel: LiveData<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        adapter = ContactListAdapter(context!!, clickListener = { contactId ->
            viewContact(contactId)
        })
        contactsRV.adapter = adapter
        contactsRV.layoutManager = LinearLayoutManager(context)

        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(viewLifecycleOwner, Observer { contacts ->
            contacts?.let { contactsList ->
                val sortedList = contactsList.sortedWith(compareBy {it.date}).asReversed()
                adapter.setContacts(sortedList)
            }
        })

        view.findViewById<FloatingActionButton>(R.id.addContactFab).setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }

    private fun viewContact(contactId: Int) {
        val action =
            ContactsFragmentDirections.actionFirstFragmentToViewContactFragment(
                contactId
            )
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contacts, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                val filterBottomSheet = FilterBottomSheet()
                filterBottomSheet.setTargetFragment(this, 100)
                filterBottomSheet.show(fragmentManager!!, FilterBottomSheet.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFinishFilterBottomSheet(atRisk: Boolean, hasNumber: Boolean, hasEmail: Boolean) {

        val filters = HashMap<String, Boolean>()
        filters["atRisk"] = atRisk
        filters["hasNumber"] = hasNumber
        filters["hasEmail"] = hasEmail
        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        with(contactViewModel) {
            //pass my HashMap to my ViewModel for update/change/set on filters MutableLiveData HashMap variable
            setFilters(filters)
        }

        contactViewModel.filtered.observe(viewLifecycleOwner, Observer { contacts ->
            contacts?.let { contactsList ->
                val sortedList = contactsList.sortedWith(compareBy {it.date}).asReversed()
                adapter.setContacts(sortedList)
            }
        })

    }
}
