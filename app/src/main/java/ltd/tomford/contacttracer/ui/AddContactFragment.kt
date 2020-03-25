package ltd.tomford.contacttracer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import kotlinx.android.synthetic.main.fragment_add_contact.*
import kotlinx.android.synthetic.main.fragment_add_contact.view.*
import ltd.tomford.contacttracer.R
import ltd.tomford.contacttracer.models.Contact
import ltd.tomford.contacttracer.utils.DateTimeUtils
import ltd.tomford.contacttracer.viewmodels.ContactViewModel
import java.time.ZoneId
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddContactFragment : Fragment() {

    private val args: AddContactFragmentArgs by navArgs()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactId = args.contactId
        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        if (contactId != -1) {
            contactViewModel.getContactById(contactId).observe(viewLifecycleOwner, androidx.lifecycle.Observer { contact ->
                view.contactNameET.setText(contact.name)
                view.contactNumberET.setText(contact.number)
                view.contactEmailET.setText(contact.email)
                view.contactDateET.setText(contact.date)
            })
        }

        view.contactDateET.setOnClickListener {
            MaterialDialog(context!!).show {
                datePicker(maxDate = Calendar.getInstance()) { _, date ->
                    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    val localDateString = DateTimeUtils(context).dateToTimestamp(localDate)
                    view.contactDateET.setText(localDateString)
                }
            }
        }

        view.saveContactButton.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {

        val name = contactNameET.text.toString()
        val numberString = contactNumberET.text.toString()
        val email = contactEmailET.text.toString()
        val date = contactDateET.text.toString()

        if (name.isEmpty()) {
            contactNameTIL.error = "You must enter a name"
            return
        } else {
            contactNameTIL.error = null
        }

        if (date.isEmpty()) {
            contactDateTIL.error = "You must set a date"
            return
        } else {
            contactDateTIL.error = null
        }

        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        if (args.contactId == -1) {
            val contact = Contact(
                null,
                name,
                numberString,
                email,
                date
            )
            contactViewModel.insertContact(contact)
        } else {
            val contact = Contact(
                args.contactId,
                name,
                numberString,
                email,
                date
            )
            contactViewModel.updateContact(contact)
        }

        val navController = findNavController()
        navController.navigateUp()

    }
}
