package com.example.thehinducalender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var homePageFragment: HomePageFragment
    private lateinit var festivalsAndHolidaysListFragment: FestivalsAndHolidaysListFragment
    private lateinit var mainCalenderFragment: MainCalenderFragment
    private lateinit var widgetsListFragment: WidgetsListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setUp()


    }

    private fun setUp() {

        homePageFragment = HomePageFragment()
        festivalsAndHolidaysListFragment= FestivalsAndHolidaysListFragment()
        mainCalenderFragment=MainCalenderFragment()
        widgetsListFragment= WidgetsListFragment()
        setUpFragment()
    }

    private fun setUpFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragmentFrameLayout, widgetsListFragment)
            commit()
        }
    }


}