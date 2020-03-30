package ltd.tomford.contacttracer.models

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import ltd.tomford.contacttracer.utils.DateTimeUtils

@Entity(tableName = "contacts_table")
class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    var name: String,

    var number: String? = null,

    var email: String? = null,

    var date: String)   {

    private fun hasEmail(): Boolean {
        return !email.isNullOrEmpty()
    }

    private fun hasNumber(): Boolean {
        return !number.isNullOrEmpty()
    }

    private fun isAtRisk(): Boolean {
        return true
    }

    fun isContactAtRisk(context: Context): Boolean {
        val dateTimeUtils = DateTimeUtils(context)
        val dateOfLastContact = dateTimeUtils.fromTimestamp(date)
        return dateTimeUtils.patientDangerCheck(dateOfLastContact)
    }

}