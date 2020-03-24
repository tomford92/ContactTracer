package ltd.tomford.contacttracer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.*
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : com.takisoft.preferencex.PreferenceFragmentCompat(),
        Preference.OnPreferenceChangeListener {
        override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val contactTraceSetting = sharedPreferences.getBoolean("contactTrace", false)
            val incubationPeriodSetting = sharedPreferences.getString("incubationPeriod", "")
            val dateOfInfectionSetting = sharedPreferences.getString("dateOfInfection", "")
            val darkModeSetting = sharedPreferences.getBoolean("darkTheme", false)
            val darkModePreference = findPreference<SwitchPreferenceCompat>("darkTheme")
            when (darkModeSetting) {
                true -> darkModePreference?.isChecked = true
                false -> darkModePreference?.isChecked = false
            }

            darkModePreference?.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)

            when (preference?.key) {
                "darkTheme" -> {
                    if (newValue == true) {
                        setTheme(MODE_NIGHT_YES)
                    } else {
                        setTheme(MODE_NIGHT_NO)
                    }
                }
            }
            return true
        }

        private fun setTheme(mode: Int) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}