package com.example.thehinducalender

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thehinducalender.models.WidgetDisplayItem
import kotlinx.android.synthetic.main.fragment_widgets_list.*


class WidgetsListFragment : Fragment(), IWidgetsListAdapter {

    private lateinit var mContext: Context
    private val widgetsList = ArrayList<WidgetDisplayItem>(0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = inflater.context

        return inflater.inflate(R.layout.fragment_widgets_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 0..10) {
            widgetsList.add(WidgetDisplayItem(R.color.g_orange, "FREE", "I \nDon't \nKnow"))
        }


        val widgetsListAdapter = WidgetsListAdapter(widgetsList, mContext, this)
        widgetsListRecyclerView.layoutManager = GridLayoutManager(mContext, 2)
        widgetsListRecyclerView.adapter = widgetsListAdapter

    }

    override fun onWidgetItemClickListener(
        previewImage: ImageView,
        scrimImage: ImageView,
        watchADImage: ImageView,
        accessTitle: TextView,
        accessDescription: TextView,
        widgetDataObj: WidgetDisplayItem
    ) {
        val intent = Intent(mContext, ExpandedWidgetViewActivity::class.java)
        intent.putExtra("widgetData", widgetDataObj)

        val pair1: Pair<View, String> = Pair(previewImage, "widgetPreviewImageT")
        val pair2: Pair<View, String> = Pair(scrimImage, "scrimLayerWidgetList")
        val pair3: Pair<View, String> = Pair(watchADImage, "playButtonT")
        val pair4: Pair<View, String> = Pair(accessTitle, "widgetAccessTypeStatusTitleInWidgetsListItemT")
        val pair5: Pair<View, String> = Pair(accessDescription, "widgetAccessTypeStatusDescriptionInWidgetsListItemT")


        val optionCompact: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                pair1,
                pair2,
                pair3,
                pair4,
                pair5,
            )

        requireActivity().startActivity(intent, optionCompact.toBundle())

    }

}