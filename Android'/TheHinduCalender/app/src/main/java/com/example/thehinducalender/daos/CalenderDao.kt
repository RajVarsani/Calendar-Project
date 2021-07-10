package com.example.thehinducalender.daos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.thehinducalender.R


class CalenderDao {

    private val monthWiseFirstDay = arrayOf(0, 2, 6, 4, 5, 2, 3, 2, 2, 2, 3, 1)

    //    private val dataHandlerDao = DataHandlerDao()
    private val viewsSetterDao = ViewsSetterDao()


    fun loadJsonDataFromAssets(context: Context, languagePreference: Int) {
        viewsSetterDao.loadJsonDataFromAssets(
            context,
            languagePreference
        )
    }


    fun setHomePageCalenderTable(
        table: TableLayout,
        month: Int,
        applicationContext: Context,
    ) {


        var currentDateIndex = 1
        val noOfDaysInThisMonth = viewsSetterDao.noOfDaysInMonth(month + 1)
        var row: TableRow = table.getChildAt(0) as TableRow
        val startPoint: Int = viewsSetterDao.getStartIndexOfTheMonth(month + 1)




        for (i in 0..6) {
            val child: TextView = row.getChildAt(i) as TextView

            if (i < startPoint) {
                child.text = "-"

                child.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.g_grey_text_color_one
                    )
                )
            } else {
                child.text = currentDateIndex++.toString()
            }
        }


        for (i in 1..3) {
            row = table.getChildAt(i) as TableRow
            for (j in 0..6) {

                (row.getChildAt(j) as TextView).text = currentDateIndex++.toString()
            }
        }



        for (i in 4..5) {
            row = table.getChildAt(i) as TableRow

            for (j in 0..6) {

                val child: TextView = row.getChildAt(j) as TextView

                if (currentDateIndex > noOfDaysInThisMonth) {
                    child.text = "-"

                    child.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.g_grey_text_color_one
                        )
                    )
                } else {
                    child.text = currentDateIndex++.toString()
                }
            }

        }


    }

    @SuppressLint("SetTextI18n")
    fun setUpMainCalenderTable(
        calenderTable: TableLayout,
        context: Context,
        languagePreference: Int,
        month: Int
    ) {

        var currentDateIndex = 1
        val noOfDaysInThisMonth = viewsSetterDao.noOfDaysInMonth(month)
        val startPoint: Int = viewsSetterDao.getStartIndexOfTheMonth(month)
        var row: TableRow = calenderTable.getChildAt(0) as TableRow

        val (amasList, poonamList, fatsDayList) = viewsSetterDao.getAmasPoonamFastDaysListOfTheMonth(
            month,
            2021,
            languagePreference
        )

        val specialityList = viewsSetterDao.getFestivalsListOfTheMonth(month, languagePreference)


        Log.e(
            "MainCCHK", "data: \n" +
                    "languagePreference : $languagePreference\n" +
                    "currentDateIndex : $currentDateIndex\n" +
                    "noOfDaysInThisMonth : $noOfDaysInThisMonth \n " +
                    "startPoint $startPoint \n " +
                    "amasList  : $amasList \n" +
                    "poonamList  : $poonamList \n" +
                    "fatsDayList  : $fatsDayList \n" +
                    "specialityList $specialityList"
        )


        fixViewsErrors(calenderTable, context)


        for (j in 0..6) {

            row = calenderTable.getChildAt(j) as TableRow

            val container = (row.getChildAt(0) as LinearLayout)
            if (j < startPoint) {

                viewsSetterDao.setThisMainCalenderChildEmptyText(
                    container.getChildAt(0) as TextView,
                    context
                )

                container.getChildAt(1).visibility = View.INVISIBLE

                if (j == 0) {
                    container.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.sunday_red
                        )
                    )
                }

                container.getChildAt(2).visibility = View.GONE


            } else {
                viewsSetterDao.setUpMainCalenderChild(
                    container,
                    amasList,
                    poonamList,
                    fatsDayList,
                    context,
                    currentDateIndex,
                    j == 0,
                    month,
                    specialityList,
                    languagePreference,
                    false
                )

                currentDateIndex++
            }


        }

//        var endPos1: Int
//        var endPos2: Int


        for (i in 1..4) {
            for (j in 0..6) {
                row = calenderTable.getChildAt(j) as TableRow
                val container = (row.getChildAt(i) as LinearLayout)

                if (currentDateIndex > noOfDaysInThisMonth) {

                    container.getChildAt(1).visibility = View.INVISIBLE
                    (container.getChildAt(1) as TextView).text = " \n "
                    viewsSetterDao.setThisMainCalenderChildEmptyText(
                        container.getChildAt(0) as TextView,
                        context
                    )

                    if (j == 0) {
                        container.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.sunday_red
                            )
                        )
                    }
                    container.getChildAt(2).visibility = View.GONE


                } else {


                    viewsSetterDao.setUpMainCalenderChild(
                        container,
                        amasList,
                        poonamList,
                        fatsDayList,
                        context,
                        currentDateIndex,
                        j == 0,
                        month,
                        specialityList,
                        languagePreference,
                        j == 6 && (i % 2 != 0) && i <= 3
                    )

//                    if (currentDateIndex == noOfDaysInThisMonth) {
//                        endPos1 = i
//                        endPos2 = j
//                    }

                    currentDateIndex++
                }


            }
        }


        var tempPos = 0
        while (currentDateIndex <= noOfDaysInThisMonth) {

            row = calenderTable.getChildAt(tempPos) as TableRow

            val container = (row.getChildAt(0) as LinearLayout)
            container.getChildAt(1).visibility = View.VISIBLE
            (container.getChildAt(1) as TextView).setTextAppearance(R.style.styleForMainCalenderMonthNameChildPrimary)
            (container.getChildAt(0) as TextView).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.grey_text_color_for_main_calender_children_date_index
                )
            )



            viewsSetterDao.setUpMainCalenderChild(
                container,
                amasList,
                poonamList,
                fatsDayList,
                context,
                currentDateIndex,
                tempPos == 0,
                month,
                specialityList,
                languagePreference,
                false
            )

            currentDateIndex++
        }


    }

    private fun fixViewsErrors(calenderTable: TableLayout, context: Context) {
        for (i in 0..6) {

            var row = calenderTable.getChildAt(i) as TableRow
            for (j in 0..4) {

                val container = row.getChildAt(j) as LinearLayout


                val dateIndexText = container.getChildAt(0) as TextView
                val monthAndTithi = container.getChildAt(1) as TextView
                val specialityTextView = container.getChildAt(2) as TextView



                if (dateIndexText.currentTextColor != R.color.grey_text_color_for_main_calender_children_date_index) {
                    dateIndexText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grey_text_color_for_main_calender_children_date_index
                        )
                    )
                }

//
//                viewsSetterDao.setThisMainCalenderChildEmptyText(
//                    container.getChildAt(0) as TextView,
//                    context
//                )

                if ((container.background as ColorDrawable).color != R.color.white) {
                    container.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }


                if ((dateIndexText).background != null) {
                    (dateIndexText).background = null
                }

                if (specialityTextView.visibility == View.VISIBLE) {
                    specialityTextView.visibility = View.GONE
                }
                if (monthAndTithi.visibility == View.GONE) {
                    monthAndTithi.visibility = View.VISIBLE
                }

                if (monthAndTithi.maxLines != 2) {
                    monthAndTithi.maxLines = 2
                }


                monthAndTithi.setTextAppearance(R.style.styleForMainCalenderMonthNameChildPrimary)

            }
        }
    }

}