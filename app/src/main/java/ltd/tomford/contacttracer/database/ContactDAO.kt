package ltd.tomford.contacttracer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import ltd.tomford.contacttracer.models.Contact

@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Query("DELETE FROM contacts_table WHERE id=:id")
    suspend fun deleteContactById(id: Int)

    @Query("DELETE FROM contacts_table")
    suspend fun deleteAllContacts()

    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts_table WHERE id=:id")
    fun getContactById(id: Int): LiveData<Contact>

}