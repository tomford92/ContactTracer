package ltd.tomford.contacttracer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import ltd.tomford.contacttracer.models.Contact

@Database(
    entities = [Contact::class],
    version = 3,
    exportSchema = false)
abstract class ContactDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactDAO

    companion object {
        @Volatile
        private var sInstance: ContactDatabase? = null

        // Get a database instance
        fun getDatabaseInstance(context: Context,
                                scope: CoroutineScope
        ): ContactDatabase {
            val tempInstance = sInstance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDatabase::class.java,
                        "contacts_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                sInstance = instance
                return instance
            }
        }
    }
}


