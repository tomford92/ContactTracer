package ltd.tomford.contacttracer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ltd.tomford.contacttracer.R
import ltd.tomford.contacttracer.viewmodels.ContactViewModel

class ViewContactFragment : Fragment() {

    private val args: ViewContactFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_contact, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_editcontact, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                val navController = findNavController()
                val action =
                    ViewContactFragmentDirections.actionViewContactFragmentToAddContactFragment(
                        args.contactId
                    )
                navController.navigate(action)
                return true
            }
            //val action = ContactsFragmentDirections.actionFirstFragmentToViewContactFragment(contactId)
            //        findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactId = args.contactId
        val nameTV = view.findViewById<TextView>(R.id.contactNameTV)
        val numberTV = view.findViewById<TextView>(R.id.numberTV)
        val emailTV = view.findViewById<TextView>(R.id.emailTV)
        val dateTV = view.findViewById<TextView>(R.id.lastContactTV)

        numberTV.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberTV.text.toString()))
            startActivity(intent)
        }

        emailTV.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "plain/text"
            val emailArray = arrayOf(emailTV.text.toString())
            i.putExtra("android.intent.extra.EMAIL", emailArray)
            i.putExtra("android.intent.extra.SUBJECT", "URGENT Contact Tracing - Possible Infection")
            startActivity(Intent.createChooser(i, "Email contact"))
        }

        val contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        contactViewModel.getContactById(contactId).observe(viewLifecycleOwner, Observer { contact ->
            nameTV.text = contact.name
            if (contact.number == null||contact.number == "") {
                numberTV.text = "--"
            } else {
                numberTV.text = contact.number.toString()
            }
            if (contact.email.isNullOrEmpty()) {
                emailTV.text = "--"
            } else {
                emailTV.text = contact.email
            }
            dateTV.text = contact.date
        })

    }
}
