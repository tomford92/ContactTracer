package ltd.tomford.contacttracer.viewmodels

import android.app.Application
import android.graphics.ColorSpace.Model
import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.launch
import ltd.tomford.contacttracer.database.ContactDatabase
import ltd.tomford.contacttracer.database.ContactRepo
import ltd.tomford.contacttracer.models.Contact


class ContactViewModel(application: Application): AndroidViewModel(application) {

    private val contactRepo: ContactRepo
    val allContacts: LiveData<List<Contact>>
    //initialise filterLiveData HashMap vairable
    var filters = MutableLiveData<HashMap<String, Boolean>>().apply { value = FILTERS }

    init {
        val contactDAO = ContactDatabase.getDatabaseInstance(application, viewModelScope).contactDao()
        contactRepo = ContactRepo(contactDAO)
        allContacts = contactRepo.allContacts
    }

    //function to update/change/set filters MutableLiveData HashMap variable
    //see setFilters(filters) used in Fragment
    fun setFilters(_filters: HashMap<String, Boolean>) {
        filters.value = _filters
    }


    //foreach on passed HashMap via switchMap{}
    val filtered: LiveData<List<Contact>> = Transformations.switchMap(filters) {

        //variables
        var atRisk = false
        var hasNumber = false
        var hasEmail = false

        //forEach loop on Hash
        for (filter in it) {
            when (filter.key) {
                "atRisk" -> {
                    atRisk = filter.value
                }
                "hasNumber" -> {
                    hasNumber = filter.value
                }
                "hasEmail" -> {
                    hasEmail = filter.value
                }
            }
        }

        val queryString = generateFilterQuery(hasNumber, hasEmail)
        val simpleSQLiteQuery = SimpleSQLiteQuery(queryString)
        contactRepo.getRawFiltered(simpleSQLiteQuery)
        //pass strings to DAO
//        contactRepo.searchBy(number)
    }

    fun generateFilterQuery(hasNumber: Boolean, hasEmail: Boolean): String {

        var baseQuery = "SELECT * FROM contacts_table WHERE"

        baseQuery += if (hasNumber) {
            " number IS NOT NULL"
        } else {
            " number IS NULL"
        }

        baseQuery += " AND"

        baseQuery += if (hasEmail) {
            " email IS NOT NULL"
        } else {
            " email IS NULL"
        }

        return baseQuery
    }

    companion object {
        var FILTERS: HashMap<String, Boolean>? = null
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