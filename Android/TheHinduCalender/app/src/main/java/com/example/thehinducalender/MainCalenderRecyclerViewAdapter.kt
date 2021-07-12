package com.example.thehinducalender

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.daos.CalenderDao


class MainCalenderRecyclerViewAdapter(
    private val context: Context,
    private val languagePreference: Int
) :
    RecyclerView.Adapter<MainCalenderRecyclerViewAdapter.ViewHolder>() {


    private val calenderDao = CalenderDao()


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.main_calender_recycler_view_layout, viewGroup, false)


        calenderDao.loadJsonDataFromAssets(context, languagePreference)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

//        val row = viewHolder.calenderTable.getChildAt(0) as TableRow
//        val child = row.getChildAt(0) as LinearLayout
//        val rowCount = viewHolder.calenderTable.childCount
//        val childC = row.childCount
//
//        val childChild = child.getChildAt(0) as TextView
//        val childChildC = child.childCount
//
//        Log.e(
//            "Check",
//            " row1 1 : $row \n noOfrows $rowCount \n child : $child \nchild count $childC \n childChild $childchild \nchildCount $childChildC \n text ${childchild.text}"
//        )


        calenderDao.setUpMainCalenderTable(
            viewHolder.calenderTable,
            context,
            languagePreference,
            position + 1
        )


    }

    override fun getItemCount() = 12


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val calenderTable: TableLayout = view.findViewById(R.id.mainCalenderTable)
    }


}