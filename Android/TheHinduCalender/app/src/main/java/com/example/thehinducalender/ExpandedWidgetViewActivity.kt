package com.example.thehinducalender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.thehinducalender.models.WidgetDisplayItem
import kotlinx.android.synthetic.main.activity_expanded_widget_view.*

class ExpandedWidgetViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_widget_view)

        window.sharedElementEnterTransition.duration = 300

        setUp()

    }

    private fun setUp() {
        val receivedWidgetData: WidgetDisplayItem =
            intent.extras!!.getSerializable("widgetData") as WidgetDisplayItem

        Glide
            .with(this)
            .load("https://static.india.com/wp-content/uploads/2020/07/Population-Day.jpg?impolicy=Medium_Resize&w=1200&h=800")
            .placeholder(R.color.g_orange_for_bg)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(widgetPreviewImageWidgetList)

        widgetAccessTypeStatusTitleInWidgetsListItem.text = receivedWidgetData.accessStatus
        widgetAccessTypeStatusDescriptionInWidgetsListItem.text =
            receivedWidgetData.accessStatusDescription


    }
}