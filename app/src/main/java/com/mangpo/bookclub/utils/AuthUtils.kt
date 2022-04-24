package com.mangpo.bookclub.utils

import com.mangpo.bookclub.ApplicationClass.Companion.encryptedPrefs
import com.mangpo.bookclub.ApplicationClass.Companion.prefs

object AuthUtils {

    fun setJWT(token: String) {   //JWT 저장
        with(encryptedPrefs.edit()) {
            putString("TOKEN", token)
            apply()
        }
    }

    fun setEmail(email: String) {   //email 저장
        with(encryptedPrefs.edit()) {
            putString("email", email)
            apply()
        }
    }

    fun setPassword(password: String) {   //password 저장
        with(encryptedPrefs.edit()) {
            putString("password", password)
            apply()
        }
    }

    fun getJWT(): String = encryptedPrefs.getString("TOKEN", "").toString()  //JWT 가져오기

    fun getEmail(): String = encryptedPrefs.getString("email", "").toString()  //email 가져오기

    fun getPassword(): String = encryptedPrefs.getString("password", "").toString()  //password 가져오기

    fun clear() {    //JWT, ID, password, userId 초기화
        with(encryptedPrefs.edit()) {
            clear()
            apply()
        }

        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}