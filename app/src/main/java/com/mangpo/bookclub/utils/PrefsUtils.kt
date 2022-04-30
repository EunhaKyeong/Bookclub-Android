package com.mangpo.bookclub.utils

import com.mangpo.bookclub.ApplicationClass.Companion.prefs

object PrefsUtils {
    fun setTempRecord(record: String) {
        with(prefs.edit()) {
            putString("TEMP_RECORD", record)
            apply()
        }
    }

    fun getTempRecord(): String = prefs.getString("TEMP_RECORD", "").toString()
}