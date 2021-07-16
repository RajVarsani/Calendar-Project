package com.example.thehinducalender.daos

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.thehinducalender.FestivalsListRecyclerViewAdapter
import com.example.thehinducalender.HomePageFestivalsListAdapter
import com.example.thehinducalender.IFestivalsListRecyclerViewAdapter
import com.example.thehinducalender.R
import com.example.thehinducalender.models.Festival
import com.example.thehinducalender.models.HomePageFestivalListModel
import com.example.thehinducalender.models.Quote
import com.factor.bouncy.BouncyRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ViewsSetterDao {

    private val dataHandlerDao: DataHandlerDao = DataHandlerDao()
    private val defaultQuoteForErrorHandling = Quote(
        "Whoever is happy will make others happy too.", "- Anne Frank"
    )

    private lateinit var festivalsAndHolidaysPageFestivalsList: ArrayList<Festival>
    private lateinit var adapterForFestivalAndHolidayList: FestivalsListRecyclerViewAdapter

    private val monthNames = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    )

    private val choiceTextListForFestivalsFilter = arrayOf(
        "Show All",
        "Festivals Only",
        "Bank Holidays Only"
    )


    private val primaryColorsList = arrayOf(
        R.color.green_dark_for_festivals_list,
        R.color.orange_dark_for_festivals_list,
        R.color.blue_dark_for_festivals_list
    )
    private val secondaryColorsList = arrayOf(
        R.color.green_faded_for_festivals_list,
        R.color.orange_faded_for_festivals_list,
        R.color.blue_faded_for_festivals_list
    )


    fun loadJsonDataFromAssets(context: Context, languagePreference: Int) {
        dataHandlerDao.loadJsonDataFromAssets(
            context,
            languagePreference
        )
    }


    @SuppressLint("SetTextI18n")
    fun changeHomePageCalenderDisplayMonthTo(
        monthIndex: Int,
        monthDisplayingTextView: TextView,
        languagePreference: Int
    ) {
        monthDisplayingTextView.text = "${monthNames[monthIndex]}\n${
            dataHandlerDao.hinduMonthForThisMonth(
                monthIndex,
                languagePreference
            )
        }"
    }

    fun setUpZodiacSign(zodiacSignTextView: TextView, languagePreference: Int) {
        Log.e(
            "Zodiac sin",
            "sign :${
                dataHandlerDao.getZodiacSign(
                    currentMonth() - 1,
                    todayDate() - 1,
                    languagePreference
                )
            }"
        )

        zodiacSignTextView.text = dataHandlerDao
            .getZodiacSign(currentMonth() - 1, todayDate() - 1, languagePreference)
    }


    fun setUpDateAndTithi(

        dateTextView: TextView,
        tithiTextView: TextView,
        hinduYearTextView: TextView,
        languagePreference: Int
    ) {
        dateTextView.text = todayDateInParsedFormat()

        tithiTextView.text =
            dataHandlerDao.currentTithi(currentMonth() - 1, todayDate() - 1, languagePreference)

        hinduYearTextView.text = currentHinduYearInFormat()
    }


    fun setUpQuoteOfTheDayViews(
        quoteTextView: TextView,
        authorName: TextView,
        languagePreference: Int
    ) {

        val quote: Quote =
            if (dataHandlerDao.quoteOfTheDayFOrDate(todayDate(), languagePreference) != null) {
                dataHandlerDao.quoteOfTheDayFOrDate(todayDate(), languagePreference)!!
            } else {
                defaultQuoteForErrorHandling
            }

        Log.e("Default Quote", "Default Quote is : $defaultQuoteForErrorHandling \n quote $quote")

        quoteTextView.text = quote.quote
        authorName.text = quote.author

    }


    fun setUpTodaysSpeciality(
        festivalsListRecyclerView: RecyclerView,
        languagePreference: Int,
        context: Context
    ) {
        var festivalsList: ArrayList<HomePageFestivalListModel>? =
            dataHandlerDao.getTodaysSpecialityList(currentMonth(), todayDate(), languagePreference)
        if (festivalsList == null) {
            festivalsList = ArrayList<HomePageFestivalListModel>(0)
        }

        festivalsListRecyclerView.layoutManager = LinearLayoutManager(context)
        val homePageFestivalsListAdapter = HomePageFestivalsListAdapter(festivalsList)
        festivalsListRecyclerView.adapter = homePageFestivalsListAdapter

    }

    fun setUpChoghadiya(
        currentChoghadiyaTextView: TextView,
        nextChoghadiyaTextView: TextView,
        languagePreference: Int
    ) {

        val choghadiya = dataHandlerDao.getChoghadiya(
            currentMonth(),
            currentDayOfWeek(),
            currentHour(),
            currentMinute(),
            languagePreference
        )

        Log.e(
            "Choghadiya setter",
            "setter is being called for ${currentMonth()}, ${currentDayOfWeek()}, ${currentHour()} , ${currentMinute()} $languagePreference"
        )
        Log.e("Received choghadiya", "receive choghadiya is : $choghadiya  ")
        if (choghadiya != null) {
            currentChoghadiyaTextView.text = choghadiya.split(":")[0]
            nextChoghadiyaTextView.text = choghadiya.split(":")[1]
        }

    }


    fun sunriseSunsetSetter(
        sunriseTextView: TextView,
        sunsetTextView: TextView,
        languagePreference: Int
    ) {
        sunriseTextView.text =
            dataHandlerDao.getSunriseTime(currentMonth(), todayDate(), languagePreference)
        sunsetTextView.text =
            dataHandlerDao.getSunSetTime(currentMonth(), todayDate(), languagePreference)
    }


    fun setUpFestivalsListRecyclerView(
        recyclerView: RecyclerView,
        languagePreference: Int,
        context: Context,
        currentChoiceForDataDisplay: Int,
        listener: IFestivalsListRecyclerViewAdapter
    ) {
        setFestivalsListRecyclerView(
            recyclerView,
            languagePreference,
            currentMonth(),
            currentYear(),
            context,
            currentChoiceForDataDisplay,
            listener
        )
    }

    fun setFestivalsListRecyclerView(
        recyclerView: RecyclerView,
        languagePreference: Int,
        month: Int,
        year: Int,
        context: Context,
        currentChoiceForDataDisplay: Int,
        listener: IFestivalsListRecyclerViewAdapter
    ) {
        val primaryFestivalsList =
            dataHandlerDao.getFestivalsListOfTheMonth(month, languagePreference)

        val secondaryHolidaysList: ArrayList<Festival> =
            getBankHolidaysListInThisMonth(month, year, languagePreference)


        festivalsAndHolidaysPageFestivalsList =
            mergeLists(primaryFestivalsList, secondaryHolidaysList)

        adapterForFestivalAndHolidayList =
            FestivalsListRecyclerViewAdapter(
                festivalsAndHolidaysPageFestivalsList,
                context,
                month,
                year,
                languagePreference,
                currentChoiceForDataDisplay,
                listener
            )
        recyclerView.adapter = adapterForFestivalAndHolidayList


    }


    private fun mergeLists(
        primaryFestivalsList: java.util.ArrayList<Festival>?,
        secondaryHolidaysList: java.util.ArrayList<Festival>
    ): java.util.ArrayList<Festival> {

        var currentIndexForFirstList = 0
        var currentIndexForSecondList = 0

        var sizeOfFirstList = primaryFestivalsList?.size
        if (sizeOfFirstList == null) {
            sizeOfFirstList = 0
        }
        val sizeOfSecondList = secondaryHolidaysList.size


        val listToReturn = ArrayList<Festival>(0)

        for (i in 0..30) {
            for (j in currentIndexForFirstList until sizeOfFirstList) {

                if (primaryFestivalsList!![j].date.toInt() == i + 1) {
                    listToReturn.add(primaryFestivalsList[j])
                    currentIndexForFirstList++
                }
                if (primaryFestivalsList[j].date.toInt() > i + 1) {
                    break
                }
            }
            for (j in currentIndexForSecondList until sizeOfSecondList) {

                if (secondaryHolidaysList[j].date.toInt() == i + 1) {
                    listToReturn.add(secondaryHolidaysList[j])
                    currentIndexForSecondList++
                }
                if (secondaryHolidaysList[j].date.toInt() > i + 1) {
                    break
                }
            }
        }

        return listToReturn
    }

    fun getBankHolidaysListInThisMonthParsedToIntegerList(
        month: Int,
        year: Int,
        languagePreference: Int
    ): ArrayList<Int> {

        val tempList = getBankHolidaysListInThisMonth(
            month,
            year,
            languagePreference
        )

        val listToReturn = ArrayList<Int>(0)
        for (i in 0 until tempList.size) {
            listToReturn.add(tempList[i].date.toInt())
        }

        return listToReturn
    }

    private fun getBankHolidaysListInThisMonth(
        month: Int,
        year: Int,
        languagePreference: Int
    ): ArrayList<Festival> {

        var noOfSaturdays = 0
        val listToReturn = ArrayList<Festival>(0)

        for (i in 0 until noOfDaysInMonth(month - 1)) {
            if (isThisDay(i + 1, month, year, 7)) {
                noOfSaturdays++
                if (noOfSaturdays % 2 == 0) {
                    listToReturn.add(
                        Festival(
                            true,
                            "Bank Holiday",
                            (i + 1).toString()
                        )
                    )
                }
            }
        }

        return listToReturn
    }


    fun noOfDaysInMonth(month: Int): Int {
        return dataHandlerDao.noOfDaysInMonth(month - 1)
    }

    fun setDayByMonthAndDate(
        dateTextView: TextView,
        monthIndex: Int,
        date: Int,
        year: Int,
        languagePreference: Int
    ) {


//        Log.e(
//            "DayByMonth", "Inputs :  dateTextView: $dateTextView,\n" +
//                    "monthIndex $monthIndex,\n" +
//                    "date $date,\n" +
//                    "year $year,\n" +
//                    "languagePreference $languagePreference"
//        )

        try {

            dateTextView.text = getDayByDateMonthYear(date, monthIndex, year, languagePreference)


//            Log.e(
//                "DayOfWeek",
//                "DayOfWeek is $dayOfWeek and dateTextView.text = ${
//                    getDayByDateMonthYear(
//                        date,
//                        monthIndex,
//                        year,
//                        languagePreference
//                    )
//                }"
//            )

        } catch (dateFormatException: Exception) {
            Log.e(
                "dateF E",
                "error is : $dateFormatException for month $monthIndex \n date $date \nyear $year"
            )
        }

    }


    fun updateFestivalListRecyclerViewChoice(
        recyclerView: BouncyRecyclerView,
        newChoice: Int,
        month: Int,
        year: Int,
        languagePreference: Int,
        context: Context,
        listener: IFestivalsListRecyclerViewAdapter
    ) {
//        recyclerView.adapter = null
//        recyclerView.layoutManager = null
//        recyclerView.layoutManager=LinearLayoutManager(context)
//        adapterForFestivalAndHolidayList.currentChoiceForDataDisplay =newChoice
//        recyclerView.adapter =adapterForFestivalAndHolidayList
//        adapterForFestivalAndHolidayList.notifyDataSetChanged()


        val tempAdapter =
            FestivalsListRecyclerViewAdapter(
                festivalsAndHolidaysPageFestivalsList,
                context,
                month,
                year,
                languagePreference,
                newChoice,
                listener

            )
        recyclerView.adapter = tempAdapter
//        recyclerView.recycledViewPool.clear()


    }

    fun setRandomColorToTheseFestivalsListObj(
        titleWOI: TextView,
        dateWOI: TextView,
        containerWOI: ConstraintLayout,
        context: Context
    ) {

        val randomInt = (0..2).random()

        titleWOI.setTextColor(
            ContextCompat.getColor(
                context,
                primaryColorsList[randomInt]
            )
        )
        dateWOI.setTextColor(
            ContextCompat.getColor(
                context,
                primaryColorsList[randomInt]
            )
        )
        containerWOI.setBackgroundColor(
            ContextCompat.getColor(
                context,
                secondaryColorsList[randomInt]
            )
        )

    }

    fun setImage(
        context: Context,
        url: Array<String>,
        imageView: ImageView,
        imageHierarchyList: ArrayList<Int>
    ) {


        val firstTime = (0..2).random()
        var secondTIme = 0
        while (secondTIme == firstTime) {
            secondTIme = (0..2).random()
        }

        var thirdTime = 0
        while (thirdTime == firstTime || thirdTime == secondTIme) {
            thirdTime = (0..2).random()
        }

        imageHierarchyList.add(firstTime)
        imageHierarchyList.add(secondTIme)
        imageHierarchyList.add(thirdTime)



        Glide
            .with(context)
            .load(url[firstTime])
            .placeholder(R.color.g_grey_text_color_one)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(
                Glide.with(context).load(url[secondTIme])
                    .transition(DrawableTransitionOptions.withCrossFade(300))
            )
            .error(
                Glide.with(context).load(url[thirdTime])
                    .transition(DrawableTransitionOptions.withCrossFade(300))
            )
            .into(imageView)

    }


    fun changeFestivalsListPageCalenderDisplayMonthTo(
        monthIndex: Int,
        monthDisplayingTextView: TextView,
        languagePreference: Int
    ) {
        monthDisplayingTextView.text = monthNames[monthIndex]
    }

    fun changeFestivalsListPageDataDisplayChoiceTextView(
        choiceIndex: Int,
        monthDisplayingTextView: TextView,
        languagePreference: Int
    ) {
        monthDisplayingTextView.text = choiceTextListForFestivalsFilter[choiceIndex]
    }


    /** Main Calender**/


    fun getAmasPoonamFastDaysListOfTheMonth(
        month: Int,
        year: Int,
        languagePreference: Int
    ): Triple<ArrayList<Int>, ArrayList<Int>, ArrayList<Int>> {

        val tithiListOfTHeMonth = dataHandlerDao.loadTithiDataOfMonth(month, languagePreference)

        val amasList = ArrayList<Int>(0)
        val poonamList = ArrayList<Int>(0)
        val fastDaysList = ArrayList<Int>(0)

        for (i in 0 until tithiListOfTHeMonth.size) {
            when (tithiListOfTHeMonth[i]) {
                0 -> {
                    amasList.add(i + 1)
                }
                11 -> {
                    fastDaysList.add(i + 1)
                }
                15 -> {
                    poonamList.add(i + 1)
                }
            }
        }

        return Triple(amasList, poonamList, fastDaysList)

    }

    fun setThisMainCalenderChildEmptyText(textView: TextView, context: Context) {
        textView.text = "-"
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.grey_text_color_for_main_calender_children_date_index_null
            )
        )
    }


    fun getFestivalsListOfTheMonth(month: Int, languagePreference: Int): ArrayList<Festival>? {
        return dataHandlerDao.getFestivalsListOfTheMonth(month, languagePreference)
    }


    fun setUpMainCalenderChild(
        container: LinearLayout,
        amasList: java.util.ArrayList<Int>,
        poonamList: java.util.ArrayList<Int>,
        fastDayList: java.util.ArrayList<Int>,
        context: Context,
        currentDateIndex: Int,
        isSunday: Boolean,
        month: Int,
        specialityList: ArrayList<Festival>?,
        languagePreference: Int,
        isBankHoliday: Boolean
    ) {

        val dateIndexText = container.getChildAt(0) as TextView
        val monthAndTithi = container.getChildAt(1) as TextView
        val specialityTextView = container.getChildAt(2) as TextView

        dateIndexText.text = currentDateIndex.toString()
        if (monthAndTithi.visibility == View.GONE) {
            monthAndTithi.visibility = View.VISIBLE
        }



        when {
            isBankHoliday -> {
                when (currentDateIndex) {
                    in amasList -> {
                        dateIndexText.setBackgroundResource(R.drawable.c_bank_holiday_plus_amas_bg)
                        dateIndexText.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.white_primary_text_color
                            )
                        )

                    }
                    in poonamList -> {
                        dateIndexText.setBackgroundResource(R.drawable.c_bank_holiday_plus_poonam_bg)
                    }
                    in fastDayList -> {
                        dateIndexText.setBackgroundResource(R.drawable.c_bank_holiday_plus_fast_bg)
                    }
                    else -> {
                        dateIndexText.setBackgroundResource(R.drawable.c_bank_holiday_bg)

                    }
                }
            }
            currentDateIndex in poonamList -> {
                dateIndexText.setBackgroundResource(R.drawable.c_poonam_bg_for_main_calender)
            }
            currentDateIndex in amasList -> {

                Log.e(
                    "Amas Set", "Amas Set for :" +
                            " container: $container \n" +
                            "        amasList: $amasList \n" +
                            "        poonamList: $poonamList \n" +
                            "        fastDayList: $fastDayList \n" +
                            "        context: $context \n" +
                            "        currentDateIndex: $currentDateIndex \n" +
                            "        isSunday: $isSunday \n" +
                            "        month: $month \n" +
                            "        specialityList: $specialityList \n" +
                            "        languagePreference: $languagePreference \n" +
                            "        isBankHoliday $isBankHoliday: "
                )

                dateIndexText.setBackgroundResource(R.drawable.c_amas_bg_for_main_calender)
                dateIndexText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white_primary_text_color
                    )
                )
            }
            currentDateIndex in fastDayList -> {
                dateIndexText.setBackgroundResource(R.drawable.c_fast_bg_for_main_calender)
            }
        }


        val specialityListOfTheDay = dayHasAnySpeciality(specialityList, currentDateIndex)

        if (specialityListOfTheDay.size != 0) {
            monthAndTithi.text = tithiTextInOneLine(month, currentDateIndex, languagePreference)
            monthAndTithi.maxLines = 1

            if (monthAndTithi.ellipsize != TextUtils.TruncateAt.END) {
                monthAndTithi.ellipsize = TextUtils.TruncateAt.END
            }

            monthAndTithi.setTextAppearance(R.style.styleForMainCalenderMonthNameChildSecondary)
            container.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.speciality_orange
                )
            )

            specialityTextView.visibility = View.VISIBLE


            if (specialityTextView.maxLines != 1) {
                specialityTextView.maxLines = 1
            }

            if (specialityTextView.ellipsize != TextUtils.TruncateAt.END) {
                specialityTextView.ellipsize = TextUtils.TruncateAt.END
            }

            specialityTextView.text = specialityListOfTheDay[0].name
        } else {
            monthAndTithi.text = tithiTextInTwoLines(month, currentDateIndex, languagePreference)
        }

        if (isSunday) {
            container.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.sunday_red
                )
            )
        }


    }

    private fun dayHasAnySpeciality(
        specialityList: java.util.ArrayList<Festival>?,
        currentDate: Int
    ): ArrayList<Festival> {
        val listToReturn = ArrayList<Festival>(0)

        if (specialityList != null) {
            for (i in 0 until specialityList.size) {
                if (specialityList[i].date.toInt() == currentDate) {
                    listToReturn.add(specialityList[i])
//                    return true
                }
            }
        }
        return listToReturn
    }

    private fun tithiTextInTwoLines(month: Int, day: Int, languagePreference: Int): String {
        val tithiStringArr = dataHandlerDao.currentTithi(month - 1, day - 1, languagePreference)
            ?.split(" ")

        return if (tithiStringArr?.size == 2) {
            "${tithiStringArr[0]}\n${tithiStringArr[1]}"
        } else {
            "${tithiStringArr?.get(0)}\n${tithiStringArr?.get(1)} ${tithiStringArr?.get(2)}"
        }

    }

    private fun tithiTextInOneLine(month: Int, day: Int, languagePreference: Int): String? {
        return dataHandlerDao.currentTithi(month - 1, day - 1, languagePreference)
    }


    @SuppressLint("SetTextI18n")
    fun changeCalenderPageDisplayMonth(
        monthIndex: Int,
        monthNameTextViewForCalenderPageCalender: TextView,
        hinduMonthNameTextViewForCalenderPageCalender: TextView,
        languagePreference: Int
    ) {
        monthNameTextViewForCalenderPageCalender.text = monthNames[monthIndex]

        hinduMonthNameTextViewForCalenderPageCalender.text = "${
            dataHandlerDao.hinduMonthForThisMonth(
                monthIndex,
                languagePreference
            )
        }"
    }


    /** Basics**/


    private fun getDayByDateMonthYear(
        date: Int,
        month: Int,
        year: Int,
        languagePreference: Int
    ): String {
        return "$date/$month/$year-${
            SimpleDateFormat("EE", Locale.ENGLISH).format(
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.ENGLISH
                ).parse("${formatDigitForDate(date)}/${formatDigitForDate(month)}/$year")!!
            )
        }"
    }

    private fun isThisDay(
        currentDay: Int,
        month: Int,
        year: Int,
        dayOfWeek: Int
    ): Boolean {

        val calendarObj = Calendar.getInstance()
        calendarObj.time =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.ENGLISH
            ).parse("${formatDigitForDate(currentDay)}/${formatDigitForDate(month)}/$year")!!
        return calendarObj[Calendar.DAY_OF_WEEK] == dayOfWeek

    }

    private fun formatDigitForDate(digit: Int): String {
        return if (digit < 10) {
            "0$digit"
        } else {
            digit.toString()
        }
    }


    private fun todayDate(): Int {
        val simpleDateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    fun currentMonth(): Int {
        val simpleDateFormat = SimpleDateFormat("MM", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    fun currentYear(): Int {
        val simpleDateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    private fun todayDateInParsedFormat(): String {
//        val simpleDateFormat = SimpleDateFormat("dd MM, yyyy", Locale.ENGLISH)
        return "${todayDate()} ${monthNames[currentMonth() - 1].substring(0, 3)}, ${currentYear()}"
    }

    private fun currentHinduYearInFormat(): String {
        return if (currentMonth() < 11) {
            "Vikram Era 2077"
        } else if (currentMonth() == 11) {
            if (todayDate() >= 5) {
                "Vikram Era 2078"
            } else {
                "Vikram Era 2077"
            }
        } else {
            "Vikram Era 2077"
        }
    }


    fun currentDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.DAY_OF_WEEK]
    }

    fun currentHour(): Int {
        val simpleDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    fun currentMinute(): Int {
        val simpleDateFormat = SimpleDateFormat("mm", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    fun getStartIndexOfTheMonth(month: Int): Int {

        return dataHandlerDao.startIndexOfMonth(month - 1)
    }


}