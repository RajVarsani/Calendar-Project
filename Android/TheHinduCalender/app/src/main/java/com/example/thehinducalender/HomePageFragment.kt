package com.example.thehinducalender

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.daos.DataHandlerDao
import com.example.thehinducalender.daos.ViewsSetterDao
import kotlinx.android.synthetic.main.choghdiya_contaier_layout.*
import kotlinx.android.synthetic.main.current_day_data_layout.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.home_page_calender_layout.*
import kotlinx.android.synthetic.main.quote_of_the_day_layout.*
import kotlinx.android.synthetic.main.sunrise_sunset_timings_layout.*



class HomePageFragment : Fragment(R.layout.fragment_home_page), View.OnClickListener {

    private lateinit var mContext: Context


    private lateinit var viewsSetterDao: ViewsSetterDao
    private lateinit var dataHandlerDao: DataHandlerDao
    private var currentPositionOfCalender = 0

    private var languagePreference: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewsSetterDao = ViewsSetterDao()
        dataHandlerDao = DataHandlerDao()


        setUp()
    }

    /**   Initializing all necessary listeners, variables and Views on creating activity **/
    private fun setUp() {


        languagePreference = setUpUserPreferences()

        viewsSetterDao.loadJsonDataFromAssets(
            mContext,
            languagePreference
        )

        viewsSetterDao.setUpQuoteOfTheDayViews(
            quoteTextView,
            authorNameTextView,
            languagePreference
        )

        viewsSetterDao.setUpZodiacSign(zodiacSignValueTextView, languagePreference)

        viewsSetterDao.setUpDateAndTithi(
            englishCalenderDate,
            hinduCalenderDate,
            hinduCalenderYear,
            languagePreference
        )

        viewsSetterDao.setUpTodaysSpeciality(todaySpecialityContainer, languagePreference, mContext)

        viewsSetterDao.setUpChoghadiya(
            currentChoghadiyaNameTextView,
            nextChoghadiyaNameTextView,
            languagePreference
        )
        viewsSetterDao.sunriseSunsetSetter(
            sunriseTimingTextView,
            sunsetTimingTextView,
            languagePreference
        )

        setUpHomePageCalenderRecyclerView()

        monthChangingButtonForHomePageCalenderRight.setOnClickListener(this)
        monthChangingButtonForHomePageCalenderLeft.setOnClickListener(this)

    }

    private fun setUpUserPreferences(): Int {
        val pref = this.requireActivity().getSharedPreferences ("UsersSettingPref", MODE_PRIVATE)
        Log.e(
            "check",
            "isFirstTime is ${
                pref.getBoolean(
                    "isFirstTime",
                    true
                )
            }  and \n month ${pref.getInt("LanguagePreference", 0)}"
        )


        if (pref.getBoolean("isFirstTime", true) || pref.getInt("LanguagePreference", 0) !in 0..2) {
            setupForFirstTime()
        }

        return this.requireActivity().getPreferences(Context.MODE_PRIVATE)
            .getInt("LanguagePreference", 0)
    }


    private fun setUpHomePageCalenderRecyclerView() {

        val ad = HomePageCalenderAdapter(
            mContext, languagePreference
        )
        homePageCalenderRecyclerView.adapter = ad
        homePageCalenderRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        val snapHelperForHomePageCalender = PagerSnapHelper()
        snapHelperForHomePageCalender.attachToRecyclerView(homePageCalenderRecyclerView)


        homePageCalenderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView =
                        snapHelperForHomePageCalender.findSnapView(homePageCalenderRecyclerView.layoutManager)
                    currentPositionOfCalender =
                        homePageCalenderRecyclerView.layoutManager!!.getPosition(centerView!!)


                    Log.e("Snapped Item Position:", "" + currentPositionOfCalender)

                    viewsSetterDao.changeHomePageCalenderDisplayMonthTo(
                        currentPositionOfCalender,
                        monthNameTextViewForHomePageCalender,
                        languagePreference
                    )

                }
            }
        })

        scrollCalenderToCurrentMonth()

    }

    private fun scrollCalenderToCurrentMonth() {

        currentPositionOfCalender = viewsSetterDao.currentMonth() - 1
        homePageCalenderRecyclerView.scrollToPosition(currentPositionOfCalender)

        viewsSetterDao.changeHomePageCalenderDisplayMonthTo(
            currentPositionOfCalender,
            monthNameTextViewForHomePageCalender,
            languagePreference
        )

    }


    private fun scrollCalenderToPreviousMonth() {
        if (currentPositionOfCalender != 0) {
            homePageCalenderRecyclerView.smoothScrollToPosition(currentPositionOfCalender - 1)
            currentPositionOfCalender--

            viewsSetterDao.changeHomePageCalenderDisplayMonthTo(
                currentPositionOfCalender,
                monthNameTextViewForHomePageCalender,
                languagePreference
            )

        }
    }

    private fun scrollCalenderToNextMonth() {
        if (currentPositionOfCalender < 11) {
            homePageCalenderRecyclerView.smoothScrollToPosition(currentPositionOfCalender + 1)
            currentPositionOfCalender++
            viewsSetterDao.changeHomePageCalenderDisplayMonthTo(
                currentPositionOfCalender,
                monthNameTextViewForHomePageCalender,
                languagePreference
            )
        }


    }


    private fun setupForFirstTime() {
        val pref = this.requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()

        editor.putInt("LanguagePreference", 0)
        editor.apply()
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.monthChangingButtonForHomePageCalenderRight -> {
                    scrollCalenderToNextMonth()
                }
                R.id.monthChangingButtonForHomePageCalenderLeft -> {
                    scrollCalenderToPreviousMonth()
                }
            }
        }
    }


}