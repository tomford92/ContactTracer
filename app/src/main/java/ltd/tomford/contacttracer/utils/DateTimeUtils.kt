package ltd.tomford.contacttracer.utils

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeUtils(val context: Context) {

    fun fromTimestamp(value: String?): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yy")
        return value.let { LocalDate.parse(value, formatter) }
    }

    fun dateToTimestamp(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("dd-MMM-yy"))
    }

    fun patientDangerCheck(lastContact: LocalDate): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        val incubationPeriodString = sharedPreferences.getString("incubationPeriod", "0")
        val incubationPeriod = incubationPeriodString?.toLong()

        val isPatientInDanger: Boolean
        val safeDate = LocalDate.from(lastContact).plusDays(incubationPeriod!!.toLong())
        isPatientInDanger = safeDate >= LocalDate.now()
        return isPatientInDanger
    }


}