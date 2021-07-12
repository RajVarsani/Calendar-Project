package com.example.thehinducalender.daos

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.thehinducalender.models.Festival
import com.example.thehinducalender.models.HomePageFestivalListModel
import com.example.thehinducalender.models.Quote
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class DataHandlerDao {

//    private var dataJsonObjectEnglish: JSONObject? = null
//    private var dataJsonObjectHindi: JSONObject? = null
//    private var dataJsonObjectGujarati: JSONObject? = null

    private val jsonDataObj: Array<JSONObject?> = arrayOf(null, null, null)

    private val jsonFileNames = arrayOf("english_data", "hindi_data", "gujarati_data")


    fun loadJsonDataFromAssets(context: Context, languageChoice: Int) {

        var noOfErrors = 0

        while (jsonDataObj[languageChoice] == null) {
            try {
                val str: String =
                    context.assets.open(jsonFileNames[languageChoice] + ".json").bufferedReader()
                        .use { it.readText() }
                jsonDataObj[languageChoice] = JSONObject(str)

                Log.e(
                    "JSON Fetch",
                    "Fetched Data for Choice : $languageChoice \nData: ${JSONObject(str)}"
                )


            } catch (errorLoadingJsonData: Exception) {

                Log.e(
                    "JSON DATA ERR",
                    "Failed Loading Data, choice : $languageChoice \n  Error : $errorLoadingJsonData"
                )

                Toast.makeText(
                    context,
                    "Some Error Occurred, If this continues consider Reporting this Error",
                    Toast.LENGTH_LONG
                ).show()

                if (noOfErrors > 10) {
                    break
                } else {

                    noOfErrors++
                }
            }
        }

    }


    /** HomePage **/


    fun quoteOfTheDayFOrDate(today: Int, languageChoice: Int): Quote? {
        val tempQuoteJsonObjToReturn =
            jsonDataObj[languageChoice]?.getJSONArray("Quotes")?.getJSONObject(today)

        return if (tempQuoteJsonObjToReturn != null) {
            Quote(
                tempQuoteJsonObjToReturn.getString("Quote"),
                "- " + tempQuoteJsonObjToReturn.getString("Author")
            )
        } else {
            null
        }
    }

    fun hinduMonthForThisMonth(month: Int, languageChoice: Int): String? {

        if (jsonDataObj[languageChoice] != null) {

            try {
                val monthObj: JSONArray =
                    jsonDataObj[languageChoice]!!.getJSONArray("dateDetail").getJSONArray(month)

                val lengthOfMonth = monthObj.length()

                val hinduMonthAtTheStartOfGivenMonth =
                    monthObj.getJSONArray(0).getString(0).split(" ")[0]
                val hinduMonthAtTheEndOfGivenMonth =
                    monthObj.getJSONArray(lengthOfMonth - 1).getString(0).split(" ")[0]

                return if (hinduMonthAtTheStartOfGivenMonth == hinduMonthAtTheEndOfGivenMonth) {
                    hinduMonthAtTheStartOfGivenMonth
                } else {
                    "$hinduMonthAtTheStartOfGivenMonth-$hinduMonthAtTheEndOfGivenMonth"

                }
            } catch (jsonDataError: Exception) {
                Log.d(
                    "MonthDATA ERROR",
                    "Error is : $jsonDataError for month :$month language :$languageChoice "
                )
            }

        }
        return null
    }

    fun getZodiacSign(month: Int, day: Int, languageChoice: Int): String? {
        try {
            return jsonDataObj[languageChoice]!!.getJSONArray("dateDetail").getJSONArray(month)
                .getJSONArray(day).getString(1)
        } catch (jsonDataError: Exception) {
            Log.d(
                "zodiac ERROR",
                "Error is : $jsonDataError for month :$month day :$day language :$languageChoice "
            )
        }
        return null
    }


    fun currentTithi(month: Int, day: Int, languageChoice: Int): String? {
        try {
            return jsonDataObj[languageChoice]!!.getJSONArray("dateDetail").getJSONArray(month)
                .getJSONArray(day).getString(0)
        } catch (jsonDataError: Exception) {
            Log.d(
                "tithi ERR",
                "Error is : $jsonDataError for month :$month day :$day language :$languageChoice "
            )
        }
        return null
    }


    fun getTodaysSpecialityList(
        month: Int,
        day: Int,
        languageChoice: Int
    ): ArrayList<HomePageFestivalListModel>? {
        try {
            val festivalsListObj: JSONArray =
                jsonDataObj[languageChoice]!!.getJSONArray("monthDetails").getJSONObject(month - 1)
                    .getJSONArray("festivals")

            Log.e("FESTIVAL OBJ", "obj is  : $festivalsListObj")

            val festivalListToReturn = ArrayList<HomePageFestivalListModel>(0)

            for (i in 0 until festivalsListObj.length()) {
                Log.e(
                    "ERR CHK SPE B",
                    "festivalsListObj.getJSONObject(i).getString(\"date\") is ${
                        festivalsListObj.getJSONObject(i).getString("date")
                    } for i $i"
                )
                if (festivalsListObj.getJSONObject(i).getString("date").toInt() == day) {

                    Log.e(
                        "ERR CHK A",
                        "festivalsListObj.getJSONObject(i).getString(\"festival\") is ${
                            festivalsListObj.getJSONObject(i).getString("festival")
                        } \n  festivalsListObj.getJSONObject(i).getString(\"description\") is ${
                            festivalsListObj.getJSONObject(
                                i
                            ).getString("description")
                        }  \n obj is ${
                            HomePageFestivalListModel(
                                festivalsListObj.getJSONObject(i).getString("festival"),
                                festivalsListObj.getJSONObject(i).getString("description")
                            )
                        }"
                    )


                    festivalListToReturn.add(
                        HomePageFestivalListModel(
                            festivalsListObj.getJSONObject(i).getString("festival"),
                            festivalsListObj.getJSONObject(i).getString("description")
                        )
                    )
                }

                if (i > day + 1) {
                    break
                }
            }

            return festivalListToReturn


        } catch (jsonDataError: Exception) {
            Log.e(
                "speciality ERR",
                "Error is : $jsonDataError for month :$month day :$day language :$languageChoice "
            )
        }
        return null
    }


    fun getChoghadiya(
        month: Int,
        dayOfWeek: Int,
        hour: Int,
        minute: Int,
        languageChoice: Int
    ): String? {

        Log.e("Choghadiya finder", "is being called")


        try {

            Log.e("try block", "is being called")

            val choghadiyaObj: JSONObject =
                jsonDataObj[languageChoice]!!.getJSONObject("choghadiya")


            Log.e("Choghadiya OBJ", "obj is  :$choghadiyaObj")


            val choghadiyaIndex = choghadiyaIndexByHourAndMinutes(hour, minute)

            Log.e(
                "ChoghadiyaIndC1",
                "choghadiya index:   :$choghadiyaIndex \n "
            )


            val currentChoghadiya: String =
                choghadiyaByIndex(choghadiyaIndex, choghadiyaObj, dayOfWeek)


            Log.e(
                "ChoghadiyaIndC2",
                "currentChoghadiya $currentChoghadiya \n "
            )


            val nextChoghadiya: String =
                choghadiyaByIndex(choghadiyaIndex + 1, choghadiyaObj, dayOfWeek)

            Log.e(
                "ChoghadiyaIndC3",
                " nextChoghadiya $nextChoghadiya  for inps "
            )


            return "$currentChoghadiya:$nextChoghadiya"

        } catch (jsonDataError: Exception) {
            Log.d(
                "Choghadiya ERR",
                "Error is : $jsonDataError for month :$month  language :$languageChoice "
            )
        }
        return null
    }

    private fun choghadiyaByIndex(
        choghadiyaIndex: Int,
        choghadiyaObj: JSONObject,
        dayOfWeek: Int
    ): String {

        Log.e(
            "ChoghadiyaByI",
            "called, values : choghadiyaIndex $choghadiyaIndex \nchoghadiyaObj $choghadiyaObj \ndayOfWeek $dayOfWeek"
        )

        Log.e("CH.Data", " day data  is  ${choghadiyaObj.getJSONArray("Day")} \n ")
        Log.e(
            "CH.Data2",
            " day data  is  ${choghadiyaObj.getJSONArray("Day").getJSONArray(dayOfWeek - 1)} \n "
        )

        return when {


            choghadiyaIndex in 4..11 -> {
                choghadiyaObj.getJSONArray("Day").getJSONArray(dayOfWeek - 1)
                    .getString(choghadiyaIndex - 4)
            }
            choghadiyaIndex <= 3 -> {


                if (dayOfWeek == 0) {
                    choghadiyaObj.getJSONArray("Night").getJSONArray(6)
                        .getString(choghadiyaIndex + 4)
                } else {
                    choghadiyaObj.getJSONArray("Night").getJSONArray(dayOfWeek - 1)
                        .getString(choghadiyaIndex + 4)
                }


            }
            else -> {
                choghadiyaObj.getJSONArray("Night").getJSONArray(dayOfWeek - 1)
                    .getString(choghadiyaIndex - 12)
            }
        }
    }

    private fun choghadiyaIndexByHourAndMinutes(hour: Int, minute: Int): Int {
        return ((hour.toFloat() + (minute.toFloat() / 60f)) / 1.5f).toInt()
    }

    fun getSunSetTime(month: Int, day: Int, languageChoice: Int): String? {

        try {
            return jsonDataObj[languageChoice]!!.getJSONArray("Sunset").getJSONArray(day - 1)
                .getString(month - 1)
        } catch (jsonDataError: Exception) {
            Log.d(
                "sunset ERR",
                "Error is : $jsonDataError for month :$month day :$day language :$languageChoice "
            )
        }
        return null
    }

    fun getSunriseTime(month: Int, day: Int, languageChoice: Int): String? {

        try {
            return jsonDataObj[languageChoice]!!.getJSONArray("sunrise").getJSONArray(day - 1)
                .getString(month - 1)
        } catch (jsonDataError: Exception) {
            Log.d(
                "Sunrise ERR",
                "Error is : $jsonDataError for month :$month day :$day language :$languageChoice "
            )
        }
        return null
    }

    fun noOfDaysInMonth(month: Int): Int {
        try {
            return jsonDataObj[0]!!.getJSONArray("monthDetails").getJSONObject(month)
                .getString("noOfDays").toInt()
        } catch (jsonDataError: Exception) {
            Log.d(
                "NoOfDays ERR",
                "Error is : $jsonDataError for month :$month "
            )
        }
        return 30
    }


    fun getFestivalsListOfTheMonth(
        month: Int,
        languageChoice: Int
    ): ArrayList<Festival>? {
        try {
            val festivalsListObj: JSONArray =
                jsonDataObj[languageChoice]!!.getJSONArray("monthDetails").getJSONObject(month - 1)
                    .getJSONArray("festivals")

            Log.e("FESTIVAL OBJ", "obj is  : $festivalsListObj")

            val festivalListToReturn = ArrayList<Festival>(0)

            for (i in 0 until festivalsListObj.length()) {

                var url = "https://en.wikipedia.org/"
                if (!(festivalsListObj.getJSONObject(i)
                        .getString("link") == "" || festivalsListObj.getJSONObject(i)
                        .getString("link") == " ")
                ) {
                    url = festivalsListObj.getJSONObject(i).getString("link")
                }

//                Log.e("CHK" ,"chk for $i \nFestival is ${Festival(
//                    false, festivalsListObj.getJSONObject(i).getString("festival"),
//                    festivalsListObj.getJSONObject(i).getString("date"),
//                    festivalsListObj.getJSONObject(i).getBoolean("isHoliday"),
//                    festivalsListObj.getJSONObject(i).getString("description"),
//                    URL(url),
//                    arrayOfStringFromJsonArray(
//                        festivalsListObj.getJSONObject(i).getJSONArray("images")
//                    ),
//                )}")



                festivalListToReturn.add(
                    Festival(
                        false, festivalsListObj.getJSONObject(i).getString("festival"),
                        festivalsListObj.getJSONObject(i).getString("date"),
                        festivalsListObj.getJSONObject(i).getBoolean("isHoliday"),
                        festivalsListObj.getJSONObject(i).getString("description"),
                        URL(url),
                        arrayOfStringFromJsonArray(
                            festivalsListObj.getJSONObject(i).getJSONArray("images")
                        ),
                    )
                )

            }

            return festivalListToReturn


        } catch (jsonDataError: Exception) {
            Log.e(
                "FESTIVAL ERR",
                "Error is : $jsonDataError for month :$month  language :$languageChoice "
            )
        }
        return null
    }

    private fun arrayOfStringFromJsonArray(jsonArray: JSONArray): Array<String> {
        val tempArrayList = ArrayList<String>(0)
        for (i in 0 until jsonArray.length()) {
            tempArrayList.add(jsonArray[i] as String)
        }

        return tempArrayList.toTypedArray()
    }


    fun loadTithiDataOfMonth(month: Int, languageChoice: Int): ArrayList<Int> {
        val listToReturn = ArrayList<Int>(0)

        try {

            val noOfDayInMonth = noOfDaysInMonth(month - 1)

            val monthObj =
                jsonDataObj[languageChoice]!!.getJSONArray("dateDetail").getJSONArray(month - 1)

            for (i in 0 until noOfDayInMonth) {
                val tithiObj = monthObj.getJSONArray(i).getString(0).split(" ")

                listToReturn.add(parseTithiObjToInt(tithiObj, languageChoice))

            }


        } catch (jsonDataError: Exception) {
            Log.e(
                "FESTIVAL ERR",
                "Error is : $jsonDataError for month :$month  language :$languageChoice "
            )
        }

        return listToReturn
    }

    private fun parseTithiObjToInt(tithiObj: List<String>, languageChoice: Int): Int {

        when (languageChoice) {
            0 -> {

                return parseTithiObjToIntByStringConditions("Amavasya", tithiObj)

            }
        }
        return 15
    }

    private fun parseTithiObjToIntByStringConditions(
        amasText: String,
        tithiObj: List<String>
    ): Int {
        return if (tithiObj.size == 2) {
            if (tithiObj[tithiObj.size - 1] == amasText) {
                0
            } else {
                15
            }
        } else {
            tithiObj[2].toInt()
        }
    }

    fun startIndexOfMonth(month: Int): Int {
        try {

            Log.e("StMI Called", "startIndexOfMonth is called")
            val day = jsonDataObj[0]!!.getJSONArray("monthDetails").getJSONObject(month)
                .getString("firstDay")

            Log.e("Date", "startIndexOfMonth is called")

            when (day) {
                "Sunday" -> {
                    return 0
                }
                "Monday" -> {
                    return 1
                }
                "Tuesday" -> {
                    return 2
                }
                "Wednesday" -> {
                    return 3
                }
                "Thursday" -> {
                    return 4
                }
                "Friday" -> {
                    return 5
                }
                "Saturday" -> {
                    return 6
                }
            }

        } catch (jsonDataError: Exception) {
            Log.e(
                "startInd",
                "Error is : $jsonDataError for month :$month "
            )
        }
        return 0

    }


}