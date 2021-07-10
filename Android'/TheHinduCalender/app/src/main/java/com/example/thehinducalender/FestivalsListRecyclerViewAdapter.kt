package com.example.thehinducalender

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.daos.MarginsDao
import com.example.thehinducalender.daos.ViewsSetterDao
import com.example.thehinducalender.models.Festival
import com.example.thehinducalender.models.SquareCardView


class FestivalsListRecyclerViewAdapter(
    private val festivalsList: ArrayList<Festival>,
    private val context: Context,
    private val month: Int,
    private val year: Int,
    private val languagePreference: Int,
    var currentChoiceForDataDisplay: Int,
    private val listener: IFestivalsListRecyclerViewAdapter
) :
    RecyclerView.Adapter<FestivalsListRecyclerViewAdapter.ViewHolder>() {


    private val viewsSetterDao = ViewsSetterDao()
    private val marginsDao = MarginsDao()


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.festival_list_recycler_view_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = festivalsList.size


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.containerWI.visibility = View.GONE
        viewHolder.containerWOI.visibility = View.GONE
        viewHolder.mainContainer.visibility = View.GONE

        Log.e("MainCalled", "for position $position")


        if (festivalsList[position].isShort) {

            if (currentChoiceForDataDisplay != 1) {

                Log.e("A Called", "for position $position")

                viewHolder.mainContainer.visibility = View.VISIBLE
                viewHolder.containerWOI.visibility = View.VISIBLE
                marginsDao.setMarginTop(viewHolder.containerWOI, 20F)



                viewHolder.titleWOI.text = festivalsList[position].name
                viewsSetterDao.setDayByMonthAndDate(
                    viewHolder.dateWOI,
                    month,
                    festivalsList[position].date.toInt(),
                    year,
                    languagePreference
                )


                viewsSetterDao.setRandomColorToTheseFestivalsListObj(
                    viewHolder.titleWOI,
                    viewHolder.dateWOI,
                    viewHolder.subContainerWOI,
                    context
                )
            }

        } else {

            if (currentChoiceForDataDisplay != 2) {

                val imageHierarchyList = arrayListOf<Int>(0)

                Log.e("B Called", "for position $position")

                viewHolder.mainContainer.visibility = View.VISIBLE
                viewHolder.containerWI.visibility = View.VISIBLE
                marginsDao.setMarginTop(viewHolder.containerWI, 20F)




                viewsSetterDao.setDayByMonthAndDate(
                    viewHolder.dateWI,
                    month,
                    festivalsList[position].date.toInt(),
                    year,
                    languagePreference
                )
                viewsSetterDao.setImage(
                    context,
                    festivalsList[position].imagesLink,
                    viewHolder.imageView,
                    imageHierarchyList
                )

                viewHolder.titleWI.text = festivalsList[position].name
                viewHolder.description.text = festivalsList[position].description


                viewHolder.containerWI.setOnClickListener {
                    listener.festivalsListOnItemClickedListener(
                        festivalsList[position],
                        viewHolder.scrimTop,
                        viewHolder.scrimTop,
                        viewHolder.imageView,
                        imageHierarchyList,
                        viewHolder.containerWI,
                    )
                }


            }

        }


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleWI: TextView = view.findViewById(R.id.festivalOrHolidayName)
        val description: TextView = view.findViewById(R.id.festivalOrHolidayShortDescription)
        val dateWI: TextView = view.findViewById(R.id.festivalOrHolidayDateForLayoutWithImage)
        val imageView: ImageView = view.findViewById(R.id.festivalOrHolidayImageImageView)
        val containerWI: SquareCardView = view.findViewById(R.id.containerForFestivalWithImage)


        val titleWOI: TextView = view.findViewById(R.id.titleForHoliday)
        val dateWOI: TextView = view.findViewById(R.id.festivalOrHolidayDateForLayoutWithoutImage)
        val containerWOI: CardView =
            view.findViewById(R.id.containerForHolidayWithoutImage)
        val subContainerWOI: ConstraintLayout =
            view.findViewById(R.id.subContainerForBGWithoutImage)

        val mainContainer: ConstraintLayout = view.findViewById(R.id.mainContainerForFestivalsList)


        val scrimTop: ImageView = view.findViewById(R.id.topScrimForFestivalsListImage)
        val scrimBottom: ImageView = view.findViewById(R.id.bottomScrimForFestivalsListImage)

    }


}

interface IFestivalsListRecyclerViewAdapter {

    fun festivalsListOnItemClickedListener(
        festival: Festival,
        scrimTop: ImageView,
        scrimBottom: ImageView,
        imageView: ImageView,
        imageHierarchyList: ArrayList<Int>,
        containerWI: SquareCardView
    ) {

    }
}