package com.dngwjy.formapp.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T> T.logE(msg:String?){
    Log.e(T::class.simpleName,msg)
}

inline fun <reified T> T.logD(msg: String?){
    Log.d(T::class.simpleName,msg)
}

fun Context.toast(msg:String?)=msg.let {
    Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
}

inline fun<reified T> T.convertDate(milis:Long,dateFormat:String):String{
    val formatter = SimpleDateFormat(dateFormat)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milis
    return formatter.format(calendar.time)
}