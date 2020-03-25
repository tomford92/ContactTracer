package ltd.tomford.contacttracer.database

import androidx.lifecycle.LiveData
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

    fun searchBy(atRisk: Boolean, hasEmail: Boolean, hasNumber: Boolean) = contactDAO.getFiltered(atRisk, hasEmail, hasNumber)

}