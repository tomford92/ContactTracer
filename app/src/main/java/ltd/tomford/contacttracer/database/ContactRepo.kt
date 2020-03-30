package ltd.tomford.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import ltd.tomford.contacttracer.models.Contact

class ContactRepo(private val contactDAO: ContactDAO) {

    val allContacts: LiveData<List<Contact>> = contactDAO.getAllContacts()

    suspend fun insertContact(contact: Contact) {
        contactDAO.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDAO.updateContact(contact)
    }

    fun getContactById(id: Int): LiveData<Contact> {
        return contactDAO.getContactById(id)
    }

    fun searchBy(number: String?) = contactDAO.getFiltered(number)

    fun getRawFiltered(query: SimpleSQLiteQuery) = contactDAO.getRawFiltered(query)

}