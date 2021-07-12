package com.example.thehinducalender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.thehinducalender.models.WidgetDisplayItem


class WidgetsListAdapter(
    private val widgetsList: ArrayList<WidgetDisplayItem>,
    private val mContext: Context,
    private val listener: IWidgetsListAdapter
) :
    RecyclerView.Adapter<WidgetsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.widgets_list_recycler_view_layout, viewGroup, false)
        )

//        viewHolder.container.setOnClickListener {
//            listener.onWidgetItemClickListener(
//                viewHolder.widgetPreviewImage,
//                viewHolder.scrimLayer,
//                viewHolder.playADBtn,
//                viewHolder.accessStatusTitle,
//                viewHolder.accessStatusDescription,
//                widgetsList[viewHolder.absoluteAdapterPosition]
//            )
//        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        Glide
            .with(mContext)
            .load("https://static.india.com/wp-content/uploads/2020/07/Population-Day.jpg?impolicy=Medium_Resize&w=1200&h=800")
            .placeholder(R.color.g_orange_for_bg)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(viewHolder.widgetPreviewImage)

        viewHolder.accessStatusTitle.text = widgetsList[position].accessStatus
        viewHolder.accessStatusDescription.text = widgetsList[position].accessStatusDescription


        viewHolder.container.setOnClickListener {
            listener.onWidgetItemClickListener(
                viewHolder.widgetPreviewImage,
                viewHolder.scrimLayer,
                viewHolder.playADBtn,
                viewHolder.accessStatusTitle,
                viewHolder.accessStatusDescription,
                widgetsList[position]
            )
        }


    }

    override fun getItemCount() = widgetsList.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val widgetPreviewImage: ImageView = view.findViewById(R.id.widgetPreviewImageWidgetList)
        val scrimLayer: ImageView = view.findViewById(R.id.scrimLayerWidgetList)
        val playADBtn: ImageView = view.findViewById(R.id.playAdButtonWidgetList)

        val accessStatusTitle: TextView =
            view.findViewById(R.id.widgetAccessTypeStatusTitleInWidgetsListItem)
        val accessStatusDescription: TextView =
            view.findViewById(R.id.widgetAccessTypeStatusDescriptionInWidgetsListItem)
        val container: CardView =
            view.findViewById(R.id.widgetContainer)


    }


}


interface IWidgetsListAdapter {
    fun onWidgetItemClickListener(
        previewImage: ImageView,
        scrimImage: ImageView,
        watchADImage: ImageView,
        accessTitle: TextView,
        accessDescription: TextView,
        widgetDataObj: WidgetDisplayItem
    ) {

    }

}