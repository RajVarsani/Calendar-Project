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
 * App Widget Configuration implemented in [Widget4ConfigureActivity]
 */
class Widget4 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget4(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref4(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
        appWidgetAlarm.startAlarm(1, Widget4::class.java, ACTION_AUTO_UPDATE_W4)
    }

    override fun onDisabled(context: Context) {
        val name = ComponentName(context, Widget4::class.java)
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (appWidgetIds.isEmpty()) {
            val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
            appWidgetAlarm.stopAlarm(1, Widget4::class.java, ACTION_AUTO_UPDATE_W4)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        Log.e("OR CHK", "$intent")

        val name = context?.let { ComponentName(it, Widget4::class.java) }
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (intent!!.action == Widget4.ACTION_AUTO_UPDATE_W4) {
            onUpdate(
                context!!,
                AppWidgetManager.getInstance(context),
                appWidgetIds
            )
        }
    }


    companion object {
        const val ACTION_AUTO_UPDATE_W4 = "AUTO_UPDATE"
    }
}

internal fun updateAppWidget4(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.widget4)
    Log.e("WI4", "Called widget 4 Update")

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

    views.setTextViewText(R.id.dayTextViewW4, dayOfWeekText)
    views.setTextViewText(R.id.dateTextViewW4, date)
    views.setTextViewText(
        R.id.hinduDateW4P1,
        widgetsDataHandlerDao.currentHinduYearInFormatForWidgets()
    )
    views.setTextViewText(R.id.hinduDateW4P2, tithiDate)
    views.setTextViewText(R.id.zodiacSignW4, zodiacSign)
    views.setTextViewText(R.id.choghadiyaTextViewW4, "$currentChoghadiya -> $nextChoghadiya")


    appWidgetManager.updateAppWidget(appWidgetId, views)
}