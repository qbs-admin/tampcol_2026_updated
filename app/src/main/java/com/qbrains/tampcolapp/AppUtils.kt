package com.qbrains.tampcolapp

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {
    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getFormattedDate(date:String):String? {
        val sdf =SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        val output =SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return parsedDate?.let { output.format(it) }
    }

}

