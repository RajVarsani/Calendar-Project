package com.example.thehinducalender

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thehinducalender.daos.ViewsSetterDao
import com.example.thehinducalender.models.Festival
import com.example.thehinducalender.models.SquareCardView
import kotlinx.android.synthetic.main.fragment_festivals_and_holidays_list.*

class FestivalsAndHolidaysListFragment : Fragment(), View.OnClickListener,
    IFestivalsListRecyclerViewAdapter {

    private lateinit var viewsSetterDao: ViewsSetterDao
    private val languagePreference: Int = 0
    private var currentMonthIndex: Int = 0
    private var currentChoiceForDataDisplay: Int = 0

    private lateinit var mContext: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mContext = inflater.context
        return inflater.inflate(R.layout.fragment_festivals_and_holidays_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewsSetterDao = ViewsSetterDao()
        viewsSetterDao.loadJsonDataFromAssets(mContext, languagePreference)

        setUp()
    }

    private fun setUp() {

        festivalsListRecyclerView.layoutManager = LinearLayoutManager(mContext)
        currentMonthIndex = viewsSetterDao.currentMonth() - 1
        viewsSetterDao.changeFestivalsListPageCalenderDisplayMonthTo(
            currentMonthIndex,
            monthNameInFestivalsList,
            languagePreference,
        )

        viewsSetterDao.setUpFestivalsListRecyclerView(
            festivalsListRecyclerView,
            languagePreference,
            mContext,
            currentChoiceForDataDisplay,
            this
        )

        setUpOnClickListeners()

    }

    private fun setUpOnClickListeners() {
        prevBtnForFestivalsList.setOnClickListener(this)
        nextBtnForFestivalsList.setOnClickListener(this)
        swapFestivalsListChoiceContainer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.nextBtnForFestivalsList -> {
                    changeFestivalsListToNextMonth()
                }
                R.id.prevBtnForFestivalsList -> {
                    changeFestivalsListToPrevMonth()
                }
                R.id.swapFestivalsListChoiceContainer -> {
                    changeDataDisplayingChoice()
                }
            }

        }


    }

    private fun changeDataDisplayingChoice() {

        when (currentChoiceForDataDisplay) {
            0 -> {
                currentChoiceForDataDisplay = 1
                setUpListAccordingToUpdatedChoice()
            }
            1 -> {
                currentChoiceForDataDisplay = 2
                setUpListAccordingToUpdatedChoice()
            }
            2 -> {
                currentChoiceForDataDisplay = 0
                setUpListAccordingToUpdatedChoice()
            }
        }

    }

    private fun setUpListAccordingToUpdatedChoice() {

        viewsSetterDao.changeFestivalsListPageDataDisplayChoiceTextView(
            currentChoiceForDataDisplay,
            currentChoiceForFestivalsListDisplayingTextView,
            languagePreference
        )

        viewsSetterDao.updateFestivalListRecyclerViewChoice(
            festivalsListRecyclerView,
            currentChoiceForDataDisplay,
            currentMonthIndex + 1,
            viewsSetterDao.currentYear(),
            languagePreference,
            mContext,
            this
        )

//        viewsSetterDao.updateFestivalListRecyclerView(
//            festivalsListRecyclerView,
//            currentChoiceForDataDisplay
//        )

    }

    private fun changeFestivalsListToPrevMonth() {
        if (currentMonthIndex > 0) {
            currentMonthIndex--
            viewsSetterDao.setFestivalsListRecyclerView(
                festivalsListRecyclerView,
                languagePreference,
                currentMonthIndex + 1,
                viewsSetterDao.currentYear(),
                mContext,
                currentChoiceForDataDisplay,
                this
            )
            viewsSetterDao.changeFestivalsListPageCalenderDisplayMonthTo(
                currentMonthIndex,
                monthNameInFestivalsList,
                languagePreference,
            )


        }
    }


    private fun changeFestivalsListToNextMonth() {
        if (currentMonthIndex < 11) {
            currentMonthIndex++
            viewsSetterDao.setFestivalsListRecyclerView(
                festivalsListRecyclerView,
                languagePreference,
                currentMonthIndex + 1,
                viewsSetterDao.currentYear(),
                mContext,
                currentChoiceForDataDisplay,
                this
            )

            viewsSetterDao.changeFestivalsListPageCalenderDisplayMonthTo(
                currentMonthIndex,
                monthNameInFestivalsList,
                languagePreference
            )

        }
    }

    override fun festivalsListOnItemClickedListener(
        festival: Festival,
        scrimTop: ImageView,
        scrimBottom: ImageView,
        imageView: ImageView,
        imageHierarchyList: ArrayList<Int>,
        containerWI: SquareCardView
    ) {

        val intent: Intent = Intent(mContext, FestivalDetailsPage::class.java)
        intent.putExtra("Festival Data", festival)
        intent.putExtra("imageHierarchyList", imageHierarchyList)
        intent.putExtra("month", currentMonthIndex)
        intent.putExtra("year", 2021)

//        val pair1: Pair<View, String> = Pair(scrimTop, "topScrimT")
//        val pair2: Pair<View, String> = Pair(scrimBottom, "bottomScrimT")
        val pair3: Pair<View, String> = Pair(imageView, "FestivalImageT")
//        val pair4: Pair<View, String> = Pair(containerWI, "festivalImageContainerT")
//        val pair5: Pair<View, String> = Pair(constraintContainer, "constraintLayoutImageContainerT")


        val optionCompact: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                pair3
            )

        requireActivity().startActivity(intent, optionCompact.toBundle())
    }

}