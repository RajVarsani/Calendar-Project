package com.example.thehinducalender

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.thehinducalender.animators.NavbarAnimator
import kotlinx.android.synthetic.main.navbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var homePageFragment: HomePageFragment
    private lateinit var festivalsAndHolidaysListFragment: FestivalsAndHolidaysListFragment
    private lateinit var mainCalenderFragment: MainCalenderFragment
    private lateinit var widgetsListFragment: WidgetsListFragment
    private lateinit var navbarAnimator: NavbarAnimator
    private var tempBull = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        setUp()


    }

    private fun setUp() {

        homePageFragment = HomePageFragment()
        festivalsAndHolidaysListFragment = FestivalsAndHolidaysListFragment()
        mainCalenderFragment = MainCalenderFragment()
        widgetsListFragment = WidgetsListFragment()
        navbarAnimator = NavbarAnimator()
        setUpFragment()
        setUpNavBarOnClickListeners()
    }

    private fun setUpNavBarOnClickListeners() {

        n_child_1_f.setOnClickListener {
            Log.e("NAV ANIM CHK", "clicked")
            if (tempBull) {
                navbarAnimator.setActive(navbar_child_1, n_child_1_e, n_child_1_f, 1000, 50)
            } else {
                navbarAnimator.setInactive(navbar_child_1, n_child_1_e, n_child_1_f, 1000, 50)

            }
        }
    }

    private fun setUpFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragmentFrameLayout, homePageFragment)
            commit()
        }
    }


}