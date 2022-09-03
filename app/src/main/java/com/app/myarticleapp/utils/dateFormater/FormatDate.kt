package com.app.myarticleapp.utils.dateFormater

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object FormatDate {
    fun getFormattedFullDateString(dateStr: String): String? {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"/*'Z'*/, Locale.ENGLISH)
            val formattedDate = formatter.parse(dateStr)
            return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(formattedDate ?: Date())
        } catch (e: Exception){
            ""
        }
    }

    fun getGreetingMessage():String{
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..23 -> "Good Evening"
            else -> "Hello"
        }
    }
}