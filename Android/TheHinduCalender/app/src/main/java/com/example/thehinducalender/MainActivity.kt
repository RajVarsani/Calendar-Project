package com.example.thehinducalender

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.thehinducalender.animators.NavbarAnimator
import com.example.thehinducalender.daos.DisplayDensityDao

class MainActivity : AppCompatActivity() {

    private lateinit var homePageFragment: HomePageFragment
    private lateinit var festivalsAndHolidaysListFragment: FestivalsAndHolidaysListFragment
    private lateinit var mainCalenderFragment: MainCalenderFragment
    private lateinit var widgetsListFragment: WidgetsListFragment

    private lateinit var navbarAnimator: NavbarAnimator
    private lateinit var displayDensityDao: DisplayDensityDao

    private var currentActiveFragment = 2

    private lateinit var navbarButtons: Array<ConstraintLayout>
    private lateinit var navbarButtonsInactiveImages: Array<ImageView>
    private lateinit var navbarButtonsActiveImages: Array<ImageView>


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
        displayDensityDao = DisplayDensityDao()

        navbarButtons = arrayOf(
            findViewById(R.id.navbar_child_1),
            findViewById(R.id.navbar_child_2),
            findViewById(R.id.navbar_child_3),
            findViewById(R.id.navbar_child_4),
            findViewById(R.id.navbar_child_5),
        )

        navbarButtonsInactiveImages = arrayOf(
            findViewById(R.id.n_child_1_e),
            findViewById(R.id.n_child_2_e),
            findViewById(R.id.n_child_3_e),
            findViewById(R.id.n_child_4_e),
            findViewById(R.id.n_child_5_e),
        )

        navbarButtonsActiveImages = arrayOf(
            findViewById(R.id.n_child_1_f),
            findViewById(R.id.n_child_2_f),
            findViewById(R.id.n_child_3_f),
            findViewById(R.id.n_child_4_f),
            findViewById(R.id.n_child_5_f),
        )

        setUpFragment()


        navbarAnimator.setActive(
            navbarButtons[currentActiveFragment],
            navbarButtonsInactiveImages[currentActiveFragment],
            navbarButtonsActiveImages[currentActiveFragment],
            0,
            displayDensityDao.getPx(25)
        )

        setUpNavBarOnClickListeners()
    }

    private fun setUpNavBarOnClickListeners() {


        for (i in navbarButtons.indices) {
            navbarButtons[i].setOnClickListener {
                if (currentActiveFragment != i) {

                 setThisCurrentFragment(i)

                }
            }
        }

    }

    private fun setThisCurrentFragment(i: Int) {
        navbarAnimator.setInactive(
            navbarButtons[currentActiveFragment],
            navbarButtonsInactiveImages[currentActiveFragment],
            navbarButtonsActiveImages[currentActiveFragment],
            130
        )

        currentActiveFragment = i

        navbarAnimator.setActive(
            navbarButtons[currentActiveFragment],
            navbarButtonsInactiveImages[currentActiveFragment],
            navbarButtonsActiveImages[currentActiveFragment],
            160,
            displayDensityDao.getPx(25)
        )
    }

    private fun setUpFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragmentFrameLayout, homePageFragment)
            commit()
        }
    }

}