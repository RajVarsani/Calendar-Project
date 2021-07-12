package com.example.thehinducalender

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.thehinducalender.daos.ViewsSetterDao
import kotlinx.android.synthetic.main.fragment_main_calender.*


class MainCalenderFragment : Fragment(), View.OnClickListener {
    private lateinit var viewsSetterDao: ViewsSetterDao
    private var languagePreference = 0
    private var currentPositionOfCalender = 0

    private lateinit var mContext : Context



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mContext = inflater.context
        return inflater.inflate(R.layout.fragment_main_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewsSetterDao = ViewsSetterDao()
        languagePreference = this.requireActivity().getPreferences(Context.MODE_PRIVATE).getInt("LanguagePreference", 0)
        viewsSetterDao.loadJsonDataFromAssets(mContext, languagePreference)
        setUp()
    }

    private fun setUp() {

        setUpCalenderRecyclerView()

        monthChangingButtonForCalenderPageCalenderLeft.setOnClickListener(this)
        monthChangingButtonForCalenderPageCalenderRight.setOnClickListener(this)

    }

    private fun setUpCalenderRecyclerView() {

        calenderPageCalenderRecyclerView.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        val mainCalenderAdapter = MainCalenderRecyclerViewAdapter(mContext, languagePreference)
        calenderPageCalenderRecyclerView.adapter = mainCalenderAdapter

        val snapHelperForMainCalender = PagerSnapHelper()
        snapHelperForMainCalender.attachToRecyclerView(calenderPageCalenderRecyclerView)


        calenderPageCalenderRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView =
                        snapHelperForMainCalender.findSnapView(calenderPageCalenderRecyclerView.layoutManager)
                    currentPositionOfCalender =
                        calenderPageCalenderRecyclerView.layoutManager!!.getPosition(centerView!!)


                    Log.e("Snapped Item Position:", "" + currentPositionOfCalender)
                    calenderPageCalenderRecyclerView.recycledViewPool.clear()

                    viewsSetterDao.changeCalenderPageDisplayMonth(
                        currentPositionOfCalender,
                        monthNameTextViewForCalenderPageCalender,
                        hinduMonthNameTextViewForCalenderPageCalender,
                        languagePreference
                    )

                }
            }


        })

        scrollCalenderToCurrentMonth()

    }

    private fun scrollCalenderToCurrentMonth() {
        currentPositionOfCalender = viewsSetterDao.currentMonth() - 1
        calenderPageCalenderRecyclerView.scrollToPosition(currentPositionOfCalender)
        viewsSetterDao.changeCalenderPageDisplayMonth(
            currentPositionOfCalender,
            monthNameTextViewForCalenderPageCalender,
            hinduMonthNameTextViewForCalenderPageCalender,
            languagePreference
        )

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.monthChangingButtonForCalenderPageCalenderRight -> {
                    scrollMainCalenderToNextMonth()
                }
                R.id.monthChangingButtonForCalenderPageCalenderLeft -> {
                    scrollMainCalenderToPreviousMonth()
                }
            }
        }
    }

    private fun scrollMainCalenderToNextMonth() {

        if (currentPositionOfCalender < 11) {
            calenderPageCalenderRecyclerView.recycledViewPool.clear()

            currentPositionOfCalender++
            calenderPageCalenderRecyclerView.smoothScrollToPosition(currentPositionOfCalender)
            viewsSetterDao.changeCalenderPageDisplayMonth(
                currentPositionOfCalender,
                monthNameTextViewForCalenderPageCalender,
                hinduMonthNameTextViewForCalenderPageCalender,
                languagePreference
            )
        }


    }

    private fun scrollMainCalenderToPreviousMonth() {
        if (currentPositionOfCalender > 0) {
            calenderPageCalenderRecyclerView.recycledViewPool.clear()

            currentPositionOfCalender--
            calenderPageCalenderRecyclerView.smoothScrollToPosition(currentPositionOfCalender)
            viewsSetterDao.changeCalenderPageDisplayMonth(
                currentPositionOfCalender,
                monthNameTextViewForCalenderPageCalender,
                hinduMonthNameTextViewForCalenderPageCalender,
                languagePreference
            )
        }


    }

}