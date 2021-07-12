package com.example.thehinducalender

//import kotlinx.android.synthetic.main.festival_description_image_layout_open_state.*
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.thehinducalender.models.Festival
import kotlinx.android.synthetic.main.activity_festival_details_page.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FestivalDetailsPage : AppCompatActivity() {

    /** Initializing all variables **/

    private val imageUrlsList = ArrayList<String>(0)

    private var currentImageIndex = 0
    private lateinit var handlerForImageSwap: Handler
    private val runnableForImageSwap = object : Runnable {
        override fun run() {
            Log.d("Handlers", "Handler Called")
            swapImage()
            handlerForImageSwap.postDelayed(this, 5000)
        }
    }


    override fun onPause() {
        super.onPause()
        handlerForImageSwap.removeCallbacks(runnableForImageSwap)
    }

    override fun onResume() {
        super.onResume()
        handlerForImageSwap.post(runnableForImageSwap)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_festival_details_page)
        window.sharedElementEnterTransition.duration = 250

        setUp()

    }

    private fun setUp() {

        val receivedFestivalData: Festival =
            intent.extras!!.getSerializable("Festival Data") as Festival
        val currentMonth: Int =
            intent.extras!!.getInt("month") + 1
        val currentYear: Int =
            intent.extras!!.getInt("year")


        @Suppress("UNCHECKED_CAST")
        val imageHierarchyList: ArrayList<Int> =
            intent.extras!!.getSerializable("imageHierarchyList") as ArrayList<Int>


        val urlArray = receivedFestivalData.imagesLink

        for (i in urlArray.indices) {
            imageUrlsList.add(urlArray[i])
        }

        currentImageIndex = imageHierarchyList[0]
        festivalDescriptionFestivalDetailPage.text = receivedFestivalData.description
        currentDayTextFestivalDetailPage.text =
            formattedDate(receivedFestivalData.date.toInt(), currentMonth, currentYear)
        festivalNameFestivalDescriptionPage.text=receivedFestivalData.name

        Glide
            .with(this)
            .load(imageUrlsList[imageHierarchyList[0]])
            .centerCrop()
            .placeholder(R.color.g_grey_text_color_one)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(
                Glide.with(this).load(imageUrlsList[imageHierarchyList[1]])
                    .transition(DrawableTransitionOptions.withCrossFade(300))

            )
            .error(
                Glide.with(this).load(imageUrlsList[imageHierarchyList[2]])
                    .transition(DrawableTransitionOptions.withCrossFade(300))
            )
            .into(festivalOrHolidayImageImageView)



        handlerForImageSwap = Handler(Looper.getMainLooper())
        handlerForImageSwap.postDelayed(runnableForImageSwap, 30000)


        if (receivedFestivalData.wikiLink == URL("https://en.wikipedia.org/")) {
            wikiLinkButton.visibility = View.GONE
        } else {
            wikiLinkButton.setOnClickListener {
                try {
                    val browserOpeningIntentForWikipediaFestivalPage =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(receivedFestivalData.wikiLink.toString())
                        )
                    startActivity(browserOpeningIntentForWikipediaFestivalPage)

                } catch (error: Exception) {
                    Log.e("WIKI WRR", "Error is $error")

                    Toast.makeText(this, "An error Occurred", Toast.LENGTH_LONG).show()
                }

            }

        }


    }


    fun swapImage() {
        Glide
            .with(this)
            .load(imageUrlsList[(currentImageIndex++) % 3])
            .centerCrop()
            .placeholder(R.color.g_grey_text_color_one)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .error(
                Glide.with(this).load(imageUrlsList[(currentImageIndex++) % 3])
                    .transition(DrawableTransitionOptions.withCrossFade(300))


            )
            .error(
                Glide.with(this).load(imageUrlsList[(currentImageIndex++) % 3])
                    .transition(DrawableTransitionOptions.withCrossFade(300))
            )

    }

    private fun formattedDate(day: Int, month: Int, year: Int): String {
        return "$day/$month/$year-${
            SimpleDateFormat("EE", Locale.ENGLISH).format(
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.ENGLISH
                ).parse("${formatDigitForDate(day)}/${formatDigitForDate(month)}/$year")!!
            )
        }"
    }

    private fun formatDigitForDate(digit: Int): String {
        return if (digit < 10) {
            "0$digit"
        } else {
            digit.toString()
        }
    }


}