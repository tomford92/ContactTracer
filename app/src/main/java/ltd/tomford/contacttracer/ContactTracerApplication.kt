package ltd.tomford.contacttracer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceManager

class ContactTracerApplication: Application() {

    companion object {

    }

    override fun onCreate() {
        super.onCreate()
        initTheme()
    }

    private fun initTheme() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val darkTheme = sharedPreferences.getBoolean("darkTheme", false)
        if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}