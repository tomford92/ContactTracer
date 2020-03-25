package ltd.tomford.contacttracer.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import ltd.tomford.contacttracer.R
import ltd.tomford.contacttracer.models.Contact
import ltd.tomford.contacttracer.utils.DateTimeUtils
import java.util.*
import kotlin.collections.ArrayList


class ContactListAdapter internal constructor(
    private var context: Context,
    val clickListener: (contactId: Int) -> Unit
) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var contacts = emptyList<Contact>() // Cached copy of contacts
    private lateinit var contactsFiltered: List<Contact>
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val contactTracingStatus = sharedPreferences.getBoolean("contactTrace", false)

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactNameTV: TextView = itemView.findViewById(R.id.nameTV)
        val contactDateTV: TextView = itemView.findViewById(R.id.dateTV)
        val dangerImageView: ImageView = itemView.findViewById(R.id.dangerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = inflater.inflate(R.layout.contacts_rv_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = contacts[position]
        val contactId = current.id
        holder.contactNameTV.text = current.name
        holder.contactDateTV.text = current.date
        if (contactTracingStatus) {
            val dateTimeUtils = DateTimeUtils(context)
            val dateOfLastContact = dateTimeUtils.fromTimestamp(current.date)
            val isPatientInDanger = dateTimeUtils.patientDangerCheck(dateOfLastContact)
            if (isPatientInDanger) {
                holder.dangerImageView.visibility = View.VISIBLE
            } else {
                holder.dangerImageView.visibility = View.INVISIBLE
            }
        } else {
            holder.dangerImageView.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener { _ ->
            if (contactId != null) {
                clickListener.invoke(contactId)
            }
        }

    }

    internal fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount() = contacts.size

    class ContactClickListener(val clickListener: (contactId: Int) -> Unit) {
        fun onClick(contact: Contact) = contact.id?.let { clickListener(it) }
    }

    override fun getFilter(): Filter? {

        return object : Filter() {

            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    contactsFiltered = contacts
                } else {
                    val filteredList: MutableList<Contact> = ArrayList()
                    for (row in contacts) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name match

                        if (!row.email.isNullOrEmpty()) {
                            filteredList.add(row)
                        }

                        if (row.name.toLowerCase(Locale.getDefault())
                                .contains(charString.toLowerCase(Locale.getDefault()))
                        ) {
                            filteredList.add(row)
                        }
                    }
                    contactsFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactsFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                contactsFiltered = filterResults.values as List<Contact>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }
}