package com.example.thehinducalender

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AppWidgetAlarm1(private val context: Context?) {
    private val ALARM_ID = 0
    private val INTERVAL_MILLIS: Long = 10

    fun startAlarm() {
        Log.e("ST", "called")
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS.toInt())

//        Log.e("T CHk", "${calendar.timeInMillis} \n ${System.currentTimeMillis()}")

        val alarmIntent = Intent(context, Widget1::class.java).let { intent ->
            intent.action = Widget1.ACTION_AUTO_UPDATE
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
        Log.e("CHK0", "INTENT check, ${context!!.getSystemService(Context.ALARM_SERVICE)}")
        Log.e(
            "T CHk",
            "${(System.currentTimeMillis() / 300000) * 300000 + 300000} \n ${System.currentTimeMillis()}"
        )
        with(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
//            Log.e("CHK", "INTENT check, ${context.getSystemService(Context.ALARM_SERVICE)}")
            setRepeating(
                AlarmManager.RTC_WAKEUP,
                (System.currentTimeMillis() / 300000) * 300000 + 300000,
                60000,
                alarmIntent
            )
            Log.e("CHK2", "INTENT check, ${context.getSystemService(Context.ALARM_SERVICE)}")
        }
    }

    fun stopAlarm() {
        Log.e("DL", "called")

        val alarmIntent = Intent(Widget1.ACTION_AUTO_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}

