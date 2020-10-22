package com.dngwjy.formapp.data.local

import android.content.Context
import android.content.SharedPreferences

class SharedPref(private val context: Context) {
    private val PREF_NAME = "com.dngwjy.formapp"

    private val EMAIL = "USER_EMAIL"
    private val PASSWORD = "USER_PASS"
    private val USERNAME = "USER_NAME"
    private val ROLE = "USER_ROLE"
    private val CLASS = "USER_CLASS"
    private val UID = "UID"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, 0)

    var userRole: String
        get() = prefs.getString(ROLE, "").toString()
        set(value) = prefs.edit().putString(ROLE, value).apply()

    var userEmail: String
        get() = prefs.getString(EMAIL, "").toString()
        set(value) = prefs.edit().putString(EMAIL, value).apply()

    var userPass: String
        get() = prefs.getString(PASSWORD, "").toString()
        set(value) = prefs.edit().putString(PASSWORD, value).apply()

    var userName: String
        get() = prefs.getString(USERNAME, "").toString()
        set(value) = prefs.edit().putString(USERNAME, value).apply()

    var userClass: String
        get() = prefs.getString(CLASS, "").toString()
        set(value) = prefs.edit().putString(CLASS, value).apply()

    var uid: String
        get() = prefs.getString(UID, "").toString()
        set(value) = prefs.edit().putString(UID, value).apply()

}