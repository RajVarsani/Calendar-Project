package com.example.thehinducalender.appwidgetdaos

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AppWidgetsDataHandlerDao {
    private val jsonDataForWidgets: Array<JSONObject?> = arrayOf(null, null, null)
    private val jsonFileNames = arrayOf("w_english_data", "w_hindi_data", "w_gujarati_data")


    private val dayOfWeekNames = arrayOf(
        arrayOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        ),
        arrayOf(
            "रविवार",
            "सोमवार",
            "मंगलवार",
            "बुधवार",
            "गुरुवार",
            "शुक्रवार",
            "शनिवार"
        ),
        arrayOf(
            "રવિવાર",
            "સોમવાર",
            "મંગળવાર",
            "બુધવાર",
            "ગુરૂવાર",
            "શુક્રવાર",
            "શનિવાર"
        )
    )


    private val monthNames = arrayOf(

        arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        ),
        arrayOf(
            "जनवरी",
            "फरवरी",
            "मार्च",
            "अप्रैल",
            "मई",
            "जून",
            "जुलाई",
            "अगस्त",
            "सितम्बर",
            "अक्टूबर",
            "नवम्बर",
            "दिसम्बर"
        ),
        arrayOf(
            "જાન્યુઆરી",
            "ફેબ્રુઆરી",
            "માર્ચ",
            "એપ્રિલ",
            "મે",
            "જૂન",
            "જુલાઈ",
            "ઑગસ્ટ",
            "સપ્ટેમ્બર",
            "ઑક્ટ્બર",
            "નવેમ્બર",
            "ડિસેમ્બર"
        ),
    )

    fun loadJsonDataFromAssets(context: Context, languageChoice: Int) {

        var noOfErrors = 0

        while (jsonDataForWidgets[languageChoice] == null) {
            try {
                val str: String =
                    context.assets.open(jsonFileNames[languageChoice] + ".json").bufferedReader()
                        .use { it.readText() }
                jsonDataForWidgets[languageChoice] = JSONObject(str)

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

                if (noOfErrors > 2) {
                    break
                } else {
                    noOfErrors++
                }
            }
        }
    }

    fun getCurrentDayOfWeek(languageChoice: Int): String {
        val calendar = Calendar.getInstance()
        return dayOfWeekNames[languageChoice][calendar[Calendar.DAY_OF_WEEK] - 1]
    }

    fun currentHinduYearInFormatForWidgets(): String {
        return if (currentMonth() < 11) {
            "Vikram Era 2077"
        } else if (currentMonth() == 11) {
            if (currentDayDate() >= 5) {
                "Vikram Era 2078"
            } else {
                "Vikram Era 2077"
            }
        } else {
            "Vikram Era 2077"
        }
    }

    fun currentTithiAndZodiacSignForWidgets(languageChoice: Int): Pair<String?, String?> {
        try {
            val dayData = jsonDataForWidgets[languageChoice]!!.getJSONArray("dateDetail")
                .getJSONArray(currentMonth() - 1).getJSONArray(currentDayDate() - 1)
            return Pair(dayData.getString(0), dayData.getString(1))
        } catch (jsonDataError: Exception) {
            Log.d(
                "tithi ERR",
                "Error is : $jsonDataError for month :${currentMonth()} day :${currentDayDate()} language :$languageChoice "
            )
        }
        return Pair(null, null)
    }

    fun getTodaysDate(languageChoice: Int): String {
        return "${currentDayDate()} ${monthNames[languageChoice][currentMonth() - 1]} ${currentYear()}"
    }

    fun currentChoghadiya(languageChoice: Int): String? {
        val choghadiyaIndex = choghadiyaIndexByHourAndMinutesW(currentHourW(), currentMinuteW())
        return choghadiyaByIndexW(choghadiyaIndex, currentDayOfWeekW(), languageChoice)
    }

    fun choghadiyaByIndexW(
        choghadiyaIndex: Int,
        dayOfWeek: Int,
        languageChoice: Int
    ): String? {
        try {
            val choghadiyaObj = jsonDataForWidgets[languageChoice]!!.getJSONObject("choghadiya")
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
        } catch (choghadiyaByIndexError: Exception) {
            Log.e("choghadiyaByIndex", "Error is $choghadiyaByIndexError")
        }
        return null
    }

    private fun choghadiyaIndexByHourAndMinutesW(hour: Int, minute: Int): Int {
        return ((hour.toFloat() + (minute.toFloat() / 60f)) / 1.5f).toInt()
    }

    private fun currentDayDate(): Int {
        val simpleDateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    private fun currentMonth(): Int {
        val simpleDateFormat = SimpleDateFormat("MM", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    private fun currentYear(): Int {
        val simpleDateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    private fun currentDayOfWeekW(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.DAY_OF_WEEK]
    }

    private fun currentHourW(): Int {
        val simpleDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }

    private fun currentMinuteW(): Int {
        val simpleDateFormat = SimpleDateFormat("mm", Locale.ENGLISH)
        return simpleDateFormat.format(Date()).toInt()
    }


}