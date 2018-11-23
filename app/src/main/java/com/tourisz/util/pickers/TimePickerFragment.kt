package com.tourisz.util.pickers

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import android.widget.Toast
import com.tourisz.util.Constant
import com.tourisz.util.helper.AndroidHelper


import com.tourisz.util.listener.TimeSelectListener
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ValidFragment")
class TimePickerFragment(internal var timeSelectListener: TimeSelectListener?, var date: Date) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        /* var am_pm = ""

         if (c.get(Calendar.AM_PM) == Calendar.AM)
             am_pm = "AM"
         else if (c.get(Calendar.AM_PM) == Calendar.PM)
             am_pm = "PM"

         val strHrsToShow = if (c.get(Calendar.HOUR) == 0) "12" else c.get(Calendar.HOUR)*/

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user


        if (timeSelectListener != null) {
            var cdate = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).format(Date())
            if (cdate.equals(SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).format(date))) {

                var ctime = SimpleDateFormat(Constant.TIME_FORMAT, Locale.ENGLISH).format(Date())
                var ct = SimpleDateFormat(Constant.TIME_FORMAT, Locale.ENGLISH).parse(ctime)
                var st = SimpleDateFormat(Constant.TIME_FORMAT, Locale.ENGLISH).parse("$hourOfDay:$minute:00")
                if (ct.before(st)) {
                    timeSelectListener!!.onTimeSelected("$hourOfDay:$minute:00")
                } else {
                    AndroidHelper.showToast(activity, "Old time not allowed!")
                }

            } else {
                timeSelectListener!!.onTimeSelected("$hourOfDay:$minute:00")

            }

        }
    }
}
