package com.example.thehinducalender

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log


class AppWidgetAlarm1(private val context: Context?) {
    //    private val ALARM_ID = 0
//    private val appWidgetsActionArray = arrayOf(
//        Widget1.ACTION_AUTO_UPDATE_W1,
//        Widget2.ACTION_AUTO_UPDATE_W2,
//        Widget3.ACTION_AUTO_UPDATE,
//        Widget4.ACTION_AUTO_UPDATE,
//        Widget5.ACTION_AUTO_UPDATE
//    )
//    private val appWidgetsClassesArray = arrayOf(
//        Widget1::class.java,
//        Widget2::class.java,
//        Widget3::class.java,
//        Widget4::class.java,
//        Widget5::class.java
//    )

    fun startAlarm(
        alarm_id: Int,
        widgetCLass: Class<out AppWidgetProvider>,
        appWidgetAction: String
    ) {
//        Log.e("AWA C", "$appWidgetsActionArray")
//        Log.e("AWCA C", "$appWidgetsClassesArray")

        Log.e("ST", "called")
        val alarmIntent = Intent(context, widgetCLass).let { intent ->
            intent.action = appWidgetAction
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

    fun stopAlarm(
        alarm_id: Int,
        widgetCLass: Class<out AppWidgetProvider>,
        appWidgetAction: String
    ) {
        Log.e("DL", "called")

        val alarmIntent = Intent(context, widgetCLass)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm_id,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)


    }
}

