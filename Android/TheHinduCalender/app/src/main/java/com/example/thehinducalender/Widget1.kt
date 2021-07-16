package com.example.thehinducalender

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.thehinducalender.appwidgetdaos.AppWidgetsDataHandlerDao


/**
 * Implementation of App Widget functionality.
 */
class Widget1 : AppWidgetProvider() {


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget1(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.e("OE", "called")

        val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
        appWidgetAlarm.startAlarm(0)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        // Do something
        Log.e("OR CHK", "$intent")

        /*if (intent!!.action == ACTION_AUTO_UPDATE) {
            // DO SOMETHING
        }*/

        val name = context?.let { ComponentName(it, Widget1::class.java) }
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (intent!!.action == ACTION_AUTO_UPDATE) {
            onUpdate(
                context!!,
                AppWidgetManager.getInstance(context),
                appWidgetIds
            )
        }
    }

    override fun onDisabled(context: Context) {
        Log.e("OD", "called")

        // Enter relevant functionality for when the last widget is disabled
//        val appWidgetManager = AppWidgetManager.getInstance(context)
        val name = ComponentName(context, Widget1::class.java)
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (appWidgetIds.isEmpty()) {
            // stop alarm
            val appWidgetAlarm = AppWidgetAlarm1(context.getApplicationContext())
            appWidgetAlarm.stopAlarm()
        }

    }

    companion object {
        const val ACTION_AUTO_UPDATE = "AUTO_UPDATE"
    }
}

internal fun updateAppWidget1(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {


    val views = RemoteViews(context.packageName, R.layout.widget1)
    Log.e("W1 up", "$appWidgetManager , $appWidgetId")

    val widgetsDataHandlerDao = AppWidgetsDataHandlerDao();


    val pref = context.getSharedPreferences("UsersSettingPref", MODE_PRIVATE)
    val langPref = pref.getInt("LanguagePreference", 0)
    Log.e("Lang Pref", " $langPref")


    widgetsDataHandlerDao.loadJsonDataFromAssets(context, langPref)


    val dayOfWeekText = widgetsDataHandlerDao.getCurrentDayOfWeek(langPref)
    Log.e("DOW", " $dayOfWeekText ")
    views.setTextViewText(R.id.dayTextViewW1, dayOfWeekText)


    val date = widgetsDataHandlerDao.getTodaysDate(langPref)
    views.setTextViewText(R.id.dateTextViewW1, date)


    val (tithiDate, zodiacSign) = widgetsDataHandlerDao.currentTithiAndZodiacSignForWidgets(
        langPref
    )
    val hinduDate =
        widgetsDataHandlerDao.currentHinduYearInFormatForWidgets() + " / " + tithiDate
    Log.e("Dt chk", " $hinduDate")
    Log.e("Dt chk", " $zodiacSign")
    views.setTextViewText(R.id.hinduDateW1, hinduDate)
    views.setTextViewText(R.id.zodiacSignW1, zodiacSign)


    val currentChoghadiya = widgetsDataHandlerDao.currentChoghadiya(langPref)
    Log.e("Cho Chk", " $currentChoghadiya ")
    views.setTextViewText(R.id.choghadiyaTextViewW1, "Choghadiya : $currentChoghadiya")

//    val currentChoghdiya =

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}