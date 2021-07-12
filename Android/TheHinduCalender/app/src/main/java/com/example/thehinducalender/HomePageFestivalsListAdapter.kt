package com.example.thehinducalender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.models.HomePageFestivalListModel


class HomePageFestivalsListAdapter(
    private val festivalsList: ArrayList<HomePageFestivalListModel>,
) :
    RecyclerView.Adapter<HomePageFestivalsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.home_page_festivals_list_recycler_view_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.festivalName.text = festivalsList[position].name
        viewHolder.festivalDescription.text = festivalsList[position].description

    }

    override fun getItemCount() = festivalsList.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val festivalName: TextView = view.findViewById<TextView>(R.id.daySpecialityName)
        val festivalDescription: TextView =
            view.findViewById<TextView>(R.id.daySpecialityDescription)
    }


}