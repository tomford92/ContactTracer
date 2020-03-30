package ltd.tomford.contacttracer.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
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

    override fun onResume() {
        super.onResume()
        getFilterPrefsAndSet()
    }

    //this method retrieves the last filters chosen from shared preferences and calls the
    // setFilters(boolean, boolean) method to set them. It is called in onResume() only, as
    // filters are reset if the app is closed/opened again
    private fun getFilterPrefsAndSet() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val filterSet = sharedPreferences.getBoolean("filterSet", false)
        if (!filterSet) {
            return
        } else {
            val hasEmail = sharedPreferences.getBoolean("hasEmail", false)
            val hasNumber = sharedPreferences.getBoolean("hasNumber", false)
            setFilters(hasNumber, hasEmail)
        }
    }

    // takes an (Int) variable contactId and opens the ViewContactFragment, passing in the contactId
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

    //this method takes 2 booleans, one for each filter, and calls the setFilters() method on the ContactViewModel
    // it is called from onFilterPrefsAndSet() - which applies filters when resuming activity, and
    // onFinishFilterBottomSheet - which applies filters when closing the FilterBottomSheet
    private fun setFilters(hasNumber: Boolean, hasEmail: Boolean) {
        val filters = HashMap<String, Boolean>()
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

    override fun onFinishFilterBottomSheet(hasNumber: Boolean, hasEmail: Boolean, filterSet: Boolean) {
        if (filterSet) {
            setFilters(hasNumber, hasEmail)
        } else {
            val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
            contactViewModel.allContacts.observe(viewLifecycleOwner, Observer { contacts ->
                contacts?.let { contactsList ->
                    val sortedList = contactsList.sortedWith(compareBy {it.date}).asReversed()
                    adapter.setContacts(sortedList)
                }
            })
        }
    }
}
