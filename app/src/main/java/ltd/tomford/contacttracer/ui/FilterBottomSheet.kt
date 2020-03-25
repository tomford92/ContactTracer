package ltd.tomford.contacttracer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter.view.*
import ltd.tomford.contacttracer.R


class FilterBottomSheet: BottomSheetDialogFragment() {

    interface FilterBottomSheetListener {
        fun onFinishFilterBottomSheet(atRisk: Boolean, hasNumber: Boolean, hasEmail: Boolean)
    }

    private fun sendData(atRisk: Boolean, hasNumber: Boolean, hasEmail: Boolean) {
        val filterBottomSheetListener: FilterBottomSheetListener = targetFragment as FilterBottomSheetListener
        filterBottomSheetListener.onFinishFilterBottomSheet(atRisk, hasNumber, hasEmail)
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

        view.applyFiltersButton.setOnClickListener {
            val atRisk = view.atRiskCheckBox.isChecked
            val hasNumber = view.numberCheckBox.isChecked
            val hasEmail = view.emailCheckBox.isChecked
            sendData(atRisk, hasNumber, hasEmail)
            dismiss()
        }
    }

    companion object {
        const val TAG = "FilterBottomSheet"
    }
}