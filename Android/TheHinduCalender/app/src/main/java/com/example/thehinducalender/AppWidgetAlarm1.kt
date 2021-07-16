package com.example.thehinducalender

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class AppWidgetAlarm1(private val context: Context?) {
    private val ALARM_ID = 0

    fun startAlarm(alarm_id :Int) {
        Log.e("ST", "called")
        val alarmIntent = Intent(context, Widget1::class.java).let { intent ->
            intent.action = Widget1.ACTION_AUTO_UPDATE
            PendingIntent.getBroadcast(context, alarm_id, intent, 0)
        }
        Log.e(
            "T CHk",
            "${(System.currentTimeMillis())} \n ${(System.currentTimeMillis() / 5400000) * 5400000 + 1800000}"
        )
        with(context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager) {
//            setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                (System.currentTimeMillis() / 5400000) * 5400000 + 1800000,
//                5400000,
//                alarmIntent
//            )
            setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                60000,
                alarmIntent
            )
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

