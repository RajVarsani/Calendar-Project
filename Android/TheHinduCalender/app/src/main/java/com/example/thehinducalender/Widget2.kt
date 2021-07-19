package com.example.thehinducalender

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.thehinducalender.appwidgetdaos.AppWidgetsDataHandlerDao

/**
 * Implementation of App Widget functionality.
 */
class Widget2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
        appWidgetAlarm.startAlarm(1, Widget2::class.java, ACTION_AUTO_UPDATE_W2)

    }

    override fun onDisabled(context: Context) {

        val name = ComponentName(context, Widget2::class.java)
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (appWidgetIds.isEmpty()) {
            val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
            appWidgetAlarm.stopAlarm(1, Widget2::class.java, ACTION_AUTO_UPDATE_W2)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        Log.e("OR CHK", "$intent")

        val name = context?.let { ComponentName(it, Widget2::class.java) }
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (intent!!.action == ACTION_AUTO_UPDATE_W2) {
            onUpdate(
                context!!,
                AppWidgetManager.getInstance(context),
                appWidgetIds
            )
        }
    }

    companion object {
        const val ACTION_AUTO_UPDATE_W2 = "AUTO_UPDATE"
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.widget2)
    Log.e("WI2", "Called widget 2 Update")

    val widgetsDataHandlerDao = AppWidgetsDataHandlerDao()

    val pref = context.getSharedPreferences("UsersSettingPref", Context.MODE_PRIVATE)
    val langPref = pref.getInt("LanguagePreference", 0)

    widgetsDataHandlerDao.loadJsonDataFromAssets(context, langPref)

    val dayOfWeekText = widgetsDataHandlerDao.getCurrentDayOfWeek(langPref)
    val date = widgetsDataHandlerDao.getTodaysDate(langPref)
    val (tithiDate, zodiacSign) = widgetsDataHandlerDao.currentTithiAndZodiacSignForWidgets(
        langPref
    )
    val hinduDate =
        widgetsDataHandlerDao.currentHinduYearInFormatForWidgets() + " / " + tithiDate
    val currentChoghadiya = widgetsDataHandlerDao.currentChoghadiya(langPref)

    views.setTextViewText(R.id.dayTextViewW2, dayOfWeekText)
    views.setTextViewText(R.id.dateTextViewW2, date)
    views.setTextViewText(R.id.hinduDateW2, hinduDate)
    views.setTextViewText(R.id.zodiacSignW2, zodiacSign)
    views.setTextViewText(R.id.choghadiyaTextViewW2, "Choghadiya : $currentChoghadiya")


    appWidgetManager.updateAppWidget(appWidgetId, views)
}