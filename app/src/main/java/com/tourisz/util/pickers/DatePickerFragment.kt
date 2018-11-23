package com.tourisz.util.pickers


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.DatePicker
import com.tourisz.util.Constant


import com.tourisz.util.listener.DateSelectListener
import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Locale

@SuppressLint("ValidFragment")
class DatePickerFragment(internal var dateSelectListener: DateSelectListener?, var fromDate: Long) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(activity!!, this, year, month, day)
        dialog.datePicker.minDate = fromDate
        Log.e("passtime", fromDate.toString())
        // Create a new instance of DatePickerDialog and return it
        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        if (dateSelectListener != null) {
            var date = String.format(Locale.ENGLISH, "%02d", day) + "/" + String.format(Locale.ENGLISH, "%02d", month + 1) + "/" + year.toString()
            fromDate = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(date).time

            Log.e("seltime", fromDate.toString())

            dateSelectListener!!.onDateSelected(date)
        }
    }
}