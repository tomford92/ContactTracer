package ltd.tomford.contacttracer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ltd.tomford.contacttracer.models.Contact
import ltd.tomford.contacttracer.database.ContactDatabase
import ltd.tomford.contacttracer.database.ContactRepo

class ContactViewModel(application: Application): AndroidViewModel(application) {

    private val contactRepo: ContactRepo
    val allContacts: LiveData<List<Contact>>

    init {
        val contactDAO = ContactDatabase.getDatabaseInstance(application, viewModelScope).contactDao()
        contactRepo = ContactRepo(contactDAO)
        allContacts = contactRepo.allContacts
    }

    fun insertContact(contact: Contact) = viewModelScope.launch {
        contactRepo.insertContact(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch {
        contactRepo.updateContact(contact)
    }

    fun getContactById(id: Int): LiveData<Contact> {
        return contactRepo.getContactById(id)
    }

}