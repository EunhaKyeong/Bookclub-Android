package com.mangpo.bookclub.utils

import com.mangpo.bookclub.ApplicationClass.Companion.prefs

object PrefsUtils {
    fun setTempRecord(record: String) {
        with(prefs.edit()) {
            putString("TEMP_RECORD", record)
            apply()
        }
    }

    fun setUserId(userId: Int) {    //userId 저장
        with(prefs.edit()) {
            putInt("userId", userId)
            apply()
        }
    }

    fun getTempRecord(): String = prefs.getString("TEMP_RECORD", "").toString()

    fun getUserId(): Int = prefs.getInt("userId", 0)    //userId 가져오기

}