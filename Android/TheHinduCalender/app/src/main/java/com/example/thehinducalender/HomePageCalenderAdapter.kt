package com.example.thehinducalender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.daos.CalenderDao


class HomePageCalenderAdapter(
    private val applicationContext: Context,
    private val languagePreference: Int,
) :
    RecyclerView.Adapter<HomePageCalenderAdapter.ViewHolder>() {


    private val calenderDao = CalenderDao()


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.home_page_calender_recycler_view_layout, viewGroup, false)

        calenderDao.loadJsonDataFromAssets(applicationContext, languagePreference)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        calenderDao.setHomePageCalenderTable(
            viewHolder.table,
            position,
            applicationContext,
        )


    }

    override fun getItemCount() = 12


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val table: TableLayout = view.findViewById(R.id.homePageCalenderTable)
    }


}