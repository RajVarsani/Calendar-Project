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
 * App Widget Configuration implemented in [Widget5ConfigureActivity]
 */
class Widget5 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget5(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref5(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
        appWidgetAlarm.startAlarm(1, Widget5::class.java, Widget5.ACTION_AUTO_UPDATE_W5)
    }

    override fun onDisabled(context: Context) {
        val name = ComponentName(context, Widget5::class.java)
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (appWidgetIds.isEmpty()) {
            val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
            appWidgetAlarm.stopAlarm(1, Widget5::class.java, ACTION_AUTO_UPDATE_W5)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        Log.e("OR CHK", "$intent")

        val name = context?.let { ComponentName(it, Widget5::class.java) }
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (intent!!.action == ACTION_AUTO_UPDATE_W5) {
            onUpdate(
                context!!,
                AppWidgetManager.getInstance(context),
                appWidgetIds
            )
        }
    }

    companion object {
        const val ACTION_AUTO_UPDATE_W5 = "AUTO_UPDATE"
    }
}

internal fun updateAppWidget5(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.widget5)
    Log.e("WI5", "Called widget 5 Update")

    val widgetsDataHandlerDao = AppWidgetsDataHandlerDao()

    val pref = context.getSharedPreferences("UsersSettingPref", Context.MODE_PRIVATE)
    val langPref = pref.getInt("LanguagePreference", 0)

    widgetsDataHandlerDao.loadJsonDataFromAssets(context, langPref)

    val dayOfWeekText = widgetsDataHandlerDao.getCurrentDayOfWeek(langPref)
    val date = widgetsDataHandlerDao.getTodaysDate(langPref)
    val (tithiDate, zodiacSign) = widgetsDataHandlerDao.currentTithiAndZodiacSignForWidgets(
        langPref
    )
    val currentChoghadiya = widgetsDataHandlerDao.currentChoghadiya(langPref)
    val nextChoghadiya = widgetsDataHandlerDao.nextChoghadiya(langPref)

    views.setTextViewText(R.id.dayTextViewW5, dayOfWeekText)
    views.setTextViewText(R.id.dateTextViewW5, date)
    views.setTextViewText(
        R.id.hinduDateW5P1,
        widgetsDataHandlerDao.currentHinduYearInFormatForWidgets()
    )
    views.setTextViewText(R.id.hinduDateW5P2, tithiDate)
    views.setTextViewText(R.id.zodiacSignW5, zodiacSign)
    views.setTextViewText(R.id.choghadiyaTextViewW5, "$currentChoghadiya -> $nextChoghadiya")


    appWidgetManager.updateAppWidget(appWidgetId, views)
}