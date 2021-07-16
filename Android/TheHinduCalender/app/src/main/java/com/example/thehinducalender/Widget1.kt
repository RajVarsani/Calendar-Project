package com.example.thehinducalender

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random


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
        appWidgetAlarm.startAlarm()

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
        val appWidgetManager = AppWidgetManager.getInstance(context)
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

        fun updateAppWidget1(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val widgetText = Random.nextInt(0, 100).toString()

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.widget1)
//            views.setTextViewText(R.id.widget_text, widgetText)
            Log.e("W1 up", "$appWidgetManager , $appWidgetId")
            // Instruct the widget manager to update the widget
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_text)
            appWidgetManager.updateAppWidget(appWidgetId, views)

        }
    }

}

internal fun updateAppWidget1(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
//    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget1)
//            views.setTextViewText(R.id.widget_text, widgetText)
    Log.e("W1 up", "$appWidgetManager , $appWidgetId")

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}