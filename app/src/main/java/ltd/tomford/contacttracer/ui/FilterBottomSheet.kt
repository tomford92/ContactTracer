package ltd.tomford.contacttracer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.view.*
import ltd.tomford.contacttracer.R


class FilterBottomSheet: BottomSheetDialogFragment() {

    interface FilterBottomSheetListener {
        fun onFinishFilterBottomSheet(hasNumber: Boolean, hasEmail: Boolean, filterSet: Boolean)
    }

    private fun sendData(hasNumber: Boolean, hasEmail: Boolean, filterSet: Boolean) {
        val filterBottomSheetListener: FilterBottomSheetListener = targetFragment as FilterBottomSheetListener
        filterBottomSheetListener.onFinishFilterBottomSheet(hasNumber, hasEmail, filterSet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val hasEmailPref = sharedPreferences.getBoolean("hasEmail", false)
        val hasNumberPref = sharedPreferences.getBoolean("hasNumber", false)

        view.emailCheckBox.isChecked = hasEmailPref
        view.numberCheckBox.isChecked = hasNumberPref

        view.applyFiltersButton.setOnClickListener {
            val hasNumber = view.numberCheckBox.isChecked
            val hasEmail = view.emailCheckBox.isChecked
            val filterSet = true
            sendData(hasNumber, hasEmail, filterSet)
            setPreferences(hasNumber, hasEmail, filterSet)
            dismiss()
        }

        view.clearFiltersButton.setOnClickListener {
            sendData(hasNumber = false, hasEmail = false, filterSet = false)
            setPreferences(hasNumber = false, hasEmail = false, filterSet = false)
            dismiss()
        }
    }

    private fun setPreferences(hasNumber: Boolean, hasEmail: Boolean, filterSet: Boolean) {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().apply {
            putBoolean("hasNumber", hasNumber)
            putBoolean("hasEmail", hasEmail)
            putBoolean("filterSet", filterSet)
        }.apply()
    }

    companion object {
        const val TAG = "FilterBottomSheet"
    }
}