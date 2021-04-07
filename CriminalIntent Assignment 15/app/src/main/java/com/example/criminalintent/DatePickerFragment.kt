package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val KTARG_DATE = "date"

class DatePickerFragment : DialogFragment() {

    interface KTCallbacks {
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val KTdateListener = DatePickerDialog.OnDateSetListener {
            _: DatePicker, year: Int, month: Int, day: Int ->
            val KTresultDate : Date = GregorianCalendar(year, month, day).time
            targetFragment?.let { fragment ->
                (fragment as KTCallbacks).onDateSelected(KTresultDate)
            }
        }

        val KTdate = arguments?.getSerializable(KTARG_DATE) as Date
        val KTcalendar = Calendar.getInstance()
        KTcalendar.time = KTdate
        val KTinitialYear = KTcalendar.get(Calendar.YEAR)
        val KTinitialMonth = KTcalendar.get(Calendar.MONTH)
        val KTinitialDay = KTcalendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
                requireContext(),
                KTdateListener,
                KTinitialYear,
                KTinitialMonth,
                KTinitialDay
        )
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val KTargs = Bundle().apply {
                putSerializable(KTARG_DATE, date)
            }
            return DatePickerFragment().apply {
                arguments = KTargs
            }
        }
    }
}