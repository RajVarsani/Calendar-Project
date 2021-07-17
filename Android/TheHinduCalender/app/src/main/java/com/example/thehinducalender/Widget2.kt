package com.example.thehinducalender

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class Widget2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

        val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
        appWidgetAlarm.startAlarm(1, Widget2::class.java, ACTION_AUTO_UPDATE)

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled

        val name = ComponentName(context, Widget2::class.java)
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (appWidgetIds.isEmpty()) {
            // stop alarm
            val appWidgetAlarm = AppWidgetAlarm1(context.applicationContext)
            appWidgetAlarm.stopAlarm(1, Widget2::class.java, ACTION_AUTO_UPDATE)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.e("OR CHK", "$intent")

        val name = context?.let { ComponentName(it, Widget2::class.java) }
        val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(name)

        if (intent!!.action == ACTION_AUTO_UPDATE) {
            onUpdate(
                context!!,
                AppWidgetManager.getInstance(context),
                appWidgetIds
            )
        }
    }

    companion object {
        const val ACTION_AUTO_UPDATE = "AUTO_UPDATE"
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget2)
    Log.e("WI2", "Called widget 2 Update")
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}