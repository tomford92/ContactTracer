package ltd.tomford.contacttracer.models

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

}